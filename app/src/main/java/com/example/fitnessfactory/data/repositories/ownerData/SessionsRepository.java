package com.example.fitnessfactory.data.repositories.ownerData;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.firestoreCollections.SessionsCollection;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.repositories.BaseRepository;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;

import java.util.concurrent.ExecutionException;

import io.reactivex.Single;

public class SessionsRepository extends BaseRepository {

    @Override
    protected String getRoot() {
        return SessionsCollection.getRoot();
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

    @Override
    protected String getEntitySavingNullMessage() {
        return super.getEntitySavingNullMessage()
                .concat(ResUtils.getString(R.string.message_error_session_null));
    }
}
