package com.example.fitnessfactory.ui.components.filters.ValueCheckers;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.utils.ResUtils;

public class NotEmptyNameValueChecker implements ValueChecker {

    public static NotEmptyNameValueChecker getInstance() {
        return new NotEmptyNameValueChecker();
    }

    @Override
    public boolean isValueValid(String name) {
        return name != null && !name.isEmpty();
    }

    @Override
    public String getErrorMessage() {
        return ResUtils.getString(R.string.message_error_name_invalid);
    }
}
