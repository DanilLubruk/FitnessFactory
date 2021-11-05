package com.example.fitnessfactory.data.repositories.ownerData;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.firestoreCollections.SessionsCollection;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.models.SessionType;
import com.example.fitnessfactory.data.repositories.BaseRepository;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.example.fitnessfactory.utils.TimeUtils;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;

public class SessionsRepository extends BaseRepository {

    @Override
    protected String getRoot() {
        return SessionsCollection.getRoot();
    }

    public Single<Boolean> doesSessionsTimeIntersectWithAnyAsync(String sessionId, List<String> sessionsIds) {
        return SingleCreate(emitter -> {
             if (!emitter.isDisposed()) {
                 emitter.onSuccess(doesSessionsTimeIntersectWithAny(sessionId, sessionsIds));
             }
        });
    }

    private boolean doesSessionsTimeIntersectWithAny(String comparedSessionId, List<String> sessionsIds) throws ExecutionException, InterruptedException {
        Session comparedSession = getSession(comparedSessionId);

        for (String sessionId : sessionsIds) {
            Session session = getSession(sessionId);
            if (doesSessionsTimeIntersect(comparedSession, session)) {
                return true;
            }
        }

        return false;
    }

    private boolean doesSessionsTimeIntersect(Session comparedSession, Session session) {
        long comparedStartTime = comparedSession.getStartTime().getTime();
        long comparedEndTime = comparedSession.getEndTime().getTime();

        long startTime = session.getStartTime().getTime();
        long endTime = session.getEndTime().getTime();

        return Math.min(comparedStartTime, startTime) < Math.max(comparedEndTime, endTime);
    }

    public Single<Boolean> isSessionPackedAsync(Session session, SessionType sessionType) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(isSessionPacked(session, sessionType));
            }
        });
    }

    private boolean isSessionPacked(Session session, SessionType sessionType) {
        if (session.getClientsIds() == null) {
            return false;
        }

        return session.getClientsIds().size() >= sessionType.getPeopleAmount();
    }

    public Single<Boolean> isGymNameOccupiedAsync(String gymName) {
        return SingleCreate(emitter -> {
           if (!emitter.isDisposed()) {
               emitter.onSuccess(isGymOccupied(gymName));
           }
        });
    }

    private boolean isGymOccupied(String gymName) throws ExecutionException, InterruptedException {
        return getEntitiesAmount(newQuery().whereGymNameEquals(gymName).build()) > 0;
    }

    public Single<Boolean> isSessionTypeOccupiedAsync(String sessionTypeName) {
        return SingleCreate(emitter -> {
           if (!emitter.isDisposed()) {
               emitter.onSuccess(isSessionTypeOccupied(sessionTypeName));
           }
        });
    }

    private boolean isSessionTypeOccupied(String sessionTypeName) throws ExecutionException, InterruptedException {
        return getEntitiesAmount(newQuery().whereSessionTypeNameEquals(sessionTypeName).build()) > 0;
    }

    public Single<WriteBatch> getDeleteBatchAsync(Session session) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getDeleteBatch(session));
            }
        });
    }

    private WriteBatch getDeleteBatch(Session session) {
        return getFirestore()
                .batch()
                .delete(getCollection().document(session.getId()));
    }

    public Single<WriteBatch> getRemoveCoachBatchAsync(String sessionId, String coachId) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getRemoveCoachBatch(sessionId, coachId));
            }
        });
    }

    private WriteBatch getRemoveCoachBatch(String sessionId, String coachId) {
        return getFirestore()
                .batch()
                .update(getCollection().document(sessionId),
                        Session.COACHES_IDS_FIELD,
                        FieldValue.arrayRemove(coachId));
    }

    public Single<WriteBatch> getRemoveClientBatchAsync(String sessionId, String clientId) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getRemoveClientBatch(sessionId, clientId));
            }
        });
    }

    private WriteBatch getRemoveClientBatch(String sessionId, String clientId) {
        return getFirestore()
                .batch()
                .update(getCollection().document(sessionId),
                        Session.CLIENTS_IDS_FIELD,
                        FieldValue.arrayRemove(clientId));
    }

    public Single<WriteBatch> getAddCoachBatchAsync(String sessionId, String coachId) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getAddCoachBatch(sessionId, coachId));
            }
        });
    }

    private WriteBatch getAddCoachBatch(String sessionId, String coachId) {
        return getFirestore()
                .batch()
                .update(getCollection().document(sessionId),
                        Session.COACHES_IDS_FIELD,
                        FieldValue.arrayUnion(coachId));
    }

    public Single<WriteBatch> getAddClientBatchAsync(String sessionId, String clientId) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getAddClientBatch(sessionId, clientId));
            }
        });
    }

    private WriteBatch getAddClientBatch(String sessionId, String clientId) {
        return getFirestore()
                .batch()
                .update(getCollection().document(sessionId),
                        Session.CLIENTS_IDS_FIELD,
                        FieldValue.arrayUnion(clientId));
    }

    public Single<Session> getSessionAsync(String sessionId) {
        return SingleCreate(emitter -> {
            Session session = getSession(sessionId);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(session);
            }
        });
    }

    private Session getSession(String sessionId) throws ExecutionException, InterruptedException {
        if (StringUtils.isEmpty(sessionId)) {
            return new Session();
        }

        return Tasks.await(getCollection().document(sessionId).get()).toObject(Session.class);
    }

    public Single<Boolean> saveAsync(Session session) {
        return SingleCreate(emitter -> {
            boolean isSaved = save(session);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(isSaved);
            }
        });
    }

    private boolean save(Session session) throws Exception {
        if (session == null) {
            throw new Exception(getEntitySavingNullMessage());
        }
        boolean isNewEntity = StringUtils.isEmpty(session.getId());

        return isNewEntity ? insert(session) : update(session);
    }

    private boolean insert(Session session) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = getCollection().document();
        session.setId(documentReference.getId());
        Tasks.await(documentReference.set(session));

        return true;
    }

    private boolean update(Session session) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = getCollection().document(session.getId());
        Tasks.await(getFirestore().batch()
                .update(documentReference, Session.ID_FIELD, session.getId())
                .update(documentReference, Session.DATE_FIELD, session.getDate())
                .update(documentReference, Session.START_TIME_FIELD, session.getStartTime())
                .update(documentReference, Session.END_TIME_FIELD, session.getEndTime())
                .update(documentReference, Session.GYM_NAME_FIELD, session.getGymName())
                .update(documentReference, Session.SESSION_TYPE_NAME_FIELD, session.getSessionTypeName())
                .commit());

        return true;
    }

    public Single<Date> checkSessionStartTimeCorrectAsync(Date startTime, Date endTime) {
        return SingleCreate(emitter -> {
            Date time = checkSessionStartTimeCorrect(startTime, endTime);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(time);
            }
        });
    }

    private Date checkSessionStartTimeCorrect(Date startTime, Date endTime) throws Exception {
        if (TimeUtils.areDatesCorrectPeriod(startTime, endTime)) {
            return startTime;
        } else {
            throw new Exception(ResUtils.getString(R.string.message_error_wrong_dates));
        }
    }

    public Single<Date> checkSessionEndTimeCorrectAsync(Date startTime, Date endTime) {
        return SingleCreate(emitter -> {
            Date time = checkSessionEndTimeCorrect(startTime, endTime);

            if (!emitter.isDisposed()) {
                emitter.onSuccess(time);
            }
        });
    }

    private Date checkSessionEndTimeCorrect(Date startTime, Date endTime) throws Exception {
        if (TimeUtils.areDatesCorrectPeriod(startTime, endTime)) {
            return endTime;
        } else {
            throw new Exception(ResUtils.getString(R.string.message_error_wrong_dates));
        }
    }

    @Override
    protected String getEntitySavingNullMessage() {
        return super.getEntitySavingNullMessage()
                .concat(ResUtils.getString(R.string.message_error_session_null));
    }

    private SessionsRepository.QueryBuilder newQuery() {
        return new SessionsRepository().new QueryBuilder();
    }

    private class QueryBuilder {

        Query query = getCollection();

        public QueryBuilder whereIdEquals(String sessionId) {
            query = query.whereEqualTo(Session.ID_FIELD, sessionId);
            return this;
        }

        public QueryBuilder whereSessionTypeNameEquals(String sessionTypeName) {
            query = query.whereEqualTo(Session.SESSION_TYPE_NAME_FIELD, sessionTypeName);
            return this;
        }

        public QueryBuilder whereGymNameEquals(String gymName) {
            query = query.whereEqualTo(Session.GYM_NAME_FIELD, gymName);
            return this;
        }

        public Query build() {
            return query;
        }
    }
}
