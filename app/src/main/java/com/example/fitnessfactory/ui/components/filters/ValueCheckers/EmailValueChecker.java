package com.example.fitnessfactory.ui.components.filters.ValueCheckers;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValueChecker implements ValueChecker {

    private static final String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public static EmailValueChecker getInstance() {
        return new EmailValueChecker();
    }

    @Override
    public boolean isValueValid(String email) {
        Pattern pattern = Pattern.compile(emailRegex);

        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    @Override
    public String getErrorMessage() {
        return ResUtils.getString(R.string.message_error_email_invalid);
    }
}
