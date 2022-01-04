package com.example.fitnessfactory.ui.components.filters.ValueCheckers;

public class NoConditionChecker implements ValueChecker {

    public static NoConditionChecker getInstance() {
        return new NoConditionChecker();
    }

    @Override
    public boolean isValueValid(String value) {
        return true;
    }

    @Override
    public String getErrorMessage() {
        return "";
    }
}
