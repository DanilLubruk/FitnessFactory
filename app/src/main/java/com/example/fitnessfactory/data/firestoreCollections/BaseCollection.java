package com.example.fitnessfactory.data.firestoreCollections;
import com.example.fitnessfactory.data.AppPrefs;

public class BaseCollection {

    public static String getRoot() {
        return AppPrefs.gymOwnerId().getValue();
    }
}
