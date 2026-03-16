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
  implementation 'com.github.david0ql:android-java-politica-enmascaramiento-amovil:1.0.4'
}
```

Versiones disponibles:

| VERSION | Descripción |
|---------|-------------|
| `1.0.4` | **Versión estable actual** — inicialización global obligatoria con `MaskingConfig` |
| `1.0.2` | `maskingType` con autocompletado en Android Studio |
| `1.0.1` | Soporte JitPack con Gradle wrapper y maven-publish |
| `1.0.0` | Primera versión |

## Instalación local

```gradle
// settings.gradle
include(':masking')
project(':masking').projectDir = new File('ruta/al/modulo/masking')

// app/build.gradle
dependencies {
  implementation project(':masking')
}
```

## Inicialización obligatoria

La librería **debe** ser inicializada una sola vez en tu clase `Application`. Si cualquier componente o `MaskingEngine.mask()` se ejecuta antes de llamar a `init()`, la app lanzará un `IllegalStateException` con instrucciones claras.

### 1. Crea tu clase Application

```java
import android.app.Application;
import co.com.amovil.masking.MaskingConfig;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MaskingConfig.init(this, true);
        // false → desactiva el enmascaramiento y devuelve los valores originales
        // útil para ambientes de desarrollo o debug
    }
}
```

Si ya tienes una clase que extiende `Application` (por ejemplo con `UncaughtExceptionHandler`):

```java
public class ApplicationContext extends Application
        implements Thread.UncaughtExceptionHandler {

    @Override
    public void onCreate() {
        super.onCreate();
        MaskingConfig.init(this, !BuildConfig.DEBUG); // enmascarar solo en release
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // manejo de errores globales
    }
}
```

### 2. Registra la clase en AndroidManifest.xml

```xml
<application
    android:name=".ApplicationContext"
    android:icon="@mipmap/ic_launcher"
    ...>
```

### Comportamiento por configuración

| `MaskingConfig.init(ctx, ...)` | Resultado de `MaskingEngine.mask()` |
|-------------------------------|--------------------------------------|
| `true` | Aplica el enmascaramiento normalmente |
| `false` | Devuelve el valor original sin cambios |
| No llamado | `IllegalStateException` — la app falla |

### Verificar estado (opcional)

```java
if (MaskingConfig.isInitialized()) {
    // seguro para usar
}
```

## Tipos de enmascaramiento

Los 7 tipos disponibles, con su valor para XML (`app:maskingType`) y su constante Java (`MaskingType`):

| XML (`app:maskingType`) | Java (`MaskingType`) | Descripción |
|------------------------|----------------------|-------------|
| `sustitucion` | `SUSTITUCION` | Reemplaza con nombre sintético colombiano determinístico |
| `permutacion` | `PERMUTACION` | Intercambia por otro nombre o por el valor de `app:swappedWith` |
| `cifrado` | `CIFRADO` | Codifica el valor en Base64 |
| `enmascaramiento_parcial` | `ENMASCARAMIENTO_PARCIAL` | Enmascara dejando N caracteres visibles; modo especial para email |
| `envejecimiento_fechas` | `ENVEJECIMIENTO_FECHAS` | Desplaza una fecha en días según `app:offsetDays` |
| `tokenizacion` | `TOKENIZACION` | Genera token con prefijo y hash hexadecimal |
| `datos_sinteticos` | `DATOS_SINTETICOS` | Genera nombre sintético con semilla diferente a sustitución |

> **Nota:** Los tipos multi-palabra usan guión bajo (`_`) en XML desde la versión `1.0.2`.
> Todos los enmascaramientos son **determinísticos**: el mismo input siempre produce el mismo output.

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
| `app:maskingType` | enum | Todos | Tipo de enmascaramiento (con autocompletado) |
| `app:visibleChars` | integer | `enmascaramiento_parcial` | Caracteres visibles al final |
| `app:maskChar` | string | `enmascaramiento_parcial` | Carácter de máscara (ej. `*`) |
| `app:valueType` | enum | `enmascaramiento_parcial` | `text` o `email` |
| `app:suffix` | string | `sustitucion`, `permutacion`, `datos_sinteticos` | Sufijo de empresa (ej. `S.A.S.`) |
| `app:swappedWith` | string | `permutacion` | Valor alternativo directo |
| `app:offsetDays` | integer | `envejecimiento_fechas` | Desplazamiento en días (ej. `-180`) |
| `app:tokenPrefix` | string | `tokenizacion` | Prefijo del token (ej. `TKN`) |
| `app:maskingMode` | enum | `MaskingEditText` | `focus_lost` o `text_changed` |

## Uso básico

### 0) Componentes XML

```xml
<!-- Sustitución con sufijo de empresa -->
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
    app:maskingType="enmascaramiento_parcial"
    app:visibleChars="4"
    app:maskChar="*" />

<!-- Enmascaramiento parcial de email -->
<co.com.amovil.masking.view.MaskingTextView
    android:id="@+id/correo"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="usuario@gmail.com"
    app:maskingType="enmascaramiento_parcial"
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
    app:maskingType="datos_sinteticos" />

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
    app:maskingType="envejecimiento_fechas"
    app:offsetDays="-180"
    app:maskingMode="focus_lost" />

<!-- Cifrado Base64 en RadioButton -->
<co.com.amovil.masking.view.MaskingRadioButton
    android:id="@+id/radio"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="datos-sensibles"
    app:maskingType="cifrado" />
```

### 1) Enmascarar un valor (sin UI)

```java
import co.com.amovil.masking.MaskingEngine;
import co.com.amovil.masking.MaskingRequest;
import co.com.amovil.masking.MaskingType;
import co.com.amovil.masking.MaskingValueType;

// Sustitución
String nombre = MaskingEngine.mask(
    MaskingRequest.builder(MaskingType.SUSTITUCION, "Juan Perez").build()
).getMasked();
// → "Sofia Martinez"

// Permutación con valor fijo
String permutado = MaskingEngine.mask(
    MaskingRequest.builder(MaskingType.PERMUTACION, "Ana")
        .swappedWith("Luis (Bogota)").build()
).getMasked();
// → "Luis (Bogota)"

// Cifrado Base64
String cifrado = MaskingEngine.mask(
    MaskingRequest.builder(MaskingType.CIFRADO, "dato-sensible").build()
).getMasked();
// → "ZGF0by1zZW5zaWJsZQ=="

// Enmascaramiento parcial de número
String cedula = MaskingEngine.mask(
    MaskingRequest.builder(MaskingType.ENMASCARAMIENTO_PARCIAL, "1023456789")
        .visibleChars(4).maskChar("*").build()
).getMasked();
// → "******6789"

// Enmascaramiento parcial de email
String email = MaskingEngine.mask(
    MaskingRequest.builder(MaskingType.ENMASCARAMIENTO_PARCIAL, "usuario@gmail.com")
        .valueType(MaskingValueType.EMAIL).maskChar("*").build()
).getMasked();
// → "*******@gmail.com"

// Envejecimiento de fechas
String fecha = MaskingEngine.mask(
    MaskingRequest.builder(MaskingType.ENVEJECIMIENTO_FECHAS, "1990-05-12")
        .offsetDays(-180).build()
).getMasked();
// → "1989-11-13"

// Tokenización
String token = MaskingEngine.mask(
    MaskingRequest.builder(MaskingType.TOKENIZACION, "4111123456789012")
        .tokenPrefix("TKN").build()
).getMasked();
// → "TKN-A1B2-C3D4" (determinístico)

// Datos sintéticos con sufijo de empresa
String empresa = MaskingEngine.mask(
    MaskingRequest.builder(MaskingType.DATOS_SINTETICOS, "Acme Corp")
        .suffix("S.A.S.").build()
).getMasked();
// → "Carlos Diaz S.A.S."
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

## Reglas importantes

- `suffix` aplica en `SUSTITUCION`, `PERMUTACION` y `DATOS_SINTETICOS`.
- En `PERMUTACION`, si se envía `swappedWith`, el sufijo se ignora.
- `valueType` solo aplica a `ENMASCARAMIENTO_PARCIAL`.
- Si un email tiene formato inválido, el resultado es `****@dominio-ficticio.test`.
- Todos los enmascaramientos son **determinísticos**: el mismo input siempre produce el mismo output.

## Tests

```bash
./gradlew :masking:test
```
