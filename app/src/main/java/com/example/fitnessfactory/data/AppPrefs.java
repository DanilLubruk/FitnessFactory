package com.example.fitnessfactory.data;

import androidx.annotation.IntegerRes;

import com.tiromansev.prefswrapper.typedprefs.IntegerPreference;
import com.tiromansev.prefswrapper.typedprefs.StringPreference;

public class AppPrefs {

    public static StringPreference gymOwnerId() {
        return StringPreference
                .builder("gym_owner_id")
                .setDefaultValue("")
                .build();
    }

    public static IntegerPreference currentUserType() {
        return IntegerPreference
                .builder("is_current_user_owner")
                .build();
    }
}
