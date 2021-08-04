package com.example.fitnessfactory.data;

public class CurrentUserType {

    public static final int CURRENT_USER_OWNER = 1;
    public static final int CURRENT_USER_STAFF = 2;

    public static boolean isOwner() {
        return AppPrefs.currentUserType().getValue() == CurrentUserType.CURRENT_USER_OWNER;
    }

    public static boolean isStaff() {
        return AppPrefs.currentUserType().getValue() == CurrentUserType.CURRENT_USER_STAFF;
    }
}
