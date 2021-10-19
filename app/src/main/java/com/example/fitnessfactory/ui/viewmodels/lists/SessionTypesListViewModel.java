package com.example.fitnessfactory.ui.viewmodels.lists;

import com.example.fitnessfactory.data.dataListeners.SessionTypesListDataListener;
import com.example.fitnessfactory.data.models.SessionType;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;

import javax.inject.Inject;

public class SessionTypesListViewModel extends BaseViewModel implements DataListListener<SessionType> {

    private final SessionTypesListDataListener dataListener;

    @Inject
    public SessionTypesListViewModel(SessionTypesListDataListener dataListener) {
        this.dataListener = dataListener;
    }

    @Override
    public void startDataListener() {
        dataListener.startDataListener();
    }

    @Override
    public void stopDataListener() {
        dataListener.stopDataListener();
    }

    @Override
    public void deleteItem(SessionType item) {

    }
}
