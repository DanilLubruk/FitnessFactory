package com.example.fitnessfactory.data.repositories.ownerData;

import com.example.fitnessfactory.data.firestoreCollections.OwnerCoachesCollection;

public class OwnerCoachesRepository extends OwnerPersonnelRepository {

    @Override
    protected String getRoot() {
        return OwnerCoachesCollection.getRoot();
    }

    @Override
    protected OwnerCoachesRepository.QueryBuilder newQuery() {
        return new OwnerCoachesRepository().new QueryBuilder();
    }


}
