package com.example.fitnessfactory.ui.viewmodels.lists;

import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.repositories.GymRepository;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.utils.GuiUtils;

import java.util.List;

import javax.inject.Inject;

public class GymsListViewModel extends BaseViewModel {

    @Inject
    GymRepository gymRepository;
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
        subscribeInIOThread(gymRepository.deleteCompletable(gym), this::handleError);
    }
}
