package com.example.fitnessfactory.data.firestoreCollections;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.FirestoreCollections;

public class BaseCollection {

    public static String getRoot() {
        return FirestoreCollections.OWNERS_COLLECTION +
                "/" +
                AppPrefs.gymOwnerId().getValue() +
                "/" +
                AppPrefs.gymOwnerId().getValue();
    }
}
