package com.example.fitnessfactory.ui.viewmodels.lists;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.dataListeners.CoachDaysSessionsListDataListener;
import com.example.fitnessfactory.data.managers.data.SessionsDataManager;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleDialogEvent;
import com.example.fitnessfactory.data.repositories.SessionViewRepository;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerCoachesRepository;
import com.example.fitnessfactory.data.views.SessionView;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

public class CoachSessionsListTabViewModel extends ListViewModel<SessionView> {

    private final SessionsDataManager sessionsDataManager;
    private final SessionViewRepository sessionViewRepository;
    private final OwnerCoachesRepository ownerCoachesRepository;
    private final CoachDaysSessionsListDataListener dataListener;

    private final MutableLiveData<List<SessionView>> sessions = new MutableLiveData<>();
    private final MutableLiveData<Date> date = new MutableLiveData<>();
    public final ObservableField<Date> dateField = new ObservableField<>();
    private String coachEmail;

    @Inject
    public CoachSessionsListTabViewModel(SessionsDataManager sessionsDataManager,
                                         SessionViewRepository sessionViewRepository,
                                         OwnerCoachesRepository ownerCoachesRepository,
                                         CoachDaysSessionsListDataListener dataListener) {
        this.sessionsDataManager = sessionsDataManager;
        this.sessionViewRepository = sessionViewRepository;
        this.ownerCoachesRepository = ownerCoachesRepository;
        this.dataListener = dataListener;
    }

    public void setDefaultDate() {
        setDate(new Date());
    }

    private void setDate(Date date) {
        this.date.setValue(date);
        this.dateField.set(date);
    }

    public LiveData<Date> getDate() {
        return date;
    }

    public void resetCoachEmail(String coachEmail) {
        this.coachEmail = coachEmail;
    }

    public MutableLiveData<List<SessionView>> getSessions() {
        return sessions;
    }

    public void getSessionsData(List<Session> sessionList) {
        subscribeInIOThread(
                sessionViewRepository.getSessionViewsListAsync(sessionList),
                new SingleData<>(sessions::setValue, getErrorHandler()::handleError));
    }

    public void addCoachToSession(String sessionId) {
        if (StringUtils.isEmpty(coachEmail)) {
            handleItemDeletingNullError();
            return;
        }
        if (StringUtils.isEmpty(sessionId)) {
            handleSessionDeletingNullError();
            return;
        }

        subscribeInIOThread(sessionsDataManager.addCoachToSession(sessionId, coachEmail));
    }

    public void changeSessionDate(SingleDialogEvent<Date, Date> dialogEvent) {
        Date date = this.date.getValue();
        if (date == null) {
            GuiUtils.showMessage(getDateNullError());
            return;
        }

        subscribeInMainThread(
                dialogEvent.showDialog(date),
                new SingleData<>(this::setDate, getErrorHandler()::handleError));
    }

    @Override
    public void startDataListener() {
        if (StringUtils.isEmpty(coachEmail)) {
            handleItemDeletingNullError();
            return;
        }
        Date date = this.date.getValue();
        if (date == null) {
            return;
        }

        subscribeInIOThread(
                ownerCoachesRepository.getPersonnelIdByEmailAsync(coachEmail),
                new SingleData<>(
                        coachId -> dataListener.startDataListener(date, coachId),
                        getErrorHandler()::handleError));
    }

    private String getDateNullError() {
        return ResUtils.getString(R.string.message_error_date_null);
    }

    @Override
    public void stopDataListener() {
        dataListener.stopDataListener();
    }

    @Override
    public void deleteItem(SessionView item) {
        if (StringUtils.isEmpty(coachEmail)) {
            handleItemDeletingNullError();
            return;
        }
        if (item == null) {
            handleSessionDeletingNullError();
            return;
        }
        Session session = item.getSession();
        if (session == null) {
            handleSessionDeletingNullError();
            return;
        }

        subscribeInIOThread(sessionsDataManager.removeCoachFromSession(session.getId(), coachEmail));
    }

    private void handleSessionDeletingNullError() {
        GuiUtils.showMessage(
                getErrorDeletingMessage()
                        .concat(" - ")
                        .concat(ResUtils.getString(R.string.message_error_session_null)));
    }

    @Override
    protected String getItemNullClause() {
        return getErrorMessageBreak().concat(ResUtils.getString(R.string.message_error_coach_null));
    }
}
