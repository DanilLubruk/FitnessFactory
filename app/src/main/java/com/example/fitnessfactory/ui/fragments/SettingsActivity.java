package com.example.fitnessfactory.ui.fragments;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.fitnessfactory.R;

public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preferences);
    }
}
