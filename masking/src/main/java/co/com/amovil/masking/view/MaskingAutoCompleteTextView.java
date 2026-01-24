package co.com.amovil.masking.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import co.com.amovil.masking.MaskingResult;
import co.com.amovil.masking.MaskingType;
import co.com.amovil.masking.R;

public class MaskingAutoCompleteTextView extends AppCompatAutoCompleteTextView {
  private final MaskingTextConfig config = new MaskingTextConfig();
  private boolean isApplying;

  public MaskingAutoCompleteTextView(Context context) {
    super(context);
    init(null);
  }

  public MaskingAutoCompleteTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public MaskingAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
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

  private void init(AttributeSet attrs) {
    config.loadFromAttrs(getContext(), attrs);
  }
}
