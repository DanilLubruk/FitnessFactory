package com.example.fitnessfactory.data;
import com.tiromansev.prefswrapper.typedprefs.BooleanPreference;
import com.tiromansev.prefswrapper.typedprefs.IntegerPreference;
import com.tiromansev.prefswrapper.typedprefs.StringPreference;

public class AppPrefs {

    public static StringPreference gymOwnerId() {
        return StringPreference
                .builder("gym_owner_id")
                .setDefaultValue("")
                .build();
    }

    public static StringPreference organisationName() {
        return StringPreference
                .builder("organisation_name_pref")
                .setDefaultValue("")
                .build();
    }

    public static IntegerPreference currentUserType() {
        return IntegerPreference
                .builder("is_current_user_owner")
                .setDefaultValue(CurrentUserType.CURRENT_USER_OWNER)
                .build();
    }

    public static BooleanPreference askForOrganisationName() {
        return BooleanPreference
                .builder("do_ask_for_org_name")
                .setDefaultValue(false)
                .build();
    }

    public static BooleanPreference askToSendAdminEmailInvite() {
        return BooleanPreference
                .builder("ask_for_sending_admin_email_invite")
                .setDefaultValue(true)
                .build();
    }

    public static BooleanPreference askToSendCoachEmailInvite() {
        return BooleanPreference
                .builder("ask_for_sending_coach_email_invite")
                .setDefaultValue(true)
                .build();
    }

    public static StringPreference currencySign() {
        return StringPreference
                .builder("currency_sign_pref")
                .setDefaultValue("₽")
                .build();
    }
}
