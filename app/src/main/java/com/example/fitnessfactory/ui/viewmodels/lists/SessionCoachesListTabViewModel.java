package com.example.fitnessfactory.ui.viewmodels.lists;

import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.dataListeners.ArgDataListener;
import com.example.fitnessfactory.data.dataListeners.SessionsCoachesListDataListener;
import com.example.fitnessfactory.data.managers.data.SessionsDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.Personnel;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.repositories.UserRepository;
import com.example.fitnessfactory.data.repositories.ownerData.SessionsRepository;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class SessionCoachesListTabViewModel extends SessionParticipantListTabViewModel<AppUser> {

    private SessionsCoachesListDataListener dataListener;
    private UserRepository userRepository;

    private MutableLiveData<List<AppUser>> coaches = new MutableLiveData<>();

    @Inject
    public SessionCoachesListTabViewModel(SessionsDataManager sessionsDataManager,
                                          SessionsRepository sessionsRepository,
                                          SessionsCoachesListDataListener dataListener,
                                          UserRepository userRepository) {
        super(sessionsDataManager, sessionsRepository);
        this.dataListener = dataListener;
        this.userRepository = userRepository;
    }

    public MutableLiveData<List<AppUser>> getCoaches() {
        return coaches;
    }

    public void resetCoachesList(List<String> coachesEmails) {
        subscribeInIOThread(
                userRepository.getUsersByEmailsAsync(coachesEmails),
                new SingleData<>(coaches::setValue, getErrorHandler()::handleError));
    }

    @Override
    protected ArgDataListener<List<String>> getDataListener() {
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
    protected String getParticipantNullMessage() {
        return ResUtils.getString(R.string.message_error_coach_null);
    }
}
