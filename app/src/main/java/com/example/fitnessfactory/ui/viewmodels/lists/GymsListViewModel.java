package com.example.fitnessfactory.ui.viewmodels.lists;
import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.dataListeners.GymsListDataListener;
import com.example.fitnessfactory.data.managers.GymsAccessManager;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.repositories.GymRepository;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.utils.RxUtils;
import javax.inject.Inject;

public class GymsListViewModel extends BaseViewModel {

    @Inject
    GymRepository gymRepository;
    @Inject
    GymsAccessManager gymsAccessManager;
    @Inject
    GymsListDataListener gymsListDataListener;

    public GymsListViewModel() {
        FFApp.get().getAppComponent().inject(this);
    }

    public void addGymsListDataListener() {
        gymsListDataListener.setGymsListListener();
    }

    public void removeGymsListDataListener() {
        gymsListDataListener.removeDataListener();
    }

    public void deleteGym(Gym gym) {
        if (gym == null) {
            return;
        }

        subscribeInIOThread(gymsAccessManager.deleteGymCompletable(gym.getId()), RxUtils::handleError);
    }
}
