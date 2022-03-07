package com.example.fitnessfactory.data;

import androidx.annotation.NonNull;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.ArrayList;
import java.util.List;

public enum LanguageLocales {
    system, ru, en;

    public static final String russianLocaleCode = "ru";
    public static final String englishLocaleCode = "en";
    private static final String ukranianLocaleCode = "uk";
    private static final String belarussianLocaleCode = "be";

    public static boolean isRussian(String localeCode) {
        return localeCode.equals(russianLocaleCode)
                || localeCode.equals(ukranianLocaleCode)
                || localeCode.equals(belarussianLocaleCode);
    }

    public static List<LanguageLocales> toList() {
        List<LanguageLocales> languageLocales = new ArrayList<>();

        languageLocales.add(system);
        languageLocales.add(ru);
        languageLocales.add(en);

        return languageLocales;
    }

    public String getLocaleCode() {
        switch (this) {
            case system:
                return LocaleHelper.getLocaleLanguage();
            case ru:
                return russianLocaleCode;
            case en:
                return englishLocaleCode;
        }

        return "";
    }

    @NonNull
    @Override
    public String toString() {
        switch (this) {
            case system:
                return ResUtils.getString(R.string.caption_system);
            case ru:
                return ResUtils.getString(R.string.caption_russian_language);
            case en:
                return ResUtils.getString(R.string.caption_english_language);
        }

        return "";
    }
}
