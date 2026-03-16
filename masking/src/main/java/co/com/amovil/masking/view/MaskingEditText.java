package co.com.amovil.masking.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import co.com.amovil.masking.MaskingResult;
import co.com.amovil.masking.MaskingType;
import co.com.amovil.masking.MaskingValueType;
import co.com.amovil.masking.R;

public class MaskingEditText extends AppCompatEditText {
  private MaskingEditTextController controller;
  private final MaskingTextConfig config = new MaskingTextConfig();
  private boolean isApplying;

  public MaskingEditText(Context context) {
    super(context);
    init(null);
  }

  public MaskingEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public MaskingEditText(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  private void init(AttributeSet attrs) {
    config.loadFromAttrs(getContext(), attrs);
    if (attrs == null) {
      return;
    }
    TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.MaskingEditText);
    try {
      if (!array.hasValue(R.styleable.MaskingEditText_maskingType)) {
        return;
      }
      MaskingType maskingType = MaskingType.fromAttrValue(
          array.getInt(R.styleable.MaskingEditText_maskingType, -1));
      if (maskingType == null) {
        return;
      }
      MaskingEditTextController.Builder builder = MaskingEditTextController.attach(this, maskingType);
      if (array.hasValue(R.styleable.MaskingEditText_visibleChars)) {
        builder.visibleChars(array.getInt(R.styleable.MaskingEditText_visibleChars, 4));
      }
      String maskChar = array.getString(R.styleable.MaskingEditText_maskChar);
      if (maskChar != null) {
        builder.maskChar(maskChar);
      }
      if (array.hasValue(R.styleable.MaskingEditText_valueType)) {
        builder.valueType(MaskingValueType.fromAttrValue(
            array.getInt(R.styleable.MaskingEditText_valueType, 0)));
      }
      String suffix = array.getString(R.styleable.MaskingEditText_suffix);
      if (suffix != null) {
        builder.suffix(suffix);
      }
      if (array.hasValue(R.styleable.MaskingEditText_offsetDays)) {
        builder.offsetDays(array.getInt(R.styleable.MaskingEditText_offsetDays, -180));
      }
      String tokenPrefix = array.getString(R.styleable.MaskingEditText_tokenPrefix);
      if (tokenPrefix != null) {
        builder.tokenPrefix(tokenPrefix);
      }
      if (array.hasValue(R.styleable.MaskingEditText_maskingMode)) {
        int modeValue = array.getInt(R.styleable.MaskingEditText_maskingMode, 0);
        builder.mode(modeValue == 1
            ? MaskingEditTextController.Mode.ON_TEXT_CHANGED
            : MaskingEditTextController.Mode.ON_FOCUS_LOST);
      }
      controller = builder.build();
    } finally {
      array.recycle();
    }
    if (config.getMaskingType() != null && config.isToggleEnabled()) {
      MaskingToggleHelper.install(this, text -> {
        if (controller != null) controller.setEnabled(false);
        isApplying = true;
        setText(text);
        isApplying = false;
        if (controller != null) controller.setEnabled(true);
      });
    }
  }

  public MaskingEditTextController getController() {
    return controller;
  }

  @Override
  public void setText(CharSequence text, BufferType type) {
    if (isApplying) {
      super.setText(text, type);
      return;
    }
    if (config == null) {
      super.setText(text, type);
      return;
    }
    MaskingResult result = config.apply(text != null ? text.toString() : "");
    if (result == null) {
      super.setText(text, type);
      return;
    }
    isApplying = true;
    setTag(R.id.masking_original_text, text != null ? text.toString() : "");
    setTag(R.id.masking_masked_text, result.getMasked());
    super.setText(result.getMasked(), type);
    isApplying = false;
  }

  public void setMaskingType(MaskingType maskingType) {
    config.setMaskingType(maskingType);
  }

  public void setVisibleChars(Integer visibleChars) {
    config.setVisibleChars(visibleChars);
  }

  public void setMaskChar(String maskChar) {
    config.setMaskChar(maskChar);
  }

  public void setValueType(MaskingValueType valueType) {
    config.setValueType(valueType);
  }

  public void setSuffix(String suffix) {
    config.setSuffix(suffix);
  }

  public void setOffsetDays(Integer offsetDays) {
    config.setOffsetDays(offsetDays);
  }

  public void setTokenPrefix(String tokenPrefix) {
    config.setTokenPrefix(tokenPrefix);
  }

  public void setSwappedWith(String swappedWith) {
    config.setSwappedWith(swappedWith);
  }

  public void setAlgorithm(String algorithm) {
    config.setAlgorithm(algorithm);
  }
}
