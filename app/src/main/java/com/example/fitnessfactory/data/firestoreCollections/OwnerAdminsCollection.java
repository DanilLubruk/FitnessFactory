package com.example.fitnessfactory.data.firestoreCollections;

import com.example.fitnessfactory.data.FirestoreCollections;

public class OwnerAdminsCollection {

    public static String getRoot() {
        return BaseCollection.getRoot() +
                "/" +
                FirestoreCollections.ADMINS +
                "/" +
                FirestoreCollections.ADMINS;
    }
}
