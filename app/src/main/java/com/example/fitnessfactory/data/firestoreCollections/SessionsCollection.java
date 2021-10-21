package com.example.fitnessfactory.data.firestoreCollections;

import com.example.fitnessfactory.data.FirestoreCollections;

public class SessionsCollection {

    public static String getRoot() {
        return BaseCollection.getRoot() +
                "/" +
                FirestoreCollections.SESSIONS_COLLECTION +
                "/" +
                FirestoreCollections.SESSIONS_COLLECTION;
    }
}
