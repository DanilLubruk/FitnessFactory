package com.example.fitnessfactory.ui.viewmodels.lists.sessionParticipantList;

import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.dataListeners.ArgDataListener;
import com.example.fitnessfactory.data.dataListeners.SessionsCoachesListDataListener;
import com.example.fitnessfactory.data.managers.data.CoachesDataManager;
import com.example.fitnessfactory.data.managers.data.SessionsDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class SessionCoachesListTabViewModel extends SessionParticipantListTabViewModel<AppUser> {

    private final SessionsCoachesListDataListener dataListener;
    private final CoachesDataManager coachesDataManager;

    private MutableLiveData<List<AppUser>> coaches = new MutableLiveData<>();

    @Inject
    public SessionCoachesListTabViewModel(SessionsDataManager sessionsDataManager,
                                          SessionsCoachesListDataListener dataListener,
                                          CoachesDataManager coachesDataManager) {
        super(sessionsDataManager);
        this.dataListener = dataListener;
        this.coachesDataManager = coachesDataManager;
    }

    public MutableLiveData<List<AppUser>> getCoaches() {
        return coaches;
    }

    public void resetCoachesList(List<String> coachesEmails) {
        subscribeInIOThread(
                coachesDataManager.getCoachesUsers(coachesEmails),
                new SingleData<>(coaches::setValue, getErrorHandler()::handleError));
    }

    @Override
    protected ArgDataListener<String> getDataListener() {
        return dataListener;
    }

    @Override
    protected Completable getAddParticipantAction(String sessionId, String coachUserId) {
        return sessionsDataManager.addCoachToSession(sessionId, coachUserId);
    }

    @Override
    protected Completable getDeleteParticipantAction(String sessionId, String coachUserId) {
        return sessionsDataManager.removeCoachFromSession(sessionId, coachUserId);
    }

    @Override
    protected List<String> getParticipantsList(Session session) {
        return session.getCoachesIds();
    }

    @Override
    protected String getParticipantId(AppUser coach) {
        return coach.getId();
    }

    @Override
    protected String getItemNullClause() {
        return getErrorMessageBreak().concat(ResUtils.getString(R.string.message_error_coach_null));
    }
}
