package com.example.fitnessfactory.data.repositories.ownerData;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.firestoreCollections.SessionsCollection;
import com.example.fitnessfactory.data.models.Personnel;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.repositories.BaseRepository;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.example.fitnessfactory.utils.TimeUtils;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Completable;
import io.reactivex.Single;

public class SessionsRepository extends BaseRepository {

    @Override
    protected String getRoot() {
        return SessionsCollection.getRoot();
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
        Tasks.await(documentReference.set(session));

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

    private SessionsRepository.QueryBuilder newBuilder() {
        return new SessionsRepository().new QueryBuilder();
    }

    private class QueryBuilder {

        Query query = getCollection();

        public Query build() {
            return query;
        }
    }
}
