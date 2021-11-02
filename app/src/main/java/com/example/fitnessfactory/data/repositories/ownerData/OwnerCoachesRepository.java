package com.example.fitnessfactory.data.repositories.ownerData;

import com.example.fitnessfactory.data.firestoreCollections.CoachesSessionsCollection;
import com.example.fitnessfactory.data.firestoreCollections.OwnerCoachesCollection;
import com.example.fitnessfactory.data.models.Personnel;
import com.example.fitnessfactory.data.models.UsersSession;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.WriteBatch;
import com.google.firestore.v1.Write;

import io.reactivex.Single;

public class OwnerCoachesRepository extends OwnerPersonnelRepository {

    @Override
    protected String getRoot() {
        return OwnerCoachesCollection.getRoot();
    }

    private DocumentReference getSessionDocument(String sessionId, String coachId) {
        return getSessionsCollection(coachId).document(sessionId);
    }

    private CollectionReference getSessionsCollection(String coachId) {
        return getFirestore().collection(CoachesSessionsCollection.getRoot(coachId));
    }

    public Single<WriteBatch> getRemoveSessionBatchAsync(WriteBatch writeBatch,
                                                         String sessionId,
                                                         String coachId) {
        return SingleCreate(emitter -> {
           if (!emitter.isDisposed()) {
               emitter.onSuccess(getRemoveSessionBatch(writeBatch, sessionId, coachId));
           }
        });
    }

    private WriteBatch getRemoveSessionBatch(WriteBatch writeBatch,
                                             String sessionId,
                                             String coachId) {
        return writeBatch.delete(getSessionDocument(sessionId, coachId));
    }

    public Single<WriteBatch> getAddSessionBatchAsync(WriteBatch writeBatch,
                                                      String sessionId,
                                                      String coachId) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getAddSessionBatch(writeBatch, sessionId, coachId));
            }
        });
    }

    private WriteBatch getAddSessionBatch(WriteBatch writeBatch,
                                          String sessionId,
                                          String coachId) {
        return writeBatch.set(
                getSessionDocument(sessionId, coachId),
                UsersSession.builder().setSessionId(sessionId).build());
    }

    public Single<String> getCoachIdByEmailAsync(String coachEmail) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getCoachIdByEmail(coachEmail));
            }
        });
    }

    private String getCoachIdByEmail(String coachEmail) throws Exception {
        Personnel personnel =
                getUniqueUserEntity(
                        newQuery().whereUserEmailEquals(coachEmail).build(),
                        Personnel.class);

        return personnel.getId();
    }

    @Override
    protected OwnerCoachesRepository.QueryBuilder newQuery() {
        return new OwnerCoachesRepository().new QueryBuilder();
    }
}
