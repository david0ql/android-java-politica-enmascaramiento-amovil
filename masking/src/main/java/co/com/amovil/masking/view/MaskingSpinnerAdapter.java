package co.com.amovil.masking.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import co.com.amovil.masking.MaskingEngine;
import co.com.amovil.masking.MaskingRequest;
import co.com.amovil.masking.MaskingResult;
import co.com.amovil.masking.MaskingType;
import co.com.amovil.masking.R;

import java.util.ArrayList;
import java.util.List;

public class MaskingSpinnerAdapter extends ArrayAdapter<MaskingRequest> {
  private final int itemLayout;
  private final int dropdownLayout;

  public MaskingSpinnerAdapter(Context context, List<MaskingRequest> items) {
    this(context, items, android.R.layout.simple_spinner_item,
        android.R.layout.simple_spinner_dropdown_item);
  }

  public MaskingSpinnerAdapter(Context context, List<MaskingRequest> items,
                               int itemLayout, int dropdownLayout) {
    super(context, itemLayout, items);
    this.itemLayout = itemLayout;
    this.dropdownLayout = dropdownLayout;
    setDropDownViewResource(dropdownLayout);
  }

  public static MaskingSpinnerAdapter fromStrings(Context context, List<String> values,
                                                  MaskingType type) {
    List<MaskingRequest> items = new ArrayList<>();
    if (values != null) {
      for (String value : values) {
        items.add(MaskingRequest.builder(type, value).build());
      }
    }
    return new MaskingSpinnerAdapter(context, items);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = super.getView(position, convertView, parent);
    bindView(view, getItem(position));
    return view;
  }

  @Override
  public View getDropDownView(int position, View convertView, ViewGroup parent) {
    View view = super.getDropDownView(position, convertView, parent);
    bindView(view, getItem(position));
    return view;
  }

  private void bindView(View view, MaskingRequest request) {
    if (!(view instanceof TextView) || request == null) {
      return;
    }
    MaskingResult result = MaskingEngine.mask(request);
    TextView textView = (TextView) view;
    textView.setText(result.getMasked());
    textView.setTag(R.id.masking_original_text, request.getOriginal());
    textView.setTag(R.id.masking_masked_text, result.getMasked());
  }
}
