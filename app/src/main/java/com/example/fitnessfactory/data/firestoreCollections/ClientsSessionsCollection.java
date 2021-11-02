package com.example.fitnessfactory.data.firestoreCollections;

import com.example.fitnessfactory.data.FirestoreCollections;

public class ClientsSessionsCollection {

    public static String getRoot(String clientId) {
        return ClientsCollection.getRoot() +
                "/" +
                clientId +
                "/" +
                FirestoreCollections.SESSIONS_COLLECTION;
    }
}
