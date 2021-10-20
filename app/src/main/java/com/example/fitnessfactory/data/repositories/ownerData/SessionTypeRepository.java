package com.example.fitnessfactory.data.repositories.ownerData;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.firestoreCollections.SessionTypesCollection;
import com.example.fitnessfactory.data.models.SessionType;
import com.example.fitnessfactory.data.repositories.BaseRepository;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import java.util.concurrent.ExecutionException;

import io.reactivex.Completable;
import io.reactivex.Single;

public class SessionTypeRepository extends BaseRepository {

    @Override
    public String getRoot() {
        return SessionTypesCollection.getRoot();
    }

    public Single<Boolean> saveAsync(SessionType sessionType) {
        return SingleCreate(emitter -> {
            boolean isSaved = save(sessionType);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isSaved);
            }
        });
    }

    private boolean save(SessionType sessionType) throws Exception {
        if (sessionType == null) {
            throw new Exception(getEntityNullMessage());
        }
        boolean isNewEntity = StringUtils.isEmpty(sessionType.getId());

        return isNewEntity ? insert(sessionType) : update(sessionType);
    }

    private boolean insert(SessionType sessionType) throws Exception {
        if (isNewSessionTypeNameOccupied(sessionType)) {
            throw new Exception(getNameOccupiedMessage());
        }

        DocumentReference documentReference = getCollection().document();
        sessionType.setId(documentReference.getId());
        Tasks.await(documentReference.set(sessionType));

        return true;
    }

    private boolean isNewSessionTypeNameOccupied(SessionType sessionType) throws ExecutionException, InterruptedException {
        int typesAmount =
                getEntitiesAmount(newQuery()
                        .whereNameEquals(sessionType.getName()).build());

        return typesAmount > 0;
    }

    private boolean update(SessionType sessionType) throws Exception {
        if (isExistingSessionTypeNameOccupied(sessionType)) {
            throw new Exception(getNameOccupiedMessage());
        }

        Tasks.await(getCollection().document(sessionType.getId()).set(sessionType));

        return true;
    }

    private boolean isExistingSessionTypeNameOccupied(SessionType sessionType) throws ExecutionException, InterruptedException {
        int typesAmount =
                getEntitiesAmount(newQuery()
                        .whereIdNotEquals(sessionType.getId())
                        .whereNameEquals(sessionType.getName())
                        .build());

        return typesAmount > 0;
    }

    public Single<Boolean> deleteSessionTypeSingle(SessionType sessionType) {
        return SingleCreate(emitter -> {
            boolean isDeleted = deleteSessionType(sessionType);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isDeleted);
            }
        });
    }

    public Completable deleteSessionTypeCompletable(SessionType sessionType) {
        return CompletableCreate(emitter -> {
            deleteSessionType(sessionType);

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        });
    }

    private boolean deleteSessionType(SessionType sessionType) throws ExecutionException, InterruptedException {
        Tasks.await(getCollection().document(sessionType.getId()).delete());

        return true;
    }

    public Single<SessionType> getSessionTypeAsync(String typeId) {
        return SingleCreate(emitter -> {
            SessionType sessionType = getSessionType(typeId);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(sessionType);
            }
        });
    }

    private SessionType getSessionType(String typeId) throws Exception {
        if (StringUtils.isEmpty(typeId)) {
            return new SessionType();
        }

        return getUniqueSessionType(typeId);
    }

    private SessionType getUniqueSessionType(String typeId) throws Exception {
        return getUniqueEntity(newQuery().whereIdEquals(typeId).build(), SessionType.class, getSessionTypeNotUniqueMessage());
    }

    private String getSessionTypeNotUniqueMessage() {
        return ResUtils.getString(R.string.message_session_type_not_unique);
    }

    @Override
    protected String getEntityNullMessage() {
        return super.getEntityNullMessage()
                .concat(ResUtils.getString(R.string.message_session_type_null));
    }

    private String getNameOccupiedMessage() {
        return ResUtils.getString(R.string.message_session_type_name_occupied);
    }

    private SessionTypeRepository.QueryBuilder newQuery() {
        return new SessionTypeRepository().new QueryBuilder();
    }

    private class QueryBuilder {

        Query query = getCollection();

        public QueryBuilder whereIdNotEquals(String typeId) {
            query = query.whereNotEqualTo(SessionType.ID_FIELD, typeId);
            return this;
        }

        public QueryBuilder whereIdEquals(String typeId) {
            query = query.whereEqualTo(SessionType.ID_FIELD, typeId);
            return this;
        }

        public QueryBuilder whereNameEquals(String name) {
            query = query.whereEqualTo(SessionType.NAME_FIELD, name);
            return this;
        }

        public Query build() {
            return query;
        }
    }
}
