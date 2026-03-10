# android-java-politica-enmascaramiento-amovil

Libreria Android (Java) para aplicar la politica de enmascaramiento con las 7 tecnicas: Sustitucion, Permutacion, Cifrado, Enmascaramiento Parcial, Envejecimiento de Fechas, Tokenizacion y Datos Sinteticos.

## Package

Se usa `co.com.amovil.masking` como package base. Es corto, consistente y deja espacio para futuras librerias dentro de `co.com.amovil`.

## Instalacion (local)

Incluye el modulo `masking` en tu proyecto Android:

```gradle
// settings.gradle
include(":masking")
```

```gradle
// build.gradle del app
implementation project(":masking")
```

## Instalacion desde GitHub (JitPack)

1) Agrega JitPack como repositorio:

```gradle
// settings.gradle (Gradle 7+)
dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
    maven { url 'https://jitpack.io' }
  }
}
```

2) Agrega la dependencia con el tag o commit que quieras usar:

```gradle
dependencies {
  implementation 'com.github.david0ql:android-java-politica-enmascaramiento-amovil:VERSION'
}
```

Ejemplos de VERSION:
- Un tag, por ejemplo `1.0.0`
- Un commit hash, por ejemplo `a1b2c3d`
- `main-SNAPSHOT` para la rama principal

## Uso basico

### 0) Componentes XML (atributos)

```xml
<co.com.amovil.masking.view.MaskingTextView
    android:id="@+id/nombre"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Juan Perez"
    app:maskingType="sustitucion"
    app:suffix="S.A.S." />

<co.com.amovil.masking.view.MaskingTextView
    android:id="@+id/cedula"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="1023456789"
    app:maskingType="enmascaramiento-parcial"
    app:visibleChars="4"
    app:maskChar="*" />

<co.com.amovil.masking.view.MaskingTextView
    android:id="@+id/correo"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="usuario@gmail.com"
    app:maskingType="enmascaramiento-parcial"
    app:valueType="email"
    app:maskChar="*" />

<co.com.amovil.masking.view.MaskingButton
    android:id="@+id/boton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="4111123456789012"
    app:maskingType="tokenizacion"
    app:tokenPrefix="TKN" />

<co.com.amovil.masking.view.MaskingCheckBox
    android:id="@+id/checkbox"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Maria Lopez"
    app:maskingType="datos-sinteticos" />

<co.com.amovil.masking.view.MaskingAutoCompleteTextView
    android:id="@+id/autocomplete"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Ana"
    app:maskingType="permutacion"
    app:swappedWith="Luis (Bogota)" />

<co.com.amovil.masking.view.MaskingEditText
    android:id="@+id/fecha"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="1990-05-12"
    app:maskingType="envejecimiento-fechas"
    app:offsetDays="-180"
    app:maskingMode="focus_lost" />
```

Componentes XML disponibles:

- MaskingTextView
- MaskingButton
- MaskingCheckBox
- MaskingRadioButton
- MaskingToggleButton
- MaskingCheckedTextView
- MaskingAutoCompleteTextView
- MaskingMultiAutoCompleteTextView
- MaskingEditText

Atributos XML relevantes:

| Atributo | Tipo | Aplica a | Descripción |
|---------|------|----------|-------------|
| `app:maskingType` | `string` | Todos los componentes | Tipo de enmascaramiento |
| `app:visibleChars` | `integer` | Enmascaramiento parcial | Cantidad de caracteres visibles al final para texto |
| `app:maskChar` | `string` | Enmascaramiento parcial | Carácter usado para enmascarar |
| `app:valueType` | `string` | Enmascaramiento parcial | Usa `text` o `email` |
| `app:suffix` | `string` | Sustitucion, Permutacion, Datos Sinteticos | Sufijo opcional para empresa, por ejemplo `S.A.` o `S.A.S.` |
| `app:swappedWith` | `string` | Permutacion | Valor alternativo a usar directamente |
| `app:offsetDays` | `integer` | Envejecimiento de fechas | Desplazamiento en días |
| `app:tokenPrefix` | `string` | Tokenizacion | Prefijo del token |
| `app:maskingMode` | `string` | `MaskingEditText` | Usa `focus_lost` o `text_changed` |

### 1) Enmascarar un valor (sin UI)

```java
MaskingRequest request = MaskingRequest.builder(MaskingType.TOKENIZACION, "4111123456789012")
    .tokenPrefix("TKN")
    .build();

MaskingResult result = MaskingEngine.mask(request);
String masked = result.getMasked();
```

Para empresas con sufijo:

```java
MaskingRequest companyRequest = MaskingRequest.builder(MaskingType.SUSTITUCION, "Acme")
    .suffix("S.A.S.")
    .build();

String maskedCompany = MaskingEngine.mask(companyRequest).getMasked();
// Laura Martinez S.A.S.
```

Regla:

- `suffix` aplica en `SUSTITUCION`, `PERMUTACION` y `DATOS_SINTETICOS`.
- En `PERMUTACION`, el sufijo solo se usa cuando no envías `swappedWith`.

Para correo con enmascaramiento parcial:

```java
MaskingRequest emailRequest = MaskingRequest.builder(
        MaskingType.ENMASCARAMIENTO_PARCIAL,
        "usuario@gmail.com")
    .valueType(MaskingValueType.EMAIL)
    .maskChar("*")
    .build();

String maskedEmail = MaskingEngine.mask(emailRequest).getMasked();
// *******@gmail.com
```

Regla:

- Si `valueType` es `MaskingValueType.EMAIL`, la librería enmascara la parte antes de `@` y conserva el dominio.
- Si el correo no tiene un formato válido, el resultado será `****@dominio-ficticio.test`.

### 2) TextView y componentes derivados (Button, Chip, etc.)

```java
TextView textView = findViewById(R.id.nombre);
MaskingRequest request = MaskingRequest.builder(MaskingType.SUSTITUCION, "Juan Perez")
    .suffix("S.A.")
    .build();
MaskingViewApplier.apply(textView, request);
```

### 3) EditText (enmascarar al perder foco)

```java
EditText editText = findViewById(R.id.cedula);
MaskingEditTextController.attach(editText, MaskingType.ENMASCARAMIENTO_PARCIAL)
    .visibleChars(4)
    .maskChar("*")
    .mode(MaskingEditTextController.Mode.ON_FOCUS_LOST)
    .build();

String original = MaskingEditTextController.getOriginalText(editText);
```

Si el valor es correo, usa `.valueType(MaskingValueType.EMAIL)` en el builder del controller o `app:valueType="email"` en XML. Cuando el correo no trae dominio válido, la librería retorna `****@dominio-ficticio.test`.

Para nombres de empresa puedes usar `.suffix("S.A.")` o `.suffix("S.A.S.")` en el builder, o `app:suffix="S.A.S."` en XML.

Si necesitas el mismo comportamiento en `MaskingEditTextController`, también puedes hacer:

```java
MaskingEditTextController.attach(editText, MaskingType.SUSTITUCION)
    .suffix("S.A.S.")
    .mode(MaskingEditTextController.Mode.ON_FOCUS_LOST)
    .build();
```

### 4) Spinner

```java
Spinner spinner = findViewById(R.id.spinner);
List<String> valores = Arrays.asList("4111123456789012", "5555444433332222");
MaskingSpinnerAdapter adapter = MaskingSpinnerAdapter.fromStrings(
    spinner.getContext(), valores, MaskingType.TOKENIZACION);
spinner.setAdapter(adapter);
```

### 5) ListView / Listas simples

```java
ListView listView = findViewById(R.id.lista);
List<String> valores = Arrays.asList("Ana", "Maria", "Juan");
MaskingArrayAdapter adapter = MaskingArrayAdapter.fromStrings(
    listView.getContext(), valores, MaskingType.SUSTITUCION);
listView.setAdapter(adapter);
```

## Tipos de enmascaramiento

1. Sustitucion
2. Permutacion
3. Cifrado (Base64)
4. Enmascaramiento Parcial
5. Envejecimiento de Fechas
6. Tokenizacion
7. Datos Sinteticos

Reglas importantes:

- `suffix` está disponible en `SUSTITUCION`, `PERMUTACION` y `DATOS_SINTETICOS`.
- `valueType` solo aplica a `ENMASCARAMIENTO_PARCIAL`.
- En `PERMUTACION`, si envías `swappedWith`, ese valor se usa tal cual y no se modifica con `suffix`.

## Tests

```bash
./gradlew :masking:test
```
