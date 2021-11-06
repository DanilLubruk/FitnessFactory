package com.example.fitnessfactory.ui.viewmodels.lists;

import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.dataListeners.DaysSessionsListDataListener;
import com.example.fitnessfactory.data.dataListeners.SessionsCalendarDataListener;
import com.example.fitnessfactory.data.managers.data.SessionsDataManager;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.repositories.SessionViewRepository;
import com.example.fitnessfactory.data.views.SessionView;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataOperatingViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class SessionsListViewModel extends DataOperatingViewModel {

    private final SessionsDataManager sessionsDataManager;
    private final SessionViewRepository sessionViewRepository;
    private final SessionsCalendarDataListener calendarDataListener;
    private final DaysSessionsListDataListener daysSessionsDataListener;

    private MutableLiveData<List<SessionView>> sessions = new MutableLiveData<>();

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

    public void startDaysSessionsDataListener(Date date) {
        daysSessionsDataListener.startDataListener(date);
    }

    public void stopDaysSessionsDataListener() {
        daysSessionsDataListener.stopDataListener();
    }

    public void deleteItem(Session item) {
        if (item == null) {
            handleItemDeletingNullError();
            return;
        }

        subscribeInIOThread(sessionsDataManager.deleteSessionCompletable(item));
    }

    @Override
    protected String getItemNullClause() {
        return getErrorMessageBreak().concat(ResUtils.getString(R.string.message_error_session_null));
    }
}
