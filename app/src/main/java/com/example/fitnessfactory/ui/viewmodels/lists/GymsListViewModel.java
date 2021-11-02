package com.example.fitnessfactory.ui.viewmodels.lists;

import com.example.fitnessfactory.data.dataListeners.GymsListDataListener;
import com.example.fitnessfactory.data.managers.access.GymsAccessManager;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import javax.inject.Inject;

public class GymsListViewModel extends BaseViewModel implements DataListListener<Gym> {

    private GymsAccessManager gymsAccessManager;
    private GymsListDataListener gymsListDataListener;

    @Inject
    public GymsListViewModel(GymsAccessManager gymsAccessManager,
                             GymsListDataListener gymsListDataListener) {
        this.gymsAccessManager = gymsAccessManager;
        this.gymsListDataListener = gymsListDataListener;
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

        subscribeInIOThread(gymsAccessManager.deleteGymCompletable(gym.getId()));
    }
}
