package com.example.fitnessfactory.data.repositories.access;

import com.example.fitnessfactory.data.firestoreCollections.ClientsAccessCollection;

public class ClientsAccessRepository extends PersonnelAccessRepository {

    @Override
    protected String getRoot() {
        return ClientsAccessCollection.getRoot();
    }

    @Override
    protected ClientsAccessRepository.QueryBuilder newQuery() {
        return new ClientsAccessRepository().new QueryBuilder();
    }
}
