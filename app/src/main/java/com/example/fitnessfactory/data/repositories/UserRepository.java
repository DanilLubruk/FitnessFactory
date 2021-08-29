package com.example.fitnessfactory.data.repositories;
import com.example.fitnessfactory.data.firestoreCollections.UsersCollection;
import com.example.fitnessfactory.data.models.AppUser;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;

import static com.example.fitnessfactory.data.models.AppUser.EMAIL_FILED;

public class UserRepository extends BaseRepository {

    @Override
    public String getRoot() {
        return UsersCollection.getRoot();
    }

    public Single<AppUser> registerUser(String email, String userName) {
        return SingleCreate(emitter -> {
            DocumentReference documentReference = getCollection().document();
            AppUser appUser = new AppUser();
            appUser.setId(documentReference.getId());
            appUser.setName(userName);
            appUser.setEmail(email);
            Tasks.await(documentReference.set(appUser));

            if (!emitter.isDisposed()) {
                emitter.onSuccess(appUser);
            }
        });
    }

    public Single<List<AppUser>> getOwnersByIds(List<String> ownerIds) {
        return SingleCreate(emitter -> {
            List<AppUser> owners = getAppUsersById(ownerIds);
            owners = makeCurrentUserFirstInList(owners, ownerIds.get(0));

            if (!emitter.isDisposed()) {
                emitter.onSuccess(owners);
            }
        });
    }

    private List<AppUser> makeCurrentUserFirstInList(List<AppUser> owners, String currentUserId) {
        for (int i = 0; i < owners.size(); i++) {
            AppUser currentUser = owners.get(i);
            if (currentUser.getId().equals(currentUserId)) {
                AppUser currentFirstUser = owners.get(0);
                owners.set(0, currentUser);
                owners.set(i, currentFirstUser);
                break;
            }
        }

        return owners;
    }

    private List<AppUser> getAppUsersById(List<String> ownerIds) throws Exception {
        QuerySnapshot querySnapshot = Tasks.await(getCollection().whereIn(AppUser.ID_FIELD, ownerIds).get());

        return querySnapshot.toObjects(AppUser.class);
    }

    public Single<AppUser> getAppUserByEmailAsync(String email) {
        return SingleCreate(emitter -> {
            AppUser appUser = getAppUserByEmail(email);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(appUser);
            }
        });
    }

    private AppUser getAppUserByEmail(String email) throws Exception {
        QuerySnapshot querySnapshot = Tasks.await(getCollection().whereEqualTo(EMAIL_FILED, email).get());
        List<DocumentSnapshot> documents = querySnapshot.getDocuments();

        checkEmailUniqueness(documents);

        DocumentSnapshot document = documents.get(0);
        return document.toObject(AppUser.class);
    }

    public Single<Boolean> isUserRegisteredAsync(String email) {
        return SingleCreate(emitter -> {
            boolean isUserRegistered = isUserRegistered(email);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isUserRegistered);
            }
        });
    }

    private boolean isUserRegistered(String email) throws ExecutionException, InterruptedException {
        List<DocumentSnapshot> documentSnapshots =
                Tasks.await(
                        getCollection().whereEqualTo(EMAIL_FILED, email).get()).getDocuments();

        return documentSnapshots.size() > 0;
    }

    public Single<List<AppUser>> getUsersByEmailsAsync(List<String> emails) {
        return SingleCreate(emitter -> {
            List<AppUser> admins = getUsersByEmails(emails);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(admins);
            }
        });
    }

    private List<AppUser> getUsersByEmails(List<String> emails) throws Exception {
        if (emails.size() == 0) {
            return new ArrayList<>();
        }
        QuerySnapshot adminsQuery = Tasks.await(getCollection().whereIn(EMAIL_FILED, emails).get());

        return adminsQuery.toObjects(AppUser.class);
    }
}
