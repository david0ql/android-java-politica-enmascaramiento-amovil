package co.com.amovil.masking;

public enum MaskingValueType {
  TEXT("text"),
  EMAIL("email");

  private final String value;

  MaskingValueType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static MaskingValueType fromValue(String value) {
    if (value == null || value.trim().isEmpty()) {
      return TEXT;
    }
    for (MaskingValueType type : values()) {
      if (type.value.equalsIgnoreCase(value)) {
        return type;
      }
    }
    return TEXT;
  }

  public static MaskingValueType fromAttrValue(int value) {
    return value == 1 ? EMAIL : TEXT;
  }
}
