package com.example.fitnessfactory.data.firestoreCollections;

import com.example.fitnessfactory.data.FirestoreCollections;

public class ClientsAccessCollection {

    public static String getRoot() {
        return FirestoreCollections.APP_DATA_COLLECTION +
                "/" +
                FirestoreCollections.CLIENTS_ACCESS_COLLECTION +
                "/" +
                FirestoreCollections.CLIENTS_ACCESS_COLLECTION;
    }
}
