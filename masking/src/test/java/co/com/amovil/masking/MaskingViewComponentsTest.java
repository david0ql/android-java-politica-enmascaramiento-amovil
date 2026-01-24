package co.com.amovil.masking;

import android.content.Context;
import android.widget.TextView;

import org.robolectric.RuntimeEnvironment;


import co.com.amovil.masking.view.MaskingAutoCompleteTextView;
import co.com.amovil.masking.view.MaskingButton;
import co.com.amovil.masking.view.MaskingCheckBox;
import co.com.amovil.masking.view.MaskingCheckedTextView;
import co.com.amovil.masking.view.MaskingMultiAutoCompleteTextView;
import co.com.amovil.masking.view.MaskingRadioButton;
import co.com.amovil.masking.view.MaskingTextView;
import co.com.amovil.masking.view.MaskingToggleButton;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class MaskingViewComponentsTest {
  @Test
  public void maskingTextViewAppliesConfig() {
    MaskingTextView view = new MaskingTextView(getContext());
    configurePartialMask(view);
    view.setText("1023456789");
    assertMasked(view, "******6789", "1023456789");
  }

  @Test
  public void maskingButtonAppliesConfig() {
    MaskingButton view = new MaskingButton(getContext());
    configurePartialMask(view);
    view.setText("1023456789");
    assertMasked(view, "******6789", "1023456789");
  }

  @Test
  public void maskingCheckBoxAppliesConfig() {
    MaskingCheckBox view = new MaskingCheckBox(getContext());
    configurePartialMask(view);
    view.setText("1023456789");
    assertMasked(view, "******6789", "1023456789");
  }

  @Test
  public void maskingRadioButtonAppliesConfig() {
    MaskingRadioButton view = new MaskingRadioButton(getContext());
    configurePartialMask(view);
    view.setText("1023456789");
    assertMasked(view, "******6789", "1023456789");
  }

  @Test
  public void maskingToggleButtonAppliesConfig() {
    MaskingToggleButton view = new MaskingToggleButton(getContext());
    configurePartialMask(view);
    view.setText("1023456789");
    assertMasked(view, "******6789", "1023456789");
  }

  @Test
  public void maskingCheckedTextViewAppliesConfig() {
    MaskingCheckedTextView view = new MaskingCheckedTextView(getContext());
    configurePartialMask(view);
    view.setText("1023456789");
    assertMasked(view, "******6789", "1023456789");
  }

  @Test
  public void maskingAutoCompleteTextViewAppliesConfig() {
    MaskingAutoCompleteTextView view = new MaskingAutoCompleteTextView(getContext());
    configurePartialMask(view);
    view.setText("1023456789");
    assertMasked(view, "******6789", "1023456789");
  }

  @Test
  public void maskingMultiAutoCompleteTextViewAppliesConfig() {
    MaskingMultiAutoCompleteTextView view = new MaskingMultiAutoCompleteTextView(getContext());
    configurePartialMask(view);
    view.setText("1023456789");
    assertMasked(view, "******6789", "1023456789");
  }

  private Context getContext() {
    return RuntimeEnvironment.getApplication();
  }

  private void configurePartialMask(MaskingTextView view) {
    view.setMaskingType(MaskingType.ENMASCARAMIENTO_PARCIAL);
    view.setVisibleChars(4);
    view.setMaskChar("*");
  }

  private void configurePartialMask(MaskingButton view) {
    view.setMaskingType(MaskingType.ENMASCARAMIENTO_PARCIAL);
    view.setVisibleChars(4);
    view.setMaskChar("*");
  }

  private void configurePartialMask(MaskingCheckBox view) {
    view.setMaskingType(MaskingType.ENMASCARAMIENTO_PARCIAL);
    view.setVisibleChars(4);
    view.setMaskChar("*");
  }

  private void configurePartialMask(MaskingRadioButton view) {
    view.setMaskingType(MaskingType.ENMASCARAMIENTO_PARCIAL);
    view.setVisibleChars(4);
    view.setMaskChar("*");
  }

  private void configurePartialMask(MaskingToggleButton view) {
    view.setMaskingType(MaskingType.ENMASCARAMIENTO_PARCIAL);
    view.setVisibleChars(4);
    view.setMaskChar("*");
  }

  private void configurePartialMask(MaskingCheckedTextView view) {
    view.setMaskingType(MaskingType.ENMASCARAMIENTO_PARCIAL);
    view.setVisibleChars(4);
    view.setMaskChar("*");
  }

  private void configurePartialMask(MaskingAutoCompleteTextView view) {
    view.setMaskingType(MaskingType.ENMASCARAMIENTO_PARCIAL);
    view.setVisibleChars(4);
    view.setMaskChar("*");
  }

  private void configurePartialMask(MaskingMultiAutoCompleteTextView view) {
    view.setMaskingType(MaskingType.ENMASCARAMIENTO_PARCIAL);
    view.setVisibleChars(4);
    view.setMaskChar("*");
  }

  private void assertMasked(TextView view, String masked, String original) {
    assertEquals(masked, view.getText().toString());
    assertEquals(original, view.getTag(R.id.masking_original_text));
    assertEquals(masked, view.getTag(R.id.masking_masked_text));
  }
}
