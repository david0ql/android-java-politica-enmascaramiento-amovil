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
    app:maskingType="sustitucion" />

<co.com.amovil.masking.view.MaskingTextView
    android:id="@+id/cedula"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="1023456789"
    app:maskingType="enmascaramiento-parcial"
    app:visibleChars="4"
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

### 1) Enmascarar un valor (sin UI)

```java
MaskingRequest request = MaskingRequest.builder(MaskingType.TOKENIZACION, "4111123456789012")
    .tokenPrefix("TKN")
    .build();

MaskingResult result = MaskingEngine.mask(request);
String masked = result.getMasked();
```

### 2) TextView y componentes derivados (Button, Chip, etc.)

```java
TextView textView = findViewById(R.id.nombre);
MaskingRequest request = MaskingRequest.builder(MaskingType.SUSTITUCION, "Juan Perez").build();
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

## Tests

```bash
./gradlew :masking:test
```
