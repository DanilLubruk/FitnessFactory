package com.example.fitnessfactory.ui.components.filters.ValueCheckers;

public interface ValueChecker {

    boolean isValueValid(String value);

    String getErrorMessage();
}
