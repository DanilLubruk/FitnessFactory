package com.example.fitnessfactory.ui.components.filters;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;

public class InputFilterMinMax implements InputFilter {

    private int min, max;
    private EditText edtValue;

    public InputFilterMinMax(EditText edtValue, int min, int max) {
        this.min = min;
        this.max = max;
        this.edtValue = edtValue;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            // Removes string that is to be replaced from destination
            // and adds the new string in.
            String newVal = dest.subSequence(0, dstart)
                    // Note that below "toString()" is the only required:
                    + source.subSequence(start, end).toString()
                    + dest.subSequence(dend, dest.length());
            int input = Integer.parseInt(newVal);
            if (isInRange(min, max, input)) {
                return null;
            }
        } catch (NumberFormatException nfe) {
            GuiUtils.showMessage(nfe.getLocalizedMessage());
        }
        if (edtValue != null) {
            edtValue.setError(
                    String.format(
                            ResUtils.getString(R.string.message_input_invalid_value),
                            min, max));
        }
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
