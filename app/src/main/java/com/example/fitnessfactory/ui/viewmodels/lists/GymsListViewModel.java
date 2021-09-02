package com.example.fitnessfactory.ui.viewmodels.lists;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.dataListeners.GymsListDataListener;
import com.example.fitnessfactory.data.managers.access.GymsAccessManager;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.utils.RxUtils;
import javax.inject.Inject;

public class GymsListViewModel extends BaseViewModel implements DataListListener<Gym> {

    @Inject
    OwnerGymRepository ownerGymRepository;
    @Inject
    GymsAccessManager gymsAccessManager;
    @Inject
    GymsListDataListener gymsListDataListener;

    public GymsListViewModel() {
        FFApp.get().getAppComponent().inject(this);
    }

    public void startDataListener() {
        gymsListDataListener.startDataListener();
    }

    public void stopDataListener() {
        gymsListDataListener.stopDataListener();
    }

    public void deleteItem(Gym gym) {
        if (gym == null) {
            return;
        }

        subscribeInIOThread(gymsAccessManager.deleteGymCompletable(gym.getId()), RxUtils::handleError);
    }
}
