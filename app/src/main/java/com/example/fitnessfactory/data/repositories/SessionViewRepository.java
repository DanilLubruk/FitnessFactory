package com.example.fitnessfactory.data.repositories;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.firestoreCollections.OwnerGymsCollection;
import com.example.fitnessfactory.data.firestoreCollections.SessionTypesCollection;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.models.SessionType;
import com.example.fitnessfactory.data.views.SessionView;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class SessionViewRepository extends BaseRepository {

    private CollectionReference getSessionTypesCollection() {
        return getFirestore().collection(SessionTypesCollection.getRoot());
    }

    private CollectionReference getGymsCollection() {
        return getFirestore().collection(OwnerGymsCollection.getRoot());
    }

    public Single<String> getSessionTypeNameAsync(String sessionTypeId) {
        return SingleCreate(emitter -> {
           if (!emitter.isDisposed()) {
               emitter.onSuccess(getSessionTypeName(sessionTypeId));
           }
        });
    }

    public Single<String> getGymNameAsync(String gymId) {
        return SingleCreate(emitter -> {
           if (!emitter.isDisposed()) {
               emitter.onSuccess(getGymName(gymId));
           }
        });
    }

    public Single<SessionView> getSessionViewAsync(Session session) {
        return SingleCreate(emitter -> {
           if (!emitter.isDisposed()) {
               emitter.onSuccess(getSessionView(session));
           }
        });
    }

    public Single<List<SessionView>> getSessionViewsListAsync(List<Session> sessions) {
        return SingleCreate(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(getSessionViewsList(sessions));
            }
        });
    }

    private List<SessionView> getSessionViewsList(List<Session> sessions) throws Exception {
        List<SessionView> sessionViewsList = new ArrayList<>();

        for (Session session : sessions) {
            sessionViewsList.add(getSessionView(session));
        }

        return sessionViewsList;
    }

    private SessionView getSessionView(Session session) throws Exception {
        SessionView sessionView = new SessionView(session);

        String sessionTypeName = getSessionTypeName(session.getSessionTypeId());
        sessionView.setSessionTypeName(sessionTypeName);

        String gymName = getGymName(session.getGymId());
        sessionView.setGymName(gymName);

        return sessionView;
    }

    private String getSessionTypeName(String sessionTypeId) throws Exception {
        if (StringUtils.isEmpty(sessionTypeId)) {
            return "";
        }

        return getUniqueEntitySnapshot(
                getSessionTypesCollection().whereEqualTo(SessionType.ID_FIELD, sessionTypeId),
                getSessionTypeUnuniqueMessage())
                .getString(SessionType.NAME_FIELD);
    }

    private String getSessionTypeUnuniqueMessage() {
        return ResUtils.getString(R.string.message_error_session_type_id_not_unique);
    }

    private String getGymName(String gymId) throws Exception {
        if (StringUtils.isEmpty(gymId)) {
            return "";
        }

        return getUniqueEntitySnapshot(
                getGymsCollection().whereEqualTo(Gym.ID_FIELD, gymId),
                getGymIdUnuniqueMessage())
                .getString(Gym.NAME_FILED);
    }

    private String getGymIdUnuniqueMessage() {
        return ResUtils.getString(R.string.message_error_gym_id_not_unique);
    }
}
