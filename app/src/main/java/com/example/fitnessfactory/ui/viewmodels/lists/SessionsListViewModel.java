package com.example.fitnessfactory.ui.viewmodels.lists;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.dataListeners.DaysSessionsListDataListener;
import com.example.fitnessfactory.data.dataListeners.SessionsCalendarDataListener;
import com.example.fitnessfactory.data.managers.data.SessionsDataManager;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleDialogEvent;
import com.example.fitnessfactory.data.repositories.SessionViewRepository;
import com.example.fitnessfactory.data.views.SessionView;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class SessionsListViewModel extends ListViewModel<SessionView> {

    private final SessionsDataManager sessionsDataManager;
    private final SessionViewRepository sessionViewRepository;
    private final SessionsCalendarDataListener calendarDataListener;
    private final DaysSessionsListDataListener daysSessionsDataListener;

    private MutableLiveData<List<SessionView>> sessions = new MutableLiveData<>();
    private MutableLiveData<Date> date = new MutableLiveData<>();

    @Inject
    public SessionsListViewModel(SessionsDataManager sessionsDataManager,
                                 SessionViewRepository sessionViewRepository,
                                 SessionsCalendarDataListener calendarDataListener,
                                 DaysSessionsListDataListener daysSessionsDataListener) {
        this.sessionsDataManager = sessionsDataManager;
        this.sessionViewRepository = sessionViewRepository;
        this.calendarDataListener = calendarDataListener;
        this.daysSessionsDataListener = daysSessionsDataListener;
    }

    public MutableLiveData<List<SessionView>> getSessions() {
        return sessions;
    }

    public void getSessionsData(List<Session> sessionList) {
        subscribeInIOThread(
                sessionViewRepository.getSessionViewsListAsync(sessionList),
                new SingleData<>(sessions::setValue, getErrorHandler()::handleError));
    }

    public void startCalendarDataListener(Date startDate, Date endDate) {
        calendarDataListener.startDataListener(startDate, endDate);
    }

    public void stopCalendarDataListener() {
        calendarDataListener.stopDataListener();
    }

    public void setDate(Date date) {
        this.date.setValue(date);
    }

    public LiveData<Date> getDate() {
        return date;
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

    private String getDateNullError() {
        return ResUtils.getString(R.string.message_error_date_null);
    }

    @Override
    public void startDataListener() {
        Date date = this.date.getValue();
        if (date == null) {
            return;
        }

        daysSessionsDataListener.startDataListener(date);
    }

    public void stopDataListener() {
        daysSessionsDataListener.stopDataListener();
    }

    public void deleteItem(SessionView item) {
        if (item == null) {
            handleItemDeletingNullError();
            return;
        }
        Session session = item.getSession();
        if (session == null) {
            handleItemDeletingNullError();
            return;
        }

        subscribeInIOThread(sessionsDataManager.deleteSessionCompletable(session));
    }

    @Override
    protected String getItemNullClause() {
        return getErrorMessageBreak().concat(ResUtils.getString(R.string.message_error_session_null));
    }
}
