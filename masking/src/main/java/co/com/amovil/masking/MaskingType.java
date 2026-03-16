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

  public static MaskingType fromAttrValue(int value) {
    switch (value) {
      case 0: return SUSTITUCION;
      case 1: return PERMUTACION;
      case 2: return CIFRADO;
      case 3: return ENMASCARAMIENTO_PARCIAL;
      case 4: return ENVEJECIMIENTO_FECHAS;
      case 5: return TOKENIZACION;
      case 6: return DATOS_SINTETICOS;
      default: return null;
    }
  }
}
