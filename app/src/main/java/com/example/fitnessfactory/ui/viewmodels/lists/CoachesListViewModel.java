package com.example.fitnessfactory.ui.viewmodels.lists;

import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.dataListeners.CoachesListDataListener;
import com.example.fitnessfactory.data.managers.CoachesDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.utils.RxUtils;

import java.util.List;

import javax.inject.Inject;

public class CoachesListViewModel extends BaseViewModel implements DataListListener<AppUser> {

    @Inject
    CoachesListDataListener dataListener;
    @Inject
    CoachesDataManager dataManager;

    private MutableLiveData<List<AppUser>> coaches = new MutableLiveData<>();

    public CoachesListViewModel() {
        FFApp.get().getAppComponent().inject(this);
    }

    public MutableLiveData<List<AppUser>> getCoaches() {
        return coaches;
    }

    public void getCoachesData() {
        subscribeInIOThread(
                dataManager.getCoachesListAsync(),
                new SingleData<>(coaches::setValue, RxUtils::handleError));
    }

    @Override
    public void startDataListener() {
        dataListener.startDataListener();
    }

    @Override
    public void stopDataListener() {
        dataListener.stopDataListener();
    }
}
