package com.example.fitnessfactory.data.repositories.ownerData.participantsData;

import com.example.fitnessfactory.data.firestoreCollections.CoachesSessionsCollection;
import com.example.fitnessfactory.data.models.Session;
import com.google.firebase.firestore.CollectionReference;

import java.util.List;

public class CoachSessionsRepository extends ParticipantSessionsRepository {

    @Override
    protected CollectionReference getCollection(String coachId) {
        return getFirestore().collection(CoachesSessionsCollection.getRoot(coachId));
    }

    @Override
    protected List<String> getParticipantsIds(Session session) {
        return session.getCoachesIds();
    }
}
