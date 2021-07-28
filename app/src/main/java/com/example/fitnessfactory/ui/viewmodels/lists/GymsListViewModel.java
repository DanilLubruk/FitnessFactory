package com.example.fitnessfactory.ui.viewmodels.lists;

import androidx.lifecycle.LiveData;
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

    public void refreshGymsData() {
        subscribeInIOThread(gymRepository.getGymsAsync(),
                new SingleData<>(gyms -> gymsList.setValue(gyms),
                        throwable -> {
                            throwable.printStackTrace();
                            GuiUtils.showMessage(throwable.getLocalizedMessage());
                        }));
    }

    public LiveData<List<Gym>> getGymsList() {
        if (gymsList.getValue() == null) {
            refreshGymsData();
        }

        return gymsList;
    }
}
