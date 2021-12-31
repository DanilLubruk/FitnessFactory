package com.example.fitnessfactory.data.firestoreCollections;

import com.example.fitnessfactory.data.FirestoreCollections;

public class UsersCollection {

    public static String getRoot() {
        return FirestoreCollections.APP_DATA_COLLECTION +
                "/" +
                FirestoreCollections.USERS_COLLECTION +
                "/" +
                FirestoreCollections.USERS_COLLECTION;
    }
}
