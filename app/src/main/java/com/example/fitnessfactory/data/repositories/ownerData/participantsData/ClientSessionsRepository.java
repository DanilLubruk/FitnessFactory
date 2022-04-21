package com.example.fitnessfactory.data.repositories.ownerData.participantsData;

import com.example.fitnessfactory.data.firestoreCollections.ClientsCollection;
import com.example.fitnessfactory.data.firestoreCollections.ClientsSessionsCollection;
import com.example.fitnessfactory.data.firestoreCollections.OwnerCoachesCollection;
import com.example.fitnessfactory.data.models.Client;
import com.example.fitnessfactory.data.models.Personnel;
import com.example.fitnessfactory.data.models.Session;
import com.google.firebase.firestore.CollectionReference;

import java.util.List;

public class ClientSessionsRepository extends ParticipantSessionsRepository {

    @Override
    protected CollectionReference getCollection(String clientId) {
        return getFirestore().collection(ClientsSessionsCollection.getRoot(clientId));
    }

    private CollectionReference getClientsCollection() {
        return getFirestore().collection(ClientsCollection.getRoot());
    }

    @Override
    protected List<String> getParticipantsEmails(Session session) {
        return session.getClientsEmails();
    }

    protected String getParticipantId(String participantEmail) throws Exception {
        return getUniqueUserEntity(
                getClientsCollection().whereEqualTo(Client.EMAIL_FIELD, participantEmail),
                Client.class)
                .getId();
    }
}
