package co.com.amovil.masking.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import co.com.amovil.masking.MaskingEngine;
import co.com.amovil.masking.MaskingRequest;
import co.com.amovil.masking.MaskingResult;
import co.com.amovil.masking.MaskingType;
import co.com.amovil.masking.R;

final class MaskingTextConfig {
  private MaskingType maskingType;
  private Integer visibleChars;
  private String maskChar;
  private Integer offsetDays;
  private String tokenPrefix;
  private String swappedWith;
  private String algorithm;

  void loadFromAttrs(Context context, AttributeSet attrs) {
    if (attrs == null || context == null) {
      return;
    }
    TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MaskingTextView);
    try {
      maskingType = MaskingType.fromSlug(array.getString(R.styleable.MaskingTextView_maskingType));
      if (array.hasValue(R.styleable.MaskingTextView_visibleChars)) {
        visibleChars = array.getInt(R.styleable.MaskingTextView_visibleChars, 4);
      }
      maskChar = array.getString(R.styleable.MaskingTextView_maskChar);
      if (array.hasValue(R.styleable.MaskingTextView_offsetDays)) {
        offsetDays = array.getInt(R.styleable.MaskingTextView_offsetDays, -180);
      }
      tokenPrefix = array.getString(R.styleable.MaskingTextView_tokenPrefix);
      swappedWith = array.getString(R.styleable.MaskingTextView_swappedWith);
      algorithm = array.getString(R.styleable.MaskingTextView_algorithm);
    } finally {
      array.recycle();
    }
  }

  MaskingResult apply(String original) {
    if (maskingType == null) {
      return null;
    }
    String safeOriginal = original == null ? "" : original;
    MaskingRequest.Builder builder = MaskingRequest.builder(maskingType, safeOriginal);
    if (visibleChars != null) {
      builder.visibleChars(visibleChars);
    }
    if (maskChar != null) {
      builder.maskChar(maskChar);
    }
    if (offsetDays != null) {
      builder.offsetDays(offsetDays);
    }
    if (tokenPrefix != null) {
      builder.tokenPrefix(tokenPrefix);
    }
    if (swappedWith != null) {
      builder.swappedWith(swappedWith);
    }
    if (algorithm != null) {
      builder.algorithm(algorithm);
    }
    return MaskingEngine.mask(builder.build());
  }

  MaskingType getMaskingType() {
    return maskingType;
  }

  void setMaskingType(MaskingType maskingType) {
    this.maskingType = maskingType;
  }

  void setVisibleChars(Integer visibleChars) {
    this.visibleChars = visibleChars;
  }

  void setMaskChar(String maskChar) {
    this.maskChar = maskChar;
  }

  void setOffsetDays(Integer offsetDays) {
    this.offsetDays = offsetDays;
  }

  void setTokenPrefix(String tokenPrefix) {
    this.tokenPrefix = tokenPrefix;
  }

  void setSwappedWith(String swappedWith) {
    this.swappedWith = swappedWith;
  }

  void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }
}
