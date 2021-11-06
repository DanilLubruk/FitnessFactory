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

    public void resetCoachesList(List<String> coachesIds) {
        subscribeInIOThread(
                coachesDataManager.getCoachesUsers(coachesIds),
                new SingleData<>(coaches::setValue, getErrorHandler()::handleError));
    }

    @Override
    protected ArgDataListener<String> getDataListener() {
        return dataListener;
    }

    @Override
    protected Completable getAddParticipantAction(String sessionId, String coachEmail) {
        return sessionsDataManager.addCoachToSession(sessionId, coachEmail);
    }

    @Override
    protected Completable getDeleteParticipantAction(String sessionId, String coachEmail) {
        return sessionsDataManager.removeCoachFromSession(sessionId, coachEmail);
    }

    @Override
    protected List<String> getParticipantsList(Session session) {
        return session.getCoachesIds();
    }

    @Override
    protected String getParticipantId(AppUser coach) {
        return coach.getEmail();
    }

    @Override
    protected String getItemNullClause() {
        return getErrorMessageBreak().concat(ResUtils.getString(R.string.message_error_coach_null));
    }
}
