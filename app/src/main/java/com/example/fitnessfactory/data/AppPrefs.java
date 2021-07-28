package com.example.fitnessfactory.data;

import com.tiromansev.prefswrapper.typedprefs.StringPreference;

public class AppPrefs {

    public static StringPreference gymOwnerId() {
        return StringPreference
                .builder("gym_owner_id")
                .setDefaultValue("")
                .build();
    }
}
