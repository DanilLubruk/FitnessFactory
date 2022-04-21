package com.example.fitnessfactory.ui.components.filters;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.ui.components.filters.ValueCheckers.EmailValueChecker;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;

public class InputFilterEmail implements InputFilter {
    private EditText edtEmail;

    public InputFilterEmail(EditText edtEmail) {
        this.edtEmail = edtEmail;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            // Removes string that is to be replaced from destination
            // and adds the new string in.
            String newVal = (dest.subSequence(0, dstart)
                    // Note that below "toString()" is the only required:
                    + source.subSequence(start, end).toString()
                    + dest.subSequence(dend, dest.length())).trim();
            if (EmailValueChecker.getInstance().isValueValid(newVal)) {
                return null;
            }
        } catch (NumberFormatException nfe) {
            GuiUtils.showMessage(nfe.getLocalizedMessage());
        }
        if (edtEmail != null) {
            edtEmail.setError(
                    String.format(
                            ResUtils.getString(R.string.message_error_email_invalid)));
        }
        return null;
    }
}
