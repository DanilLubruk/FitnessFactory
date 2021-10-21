package com.example.fitnessfactory.data.repositories.ownerData;

import com.example.fitnessfactory.data.firestoreCollections.OwnerAdminsCollection;

public class OwnerAdminsRepository extends OwnerPersonnelRepository {

    @Override
    protected String getRoot() {
        return OwnerAdminsCollection.getRoot();
    }

    @Override
    protected OwnerAdminsRepository.QueryBuilder newQuery() {
        return new OwnerAdminsRepository().new QueryBuilder();
    }
}
