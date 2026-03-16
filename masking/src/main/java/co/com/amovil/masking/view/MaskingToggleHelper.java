package co.com.amovil.masking.view;

import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import co.com.amovil.masking.R;

final class MaskingToggleHelper {

  interface TextSetter {
    void set(String text);
  }

  private final TextView view;
  private final TextSetter setter;
  private boolean revealed = false;

  private MaskingToggleHelper(TextView view, TextSetter setter) {
    this.view = view;
    this.setter = setter;
  }

  static void install(TextView view, TextSetter setter) {
    new MaskingToggleHelper(view, setter).attach();
  }

  private void attach() {
    updateIcon();
    view.setOnTouchListener((v, event) -> {
      if (event.getAction() != MotionEvent.ACTION_UP) {
        return false;
      }
      Drawable end = getEndDrawable();
      if (end == null) {
        return false;
      }
      int drawableStartX = view.getWidth() - view.getPaddingEnd() - end.getIntrinsicWidth();
      if (event.getX() >= drawableStartX) {
        toggle();
        view.performClick();
        return true;
      }
      return false;
    });
  }

  private void toggle() {
    revealed = !revealed;
    String text = revealed
        ? (String) view.getTag(R.id.masking_original_text)
        : (String) view.getTag(R.id.masking_masked_text);
    if (text != null) {
      setter.set(text);
    }
    updateIcon();
  }

  private void updateIcon() {
    int res = revealed ? R.drawable.ic_masking_eye_off : R.drawable.ic_masking_eye_on;
    Drawable icon = ContextCompat.getDrawable(view.getContext(), res);
    int dp8 = (int) (8 * view.getContext().getResources().getDisplayMetrics().density);
    Drawable[] d = view.getCompoundDrawablesRelative();
    view.setCompoundDrawablesRelativeWithIntrinsicBounds(d[0], d[1], icon, d[3]);
    view.setCompoundDrawablePadding(dp8);
  }

  private Drawable getEndDrawable() {
    return view.getCompoundDrawablesRelative()[2];
  }
}
