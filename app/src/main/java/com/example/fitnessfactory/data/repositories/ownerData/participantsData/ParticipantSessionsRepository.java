package com.example.fitnessfactory.data.repositories.ownerData.participantsData;

import com.example.fitnessfactory.data.models.UsersSession;
import com.example.fitnessfactory.data.repositories.BaseRepository;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.WriteBatch;

import io.reactivex.Single;

public abstract class ParticipantSessionsRepository extends BaseRepository {

    private DocumentReference getSessionDocument(String sessionId, String clientId) {
        return getCollection(clientId).document(sessionId);
    }

    protected abstract CollectionReference getCollection(String participantId);

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
