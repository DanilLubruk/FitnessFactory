package com.example.fitnessfactory.data.firestoreCollections;

import com.example.fitnessfactory.data.FirestoreCollections;

public class CoachAccessCollection {

    public static String getRoot() {
        return BaseCollection.getRoot() +
                "/" +
                FirestoreCollections.COACHES +
                "/" +
                FirestoreCollections.COACHES;
    }
}
