package com.example.fitnessfactory.data.firestoreCollections;

import com.example.fitnessfactory.data.FirestoreCollections;

public class CoachAccessCollection {

    public static String getRoot() {
        return FirestoreCollections.APP_DATA_COLLECTION +
                "/" +
                FirestoreCollections.COACHES_ACCESS_COLLECTION +
                "/" +
                FirestoreCollections.COACHES_ACCESS_COLLECTION;
    }
}
