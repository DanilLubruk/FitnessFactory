package com.example.fitnessfactory.ui.viewmodels.lists;

import com.example.fitnessfactory.data.models.Personnel;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;

public class SessionCoachesListTabViewModel extends BaseViewModel implements DataListListener<Personnel> {

    private String sessionId;

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public void startDataListener() {

    }

    @Override
    public void stopDataListener() {

    }

    @Override
    public void deleteItem(Personnel item) {

    }
}
