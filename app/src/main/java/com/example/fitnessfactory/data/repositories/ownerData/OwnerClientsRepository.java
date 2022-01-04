package com.example.fitnessfactory.data.repositories.ownerData;

import com.example.fitnessfactory.data.firestoreCollections.ClientsCollection;

public class OwnerClientsRepository extends OwnerPersonnelRepository {

    @Override
    protected String getRoot() {
        return ClientsCollection.getRoot();
    }

    @Override
    protected OwnerClientsRepository.QueryBuilder newQuery() {
        return new OwnerClientsRepository().new QueryBuilder();
    }
}
