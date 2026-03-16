# android-java-politica-enmascaramiento-amovil

Librería Android (Java) para aplicar la política de enmascaramiento con 7 técnicas: Sustitución, Permutación, Cifrado, Enmascaramiento Parcial, Envejecimiento de Fechas, Tokenización y Datos Sintéticos.

## Package

`co.com.amovil.masking`

## Instalación desde GitHub (JitPack)

Agrega JitPack como repositorio en `settings.gradle` (Gradle 7+):

```gradle
dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
    maven { url 'https://jitpack.io' }
  }
}
```

O en Kotlin DSL (`settings.gradle.kts`):

```kotlin
maven { url = uri("https://jitpack.io") }
```

Agrega la dependencia:

```gradle
dependencies {
  implementation 'com.github.david0ql:android-java-politica-enmascaramiento-amovil:1.0.1'
}
```

Versiones disponibles:

| VERSION | Descripción |
|---------|-------------|
| `1.0.1` | Versión estable actual |
| `1.0.0` | Primera versión |
| `main-SNAPSHOT` | Rama principal (puede ser inestable) |

## Instalación local

Incluye el módulo `masking` en tu proyecto Android:

```gradle
// settings.gradle
include(':masking')
project(':masking').projectDir = new File('ruta/al/modulo/masking')
```

```gradle
// app/build.gradle
dependencies {
  implementation project(':masking')
}
```

## Componentes XML disponibles

| Componente | Extiende |
|-----------|---------|
| `MaskingTextView` | AppCompatTextView |
| `MaskingButton` | AppCompatButton |
| `MaskingCheckBox` | AppCompatCheckBox |
| `MaskingRadioButton` | AppCompatRadioButton |
| `MaskingToggleButton` | ToggleButton |
| `MaskingCheckedTextView` | AppCompatCheckedTextView |
| `MaskingAutoCompleteTextView` | AppCompatAutoCompleteTextView |
| `MaskingMultiAutoCompleteTextView` | AppCompatMultiAutoCompleteTextView |
| `MaskingEditText` | AppCompatEditText |

## Atributos XML

| Atributo | Tipo | Aplica a | Descripción |
|---------|------|----------|-------------|
| `app:maskingType` | `string` | Todos | Tipo de enmascaramiento |
| `app:visibleChars` | `integer` | Enmascaramiento parcial | Caracteres visibles al final |
| `app:maskChar` | `string` | Enmascaramiento parcial | Carácter de máscara |
| `app:valueType` | `string` | Enmascaramiento parcial | `text` o `email` |
| `app:suffix` | `string` | Sustitución, Permutación, Datos Sintéticos | Sufijo de empresa (ej. `S.A.S.`) |
| `app:swappedWith` | `string` | Permutación | Valor alternativo directo |
| `app:offsetDays` | `integer` | Envejecimiento de fechas | Desplazamiento en días |
| `app:tokenPrefix` | `string` | Tokenización | Prefijo del token |
| `app:maskingMode` | `string` | `MaskingEditText` | `focus_lost` o `text_changed` |

## Uso básico

### 0) Componentes XML

```xml
<!-- Sustitución con sufijo -->
<co.com.amovil.masking.view.MaskingTextView
    android:id="@+id/nombre"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Juan Perez"
    app:maskingType="sustitucion"
    app:suffix="S.A.S." />

<!-- Enmascaramiento parcial de número -->
<co.com.amovil.masking.view.MaskingTextView
    android:id="@+id/cedula"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="1023456789"
    app:maskingType="enmascaramiento-parcial"
    app:visibleChars="4"
    app:maskChar="*" />

<!-- Enmascaramiento parcial de email -->
<co.com.amovil.masking.view.MaskingTextView
    android:id="@+id/correo"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="usuario@gmail.com"
    app:maskingType="enmascaramiento-parcial"
    app:valueType="email"
    app:maskChar="*" />

<!-- Tokenización en Button -->
<co.com.amovil.masking.view.MaskingButton
    android:id="@+id/boton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="4111123456789012"
    app:maskingType="tokenizacion"
    app:tokenPrefix="TKN" />

<!-- Datos sintéticos en CheckBox -->
<co.com.amovil.masking.view.MaskingCheckBox
    android:id="@+id/checkbox"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Maria Lopez"
    app:maskingType="datos-sinteticos" />

<!-- Permutación en AutoComplete -->
<co.com.amovil.masking.view.MaskingAutoCompleteTextView
    android:id="@+id/autocomplete"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Ana"
    app:maskingType="permutacion"
    app:swappedWith="Luis (Bogota)" />

<!-- Envejecimiento de fechas en EditText -->
<co.com.amovil.masking.view.MaskingEditText
    android:id="@+id/fecha"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="1990-05-12"
    app:maskingType="envejecimiento-fechas"
    app:offsetDays="-180"
    app:maskingMode="focus_lost" />
```

### 1) Enmascarar un valor (sin UI)

```java
import co.com.amovil.masking.MaskingEngine;
import co.com.amovil.masking.MaskingRequest;
import co.com.amovil.masking.MaskingResult;
import co.com.amovil.masking.MaskingType;
import co.com.amovil.masking.MaskingValueType;

// Tokenización
MaskingRequest request = MaskingRequest.builder(MaskingType.TOKENIZACION, "4111123456789012")
    .tokenPrefix("TKN")
    .build();
String masked = MaskingEngine.mask(request).getMasked();
// TKN-A1B2-C3D4 (valor determinístico basado en hash)

// Sustitución con sufijo de empresa
MaskingRequest companyRequest = MaskingRequest.builder(MaskingType.SUSTITUCION, "Acme")
    .suffix("S.A.S.")
    .build();
String maskedCompany = MaskingEngine.mask(companyRequest).getMasked();
// Laura Martinez S.A.S.

// Email con enmascaramiento parcial
MaskingRequest emailRequest = MaskingRequest.builder(MaskingType.ENMASCARAMIENTO_PARCIAL, "usuario@gmail.com")
    .valueType(MaskingValueType.EMAIL)
    .maskChar("*")
    .build();
String maskedEmail = MaskingEngine.mask(emailRequest).getMasked();
// *******@gmail.com
```

### 2) TextView y componentes derivados

```java
import co.com.amovil.masking.view.MaskingViewApplier;

TextView textView = findViewById(R.id.nombre);
MaskingRequest request = MaskingRequest.builder(MaskingType.SUSTITUCION, "Juan Perez")
    .suffix("S.A.")
    .build();
MaskingViewApplier.apply(textView, request);
```

### 3) EditText (enmascarar al perder foco)

```java
import co.com.amovil.masking.view.MaskingEditTextController;

EditText editText = findViewById(R.id.cedula);
MaskingEditTextController.attach(editText, MaskingType.ENMASCARAMIENTO_PARCIAL)
    .visibleChars(4)
    .maskChar("*")
    .mode(MaskingEditTextController.Mode.ON_FOCUS_LOST)
    .build();

// Recuperar el valor original
String original = MaskingEditTextController.getOriginalText(editText);
```

### 4) Spinner

```java
import co.com.amovil.masking.view.MaskingSpinnerAdapter;

Spinner spinner = findViewById(R.id.spinner);
List<String> valores = Arrays.asList("4111123456789012", "5555444433332222");
MaskingSpinnerAdapter adapter = MaskingSpinnerAdapter.fromStrings(
    spinner.getContext(), valores, MaskingType.TOKENIZACION);
spinner.setAdapter(adapter);
```

### 5) ListView

```java
import co.com.amovil.masking.view.MaskingArrayAdapter;

ListView listView = findViewById(R.id.lista);
List<String> valores = Arrays.asList("Ana", "Maria", "Juan");
MaskingArrayAdapter adapter = MaskingArrayAdapter.fromStrings(
    listView.getContext(), valores, MaskingType.SUSTITUCION);
listView.setAdapter(adapter);
```

## Tipos de enmascaramiento

| Tipo (enum) | Slug XML | Descripción |
|-------------|---------|-------------|
| `SUSTITUCION` | `sustitucion` | Reemplaza con nombre sintético colombiano determinístico |
| `PERMUTACION` | `permutacion` | Intercambia por otro nombre o valor explícito (`swappedWith`) |
| `CIFRADO` | `cifrado` | Codifica en Base64 |
| `ENMASCARAMIENTO_PARCIAL` | `enmascaramiento-parcial` | Enmascara dejando N caracteres visibles; modo especial para email |
| `ENVEJECIMIENTO_FECHAS` | `envejecimiento-fechas` | Desplaza una fecha en días (`offsetDays`) |
| `TOKENIZACION` | `tokenizacion` | Genera token con prefijo y hash hexadecimal |
| `DATOS_SINTETICOS` | `datos-sinteticos` | Genera nombre sintético con semilla diferente a sustitución |

### Reglas importantes

- `suffix` aplica en `SUSTITUCION`, `PERMUTACION` y `DATOS_SINTETICOS`.
- En `PERMUTACION`, si se envía `swappedWith`, el sufijo se ignora.
- `valueType` solo aplica a `ENMASCARAMIENTO_PARCIAL`.
- Todos los enmascaramientos son **determinísticos**: el mismo input siempre produce el mismo output.
- Si un email tiene formato inválido, el resultado es `****@dominio-ficticio.test`.

## Tests

```bash
./gradlew :masking:test
```
