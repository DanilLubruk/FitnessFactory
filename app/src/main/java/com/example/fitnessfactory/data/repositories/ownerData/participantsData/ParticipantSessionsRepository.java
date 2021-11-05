package com.example.fitnessfactory.data.repositories.ownerData.participantsData;

import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.models.UsersSession;
import com.example.fitnessfactory.data.repositories.BaseRepository;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Single;

public abstract class ParticipantSessionsRepository extends BaseRepository {

    private DocumentReference getSessionDocument(String sessionId, String participantId) {
        return getCollection(participantId).document(sessionId);
    }

    protected abstract CollectionReference getCollection(String participantId);

    protected abstract List<String> getParticipantsIds(Session session);

    public Single<List<String>> getParticipantSessionsIdsAsync(String participantId) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getParticipantSessionsIds(participantId));
            }
        });
    }

    private List<String> getParticipantSessionsIds(String participantId) throws ExecutionException, InterruptedException {
        List<DocumentSnapshot> documentSnapshots = Tasks.await(getCollection(participantId).get()).getDocuments();

        List<String> sessionsIds = new ArrayList<>();
        for (DocumentSnapshot documentSnapshot : documentSnapshots) {
            sessionsIds.add(documentSnapshot.getString(UsersSession.ID_FIELD));
        }

        return sessionsIds;
    }

    public Single<Boolean> isParticipantOccupiedAsync(String participantId) {
        return SingleCreate(emitter -> {
           if (!emitter.isDisposed()) {
               emitter.onSuccess(isParticipantOccupied(participantId));
           }
        });
    }

    private boolean isParticipantOccupied(String participantId) throws ExecutionException, InterruptedException {
        return getParticipantSessionsAmount(participantId) > 0;
    }

    private int getParticipantSessionsAmount(String participantId) throws ExecutionException, InterruptedException {
        return Tasks.await(getCollection(participantId).get()).getDocuments().size();
    }

    public Single<WriteBatch> getDeleteSessionBatchAsync(WriteBatch writeBatch,
                                                         Session session) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getDeleteSessionBatch(writeBatch, session));
            }
        });
    }

    private WriteBatch getDeleteSessionBatch(WriteBatch writeBatch,
                                             Session session) {
        List<String> participantsIds = getParticipantsIds(session);
        if (participantsIds == null) {
            return writeBatch;
        }

        for (String participantId : participantsIds) {
            writeBatch = writeBatch.delete(getSessionDocument(session.getId(), participantId));
        }

        return writeBatch;
    }

    public Single<WriteBatch> getRemoveSessionBatchAsync(WriteBatch writeBatch,
                                                         String sessionId,
                                                         String clientId) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getRemoveSessionBatch(writeBatch, sessionId, clientId));
            }
        });
    }

    private WriteBatch getRemoveSessionBatch(WriteBatch writeBatch,
                                             String sessionId,
                                             String clientId) {
        return writeBatch.delete(getSessionDocument(sessionId, clientId));
    }

    public Single<WriteBatch> getAddSessionBatchAsync(WriteBatch writeBatch,
                                                      String sessionId,
                                                      String clientId) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getAddSessionBatch(writeBatch, sessionId, clientId));
            }
        });
    }

    private WriteBatch getAddSessionBatch(WriteBatch writeBatch,
                                          String sessionId,
                                          String clientId) {
        return writeBatch
                .set(getSessionDocument(sessionId, clientId),
                        UsersSession.builder().setSessionId(sessionId).build());
    }
}
