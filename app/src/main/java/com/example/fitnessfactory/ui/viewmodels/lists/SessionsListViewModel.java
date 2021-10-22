package com.example.fitnessfactory.ui.viewmodels.lists;

import com.example.fitnessfactory.data.dataListeners.SessionsCalendarDataListener;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;

import java.util.Date;

import javax.inject.Inject;

public class SessionsListViewModel extends BaseViewModel {

    private SessionsCalendarDataListener dataListener;

    @Inject
    public SessionsListViewModel(SessionsCalendarDataListener dataListener) {
        this.dataListener = dataListener;
    }

    public void startDataListener(Date startDate, Date endDate) {
        dataListener.startDataListener(startDate, endDate);
    }

    public void stopDataListener() {
        dataListener.stopDataListener();
    }

    public void deleteItem(Session item) {

    }
}
