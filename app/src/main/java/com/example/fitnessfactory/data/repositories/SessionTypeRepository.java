package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.data.firestoreCollections.SessionTypesCollection;

public class SessionTypeRepository extends BaseRepository {

    @Override
    public String getRoot() {
        return SessionTypesCollection.getRoot();
    }
}
