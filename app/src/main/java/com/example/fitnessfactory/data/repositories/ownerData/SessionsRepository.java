package com.example.fitnessfactory.data.repositories.ownerData;

import com.example.fitnessfactory.data.firestoreCollections.SessionsCollection;
import com.example.fitnessfactory.data.repositories.BaseRepository;

public class SessionsRepository extends BaseRepository {

    @Override
    protected String getRoot() {
        return SessionsCollection.getRoot();
    }


}
