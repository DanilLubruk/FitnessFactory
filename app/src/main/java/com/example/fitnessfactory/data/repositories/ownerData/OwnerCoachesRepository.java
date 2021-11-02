package com.example.fitnessfactory.data.repositories.ownerData;

import com.example.fitnessfactory.data.firestoreCollections.OwnerCoachesCollection;
import com.example.fitnessfactory.data.models.Personnel;

import io.reactivex.Single;

public class OwnerCoachesRepository extends OwnerPersonnelRepository {

    @Override
    protected String getRoot() {
        return OwnerCoachesCollection.getRoot();
    }

    public Single<String> getCoachIdByEmailAsync(String coachEmail) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getCoachIdByEmail(coachEmail));
            }
        });
    }

    private String getCoachIdByEmail(String coachEmail) throws Exception {
        Personnel personnel =
                getUniqueUserEntity(
                        newQuery().whereUserEmailEquals(coachEmail).build(),
                        Personnel.class);

        return personnel.getId();
    }

    @Override
    protected OwnerCoachesRepository.QueryBuilder newQuery() {
        return new OwnerCoachesRepository().new QueryBuilder();
    }
}
