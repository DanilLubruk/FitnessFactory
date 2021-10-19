package com.example.fitnessfactory.data.repositories.ownerData;

import com.example.fitnessfactory.data.firestoreCollections.OwnerAdminsCollection;

public class OwnerAdminsRepository extends OwnerPersonnelRepository {

    @Override
    public String getRoot() {
        return OwnerAdminsCollection.getRoot();
    }
}
