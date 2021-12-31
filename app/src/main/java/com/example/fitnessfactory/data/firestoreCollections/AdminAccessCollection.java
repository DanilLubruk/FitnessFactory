package com.example.fitnessfactory.data.firestoreCollections;

import com.example.fitnessfactory.data.FirestoreCollections;

public class AdminAccessCollection {

    public static String getRoot() {
        return FirestoreCollections.APP_DATA_COLLECTION +
                "/" +
                FirestoreCollections.ADMINS_ACCESS_COLLECTION +
                "/" +
                FirestoreCollections.ADMINS_ACCESS_COLLECTION;
    }
}
