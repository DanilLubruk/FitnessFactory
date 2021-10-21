package com.example.fitnessfactory.data.repositories.access;

import com.example.fitnessfactory.data.firestoreCollections.AdminAccessCollection;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.PersonnelAccessEntry;
import com.example.fitnessfactory.data.repositories.BaseRepository;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
            ownerIds.addAll(getOwnerIds(user.getEmail()));

            if (!emitter.isDisposed()) {
                emitter.onSuccess(ownerIds);
            }
        });
    }

    private List<String> getOwnerIds(String userEmail) throws Exception {
        List<String> ownerIds = new ArrayList<>();

        for (PersonnelAccessEntry adminAccessEntry : getAdminAccessEntriesByUserEmail(userEmail)) {
            ownerIds.add(adminAccessEntry.getOwnerId());
        }

        return ownerIds;
    }

    private List<PersonnelAccessEntry> getAdminAccessEntriesByUserEmail(String userEmail) throws Exception {
        return Tasks.await(
                newQuery()
                        .whereUserEmailEquals(userEmail)
                        .build()
                        .get())
                .toObjects(PersonnelAccessEntry.class);
    }
}
