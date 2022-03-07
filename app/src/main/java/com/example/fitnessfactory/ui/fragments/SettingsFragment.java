package com.example.fitnessfactory.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.LanguageLocales;
import com.example.fitnessfactory.data.managers.RxManager;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.ui.activities.BaseActivity;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;

public class SettingsFragment extends PreferenceFragmentCompat {

    private BaseActivity activity;

    public BaseActivity getBaseActivity() {
        return activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (BaseActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    private final RxManager rxManager = new RxManager();

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey);
    }

    @Override
    public boolean onPreferenceTreeClick(@NonNull Preference preference) {
        switch (preference.getKey()) {
            case AppPrefs.languageLocale:
                rxManager.subscribeInMainThread(
                        DialogUtils.showOptionPickerDialog(
                                getBaseActivity(),
                                ResUtils.getString(R.string.caption_select_language),
                                LanguageLocales.toList()),
                        new SingleData<>(locale -> {
                            preference.setSummary(locale.toString());
                            AppPrefs.languageLocale().setValue(locale.getLocaleCode());
                        },
                                throwable -> {
                                    throwable.printStackTrace();
                                    GuiUtils.showMessage(throwable.getLocalizedMessage());
                                }));
                break;
        }
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rxManager.unsubscribe();
    }
}
