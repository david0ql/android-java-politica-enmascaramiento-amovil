package co.com.amovil.masking.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import co.com.amovil.masking.MaskingEngine;
import co.com.amovil.masking.MaskingRequest;
import co.com.amovil.masking.MaskingResult;
import co.com.amovil.masking.MaskingType;
import co.com.amovil.masking.R;

public final class MaskingEditTextController {
  public enum Mode {
    ON_FOCUS_LOST,
    ON_TEXT_CHANGED
  }

  private final EditText editText;
  private final MaskingType type;
  private final Integer visibleChars;
  private final String maskChar;
  private final Integer offsetDays;
  private final String tokenPrefix;
  private final Mode mode;
  private boolean isApplying;
  private View.OnFocusChangeListener previousFocusListener;

  private MaskingEditTextController(Builder builder) {
    this.editText = builder.editText;
    this.type = builder.type;
    this.visibleChars = builder.visibleChars;
    this.maskChar = builder.maskChar;
    this.offsetDays = builder.offsetDays;
    this.tokenPrefix = builder.tokenPrefix;
    this.mode = builder.mode;
    attach();
  }

  public static Builder attach(EditText editText, MaskingType type) {
    return new Builder(editText, type);
  }

  public static String getOriginalText(EditText editText) {
    if (editText == null) {
      return null;
    }
    Object stored = editText.getTag(R.id.masking_original_text);
    if (stored instanceof String) {
      return (String) stored;
    }
    return editText.getText() != null ? editText.getText().toString() : null;
  }

  private void attach() {
    if (mode == Mode.ON_TEXT_CHANGED) {
      editText.addTextChangedListener(new MaskingTextWatcher());
      return;
    }
    previousFocusListener = editText.getOnFocusChangeListener();
    editText.setOnFocusChangeListener((view, hasFocus) -> {
      if (!hasFocus) {
        applyMasking();
      }
      if (previousFocusListener != null) {
        previousFocusListener.onFocusChange(view, hasFocus);
      }
    });
  }

  private void applyMasking() {
    if (isApplying) {
      return;
    }
    String original = editText.getText() != null ? editText.getText().toString() : "";
    if (original.isEmpty()) {
      return;
    }
    MaskingRequest.Builder builder = MaskingRequest.builder(type, original);
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
    MaskingResult result = MaskingEngine.mask(builder.build());
    isApplying = true;
    editText.setTag(R.id.masking_original_text, original);
    editText.setTag(R.id.masking_masked_text, result.getMasked());
    editText.setText(result.getMasked());
    editText.setSelection(editText.getText().length());
    isApplying = false;
  }

  public static final class Builder {
    private final EditText editText;
    private final MaskingType type;
    private Integer visibleChars;
    private String maskChar;
    private Integer offsetDays;
    private String tokenPrefix;
    private Mode mode = Mode.ON_FOCUS_LOST;

    private Builder(EditText editText, MaskingType type) {
      if (editText == null) {
        throw new IllegalArgumentException("EditText is required");
      }
      if (type == null) {
        throw new IllegalArgumentException("Masking type is required");
      }
      this.editText = editText;
      this.type = type;
    }

    public Builder visibleChars(int visibleChars) {
      this.visibleChars = visibleChars;
      return this;
    }

    public Builder maskChar(String maskChar) {
      this.maskChar = maskChar;
      return this;
    }

    public Builder offsetDays(int offsetDays) {
      this.offsetDays = offsetDays;
      return this;
    }

    public Builder tokenPrefix(String tokenPrefix) {
      this.tokenPrefix = tokenPrefix;
      return this;
    }

    public Builder mode(Mode mode) {
      if (mode != null) {
        this.mode = mode;
      }
      return this;
    }

    public MaskingEditTextController build() {
      return new MaskingEditTextController(this);
    }
  }

  private final class MaskingTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
      if (isApplying) {
        return;
      }
      applyMasking();
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
  }
}
