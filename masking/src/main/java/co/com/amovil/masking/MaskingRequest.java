package co.com.amovil.masking;

public final class MaskingRequest {
  private final MaskingType type;
  private final String original;
  private final String swappedWith;
  private final String algorithm;
  private final Integer visibleChars;
  private final String maskChar;
  private final MaskingValueType valueType;
  private final String suffix;
  private final String originalDate;
  private final Integer offsetDays;
  private final String tokenPrefix;

  private MaskingRequest(Builder builder) {
    this.type = builder.type;
    this.original = builder.original;
    this.swappedWith = builder.swappedWith;
    this.algorithm = builder.algorithm;
    this.visibleChars = builder.visibleChars;
    this.maskChar = builder.maskChar;
    this.valueType = builder.valueType;
    this.suffix = builder.suffix;
    this.originalDate = builder.originalDate;
    this.offsetDays = builder.offsetDays;
    this.tokenPrefix = builder.tokenPrefix;
  }

  public MaskingType getType() {
    return type;
  }

  public String getOriginal() {
    return original;
  }

  public String getSwappedWith() {
    return swappedWith;
  }

  public String getAlgorithm() {
    return algorithm;
  }

  public Integer getVisibleChars() {
    return visibleChars;
  }

  public String getMaskChar() {
    return maskChar;
  }

  public MaskingValueType getValueType() {
    return valueType != null ? valueType : MaskingValueType.TEXT;
  }

  public String getOriginalDate() {
    return originalDate;
  }

  public String getSuffix() {
    return suffix;
  }

  public Integer getOffsetDays() {
    return offsetDays;
  }

  public String getTokenPrefix() {
    return tokenPrefix;
  }

  public static Builder builder(MaskingType type, String original) {
    return new Builder(type, original);
  }

  public static final class Builder {
    private final MaskingType type;
    private final String original;
    private String swappedWith;
    private String algorithm;
    private Integer visibleChars;
    private String maskChar;
    private MaskingValueType valueType;
    private String suffix;
    private String originalDate;
    private Integer offsetDays;
    private String tokenPrefix;

    private Builder(MaskingType type, String original) {
      this.type = type;
      this.original = original;
    }

    public Builder swappedWith(String swappedWith) {
      this.swappedWith = swappedWith;
      return this;
    }

    public Builder algorithm(String algorithm) {
      this.algorithm = algorithm;
      return this;
    }

    public Builder visibleChars(Integer visibleChars) {
      this.visibleChars = visibleChars;
      return this;
    }

    public Builder maskChar(String maskChar) {
      this.maskChar = maskChar;
      return this;
    }

    public Builder valueType(MaskingValueType valueType) {
      this.valueType = valueType;
      return this;
    }

    public Builder suffix(String suffix) {
      this.suffix = suffix;
      return this;
    }

    public Builder originalDate(String originalDate) {
      this.originalDate = originalDate;
      return this;
    }

    public Builder offsetDays(Integer offsetDays) {
      this.offsetDays = offsetDays;
      return this;
    }

    public Builder tokenPrefix(String tokenPrefix) {
      this.tokenPrefix = tokenPrefix;
      return this;
    }

    public MaskingRequest build() {
      if (type == null) {
        throw new IllegalArgumentException("Masking type is required");
      }
      if (original == null) {
        throw new IllegalArgumentException("Original value is required");
      }
      return new MaskingRequest(this);
    }
  }
}
