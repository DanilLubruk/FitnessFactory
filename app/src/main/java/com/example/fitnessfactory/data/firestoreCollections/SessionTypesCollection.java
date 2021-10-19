package com.example.fitnessfactory.data.firestoreCollections;

import com.example.fitnessfactory.data.FirestoreCollections;

public class SessionTypesCollection {

    public static String getRoot() {
        return BaseCollection.getRoot() +
                "/" +
                FirestoreCollections.SESSION_TYPES_COLLECTION +
                "/" +
                FirestoreCollections.SESSION_TYPES_COLLECTION;
    }
}
