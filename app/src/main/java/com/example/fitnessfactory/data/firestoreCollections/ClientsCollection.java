package com.example.fitnessfactory.data.firestoreCollections;

import com.example.fitnessfactory.data.FirestoreCollections;

public class ClientsCollection {

    public static String getRoot() {
        return BaseCollection.getRoot() +
                "/" +
                FirestoreCollections.CLIENTS_COLLECTION +
                "/" +
                FirestoreCollections.CLIENTS_COLLECTION;
    }
}
