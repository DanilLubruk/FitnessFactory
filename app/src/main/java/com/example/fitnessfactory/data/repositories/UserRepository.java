package com.example.fitnessfactory.data.repositories;
import com.example.fitnessfactory.data.firestoreCollections.UsersCollection;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.utils.UsersUtils;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;

import static com.example.fitnessfactory.data.models.AppUser.EMAIL_FILED;

public class UserRepository extends BaseRepository {

    @Override
    protected String getRoot() {
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

    public Single<List<AppUser>> getUsersByIdsAsync(List<String> usersIds) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getAppUsersByIds(usersIds));
            }
        });
    }

    public Single<List<AppUser>> getOwnersByIds(List<String> ownerIds, String currentUserId) {
        return SingleCreate(emitter -> {
            List<AppUser> owners = getAppUsersByIds(ownerIds);
            owners = UsersUtils.makeCurrentUserFirstInList(owners, currentUserId);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(owners);
            }
        });
    }

    private List<AppUser> getAppUsersByIds(List<String> usersIds) throws Exception {
        if (usersIds.isEmpty()) {
            return new ArrayList<>();
        }
        QuerySnapshot querySnapshot = Tasks.await(newQuery().whereIdIn(usersIds).build().get());

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
        return getUniqueUserEntity(newQuery().whereEmailEquals(email).build(), AppUser.class);
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
        int usersAmount = getEntitiesAmount(newQuery().whereEmailEquals(email).build());

        return usersAmount > 0;
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
        QuerySnapshot adminsQuery = Tasks.await(newQuery().whereEmailIn(emails).build().get());

        return adminsQuery.toObjects(AppUser.class);
    }

    private UserRepository.QueryBuilder newQuery() {
        return new UserRepository().new QueryBuilder();
    }

    private class QueryBuilder {

        Query query = getCollection();

        public QueryBuilder whereEmailEquals(String userEmail) {
            query = query.whereEqualTo(EMAIL_FILED, userEmail);
            return this;
        }

        public QueryBuilder whereEmailIn(List<String> usersEmails) {
            query = query.whereIn(EMAIL_FILED, usersEmails);
            return this;
        }

        public QueryBuilder whereIdIn(List<String> ownerIds) {
            query = query.whereIn(AppUser.ID_FIELD, ownerIds);
            return this;
        }

        public Query build() {
            return query;
        }
    }
}
