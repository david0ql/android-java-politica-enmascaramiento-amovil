package co.com.amovil.masking;

import java.util.Locale;

public enum MaskingType {
  SUSTITUCION("sustitucion"),
  PERMUTACION("permutacion"),
  CIFRADO("cifrado"),
  ENMASCARAMIENTO_PARCIAL("enmascaramiento-parcial"),
  ENVEJECIMIENTO_FECHAS("envejecimiento-fechas"),
  TOKENIZACION("tokenizacion"),
  DATOS_SINTETICOS("datos-sinteticos");

  private final String slug;

  MaskingType(String slug) {
    this.slug = slug;
  }

  public String toSlug() {
    return slug;
  }

  public static MaskingType fromSlug(String value) {
    if (value == null) {
      return null;
    }
    String normalized = value.toLowerCase(Locale.US).trim();
    for (MaskingType type : values()) {
      if (type.slug.equals(normalized)) {
        return type;
      }
    }
    return null;
  }
}
