package com.example.fitnessfactory.data;

import java.util.Locale;

public class LocaleHelper {

    public static String getLocaleLanguage() {
        String currentLocaleLanguage = Locale.getDefault().getLanguage();

        String localeLanguage = LanguageLocales.englishLocaleCode;
        if (LanguageLocales.isRussian(currentLocaleLanguage)) {
            localeLanguage = LanguageLocales.russianLocaleCode;
        }

        return localeLanguage;
    }
}
