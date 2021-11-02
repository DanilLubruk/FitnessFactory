package com.example.fitnessfactory.ui.viewmodels.lists;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.dataListeners.DaysSessionsListDataListener;
import com.example.fitnessfactory.data.dataListeners.SessionsCalendarDataListener;
import com.example.fitnessfactory.data.managers.data.SessionsDataManager;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.Date;

import javax.inject.Inject;

public class SessionsListViewModel extends BaseViewModel {

    private final SessionsDataManager sessionsDataManager;
    private final SessionsCalendarDataListener calendarDataListener;
    private final DaysSessionsListDataListener daysSessionsDataListener;

    @Inject
    public SessionsListViewModel(SessionsDataManager sessionsDataManager,
                                 SessionsCalendarDataListener calendarDataListener,
                                 DaysSessionsListDataListener daysSessionsDataListener) {
        this.sessionsDataManager = sessionsDataManager;
        this.calendarDataListener = calendarDataListener;
        this.daysSessionsDataListener = daysSessionsDataListener;
    }

    public void startCalendarDataListener(Date startDate, Date endDate) {
        calendarDataListener.startDataListener(startDate, endDate);
    }

    public void stopCalendarDataListener() {
        calendarDataListener.stopDataListener();
    }

    public void startDaysSessionsDataListener(Date date) {
        daysSessionsDataListener.stopDataListener();
        daysSessionsDataListener.startDataListener(date);
    }

    public void stopDaysSessionsDataListener() {

    }

    public void deleteItem(Session item) {
        if (item == null) {
            GuiUtils.showMessage(ResUtils.getString(R.string.message_error_session_null));
            return;
        }

        subscribeInIOThread(sessionsDataManager.deleteSessionCompletable(item));
    }
}
