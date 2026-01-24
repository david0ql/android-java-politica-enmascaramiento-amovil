package co.com.amovil.masking.view;

import android.view.View;
import android.widget.TextView;

import co.com.amovil.masking.MaskingEngine;
import co.com.amovil.masking.MaskingRequest;
import co.com.amovil.masking.MaskingResult;
import co.com.amovil.masking.R;

public final class MaskingViewApplier {
  private MaskingViewApplier() {
  }

  public static MaskingResult apply(View view, MaskingRequest request) {
    MaskingResult result = MaskingEngine.mask(request);
    applyMaskedValue(view, result.getMasked());
    if (view != null) {
      view.setTag(R.id.masking_original_text, request.getOriginal());
    }
    return result;
  }

  public static void applyMaskedValue(View view, String maskedValue) {
    if (view == null) {
      return;
    }
    if (view instanceof TextView) {
      ((TextView) view).setText(maskedValue);
    } else {
      view.setContentDescription(maskedValue);
      view.setTag(R.id.masking_masked_text, maskedValue);
    }
  }
}
