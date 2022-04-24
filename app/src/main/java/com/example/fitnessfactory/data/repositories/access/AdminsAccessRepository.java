package com.example.fitnessfactory.data.repositories.access;

import com.example.fitnessfactory.data.firestoreCollections.AdminAccessCollection;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.PersonnelAccessEntry;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class AdminsAccessRepository extends PersonnelAccessRepository {

    @Override
    protected String getRoot() {
        return AdminAccessCollection.getRoot();
    }

    public Single<List<String>> getOwnersByInvitedEmail(AppUser user) {
        return SingleCreate(emitter -> {
            List<String> ownerIds = new ArrayList<>();
            ownerIds.add(user.getId());
            ownerIds.addAll(getOwnerIds(user.getId()));

            if (!emitter.isDisposed()) {
                emitter.onSuccess(ownerIds);
            }
        });
    }

    private List<String> getOwnerIds(String userId) throws Exception {
        List<String> ownerIds = new ArrayList<>();

        for (PersonnelAccessEntry adminAccessEntry : getAdminAccessEntriesByUserEmail(userId)) {
            ownerIds.add(adminAccessEntry.getOwnerId());
        }

        return ownerIds;
    }

    private List<PersonnelAccessEntry> getAdminAccessEntriesByUserEmail(String userId) throws Exception {
        return Tasks.await(
                newQuery()
                        .whereUserIdEquals(userId)
                        .build()
                        .get())
                .toObjects(PersonnelAccessEntry.class);
    }

    @Override
    protected AdminsAccessRepository.QueryBuilder newQuery() {
        return new AdminsAccessRepository(). new QueryBuilder();
    }
}
