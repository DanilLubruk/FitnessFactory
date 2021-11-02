package com.example.fitnessfactory.data.repositories.ownerData.participantsData;

import com.example.fitnessfactory.data.firestoreCollections.CoachesSessionsCollection;
import com.google.firebase.firestore.CollectionReference;

public class CoachSessionsRepository extends ParticipantSessionsRepository {

    @Override
    protected CollectionReference getCollection(String coachId) {
        return getFirestore().collection(CoachesSessionsCollection.getRoot(coachId));
    }
}
