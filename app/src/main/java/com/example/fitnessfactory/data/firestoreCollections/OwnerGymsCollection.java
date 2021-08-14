package com.example.fitnessfactory.data.firestoreCollections;

import com.example.fitnessfactory.data.FirestoreCollections;
import com.example.fitnessfactory.data.repositories.BaseRepository;

public class OwnerGymsCollection {

    public static String getRoot() {
        return BaseCollection.getRoot() +
                "/" +
                FirestoreCollections.GYMS_COLLECTION +
                "/" +
                FirestoreCollections.GYMS_COLLECTION;
    }
}
