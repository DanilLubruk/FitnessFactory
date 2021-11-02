package com.example.fitnessfactory.data.repositories.ownerData.participantsData;

import com.example.fitnessfactory.data.firestoreCollections.ClientsSessionsCollection;
import com.google.firebase.firestore.CollectionReference;

public class ClientSessionsRepository extends ParticipantSessionsRepository {

    @Override
    protected CollectionReference getCollection(String clientId) {
        return getFirestore().collection(ClientsSessionsCollection.getRoot(clientId));
    }
}
