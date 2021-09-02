package com.example.fitnessfactory.ui.viewmodels.lists;

import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.dataListeners.CoachesListDataListener;
import com.example.fitnessfactory.data.managers.access.CoachesAccessManager;
import com.example.fitnessfactory.data.managers.data.CoachesDataManager;
import com.example.fitnessfactory.data.models.AppUser;

import java.util.List;

import javax.inject.Inject;


public class CoachesListViewModel extends PersonnelListViewModel {

    @Inject
    CoachesAccessManager accessManager;
    @Inject
    CoachesDataManager dataManager;
    @Inject
    CoachesListDataListener dataListener;

    private MutableLiveData<List<AppUser>> coaches = new MutableLiveData<>();

    public CoachesListViewModel() {
        FFApp.get().getAppComponent().inject(this);
    }

    protected CoachesAccessManager getAccessManager() {
        return accessManager;
    }

    protected CoachesDataManager getDataManager() {
        return dataManager;
    }

    protected CoachesListDataListener getDataListener() {
        return dataListener;
    }
}
