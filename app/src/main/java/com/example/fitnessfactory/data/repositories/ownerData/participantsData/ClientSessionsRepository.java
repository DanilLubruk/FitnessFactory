package com.example.fitnessfactory.data.repositories.ownerData.participantsData;

import com.example.fitnessfactory.data.firestoreCollections.ClientsSessionsCollection;
import com.example.fitnessfactory.data.models.Session;
import com.google.firebase.firestore.CollectionReference;

import java.util.List;

public class ClientSessionsRepository extends ParticipantSessionsRepository {

    @Override
    protected CollectionReference getCollection(String clientId) {
        return getFirestore().collection(ClientsSessionsCollection.getRoot(clientId));
    }

    @Override
    protected List<String> getParticipantsIds(Session session) {
        return session.getClientsIds();
    }
}
