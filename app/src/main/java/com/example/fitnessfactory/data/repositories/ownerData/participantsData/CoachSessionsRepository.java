package com.example.fitnessfactory.data.repositories.ownerData.participantsData;

import com.example.fitnessfactory.data.firestoreCollections.CoachesSessionsCollection;
import com.example.fitnessfactory.data.firestoreCollections.OwnerCoachesCollection;
import com.example.fitnessfactory.data.models.Personnel;
import com.example.fitnessfactory.data.models.Session;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CoachSessionsRepository extends ParticipantSessionsRepository {

    @Override
    protected CollectionReference getCollection(String coachId) {
        return getFirestore().collection(CoachesSessionsCollection.getRoot(coachId));
    }

    private CollectionReference getCoachCollection() {
        return getFirestore().collection(OwnerCoachesCollection.getRoot());
    }

    @Override
    protected List<String> getParticipantsEmails(Session session) {
        return session.getCoachesEmails();
    }

    protected String getParticipantId(String participantEmail) throws Exception {
        return getUniqueUserEntity(
                getCoachCollection().whereEqualTo(Personnel.USER_EMAIL_FIELD, participantEmail),
                Personnel.class)
                .getId();
    }

}
