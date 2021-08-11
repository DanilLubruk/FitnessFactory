package com.example.fitnessfactory.ui.viewmodels.lists;

import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.repositories.bondingRepositories.GymAccessRepository;
import com.example.fitnessfactory.data.repositories.GymRepository;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

public class GymsListViewModel extends BaseViewModel {

    @Inject
    GymRepository gymRepository;
    @Inject
    GymAccessRepository gymAccessRepository;

    private MutableLiveData<List<Gym>> gymsList = new MutableLiveData<>();

    public GymsListViewModel() {
        FFApp.get().getAppComponent().inject(this);
    }

    public void addGymsListDataListener() {
        subscribeInIOThread(gymRepository.addGymsListListener());
    }

    public void removeGymsListDataListener() {
        subscribeInIOThread(gymRepository.removeGymsListListener());
    }

    public void deleteGym(Gym gym) {
        subscribeInIOThread(gymAccessRepository.deleteGymCompletable(gym), this::handleError);
    }
}
