package com.example.fitnessfactory.data.firestoreCollections;

import com.example.fitnessfactory.data.FirestoreCollections;

public class CoachesSessionsCollection {

    public static String getRoot(String coachId) {
        return OwnerCoachesCollection.getRoot() +
                "/" +
                coachId +
                "/" +
                FirestoreCollections.SESSIONS_COLLECTION;
    }
}
