package com.example.fitnessfactory.ui.viewmodels;

import android.os.Bundle;

import androidx.databinding.ObservableField;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.GymRepository;
import com.example.fitnessfactory.utils.GuiUtils;

import javax.inject.Inject;

public class GymEditorViewModel extends BaseViewModel {

    @Inject
    GymRepository gymRepository;
    public ObservableField<Gym> gym = new ObservableField<>();

    private final String ID_KEY = "ID";
    private final String NAME_KEY = "NAME";
    private final String ADDRESS_KEY = "ADDRESS";

    public GymEditorViewModel() {
        FFApp.get().getAppComponent().inject(this);
    }

    public SingleLiveEvent<Gym> getGym(String id) {
        SingleLiveEvent<Gym> observer = new SingleLiveEvent<>();

        subscribeInIOThread(gymRepository.getGymAsync(id),
                new SingleData<>(
                        observer::setValue,
                        this::handleError));

        return observer;
    }

    public void setGym(Gym gym) {
        if (gym == null) {
            return;
        }

        if (hasHandle()) {
            setHandleState(gym);
        }

        this.gym.set(gym);
    }

    private void handleError(Throwable throwable) {
        throwable.printStackTrace();
        GuiUtils.showMessage(throwable.getLocalizedMessage());
    }

    @Override
    public void saveState(Bundle savedState) {
        super.saveState(savedState);
        Gym gym = this.gym.get();
        if (gym == null) {
            return;
        }
        getHandle().put(ID_KEY, gym.getId());
        getHandle().put(NAME_KEY, gym.getName());
        getHandle().put(ADDRESS_KEY, gym.getAddress());
    }

    @Override
    public void restoreState(Bundle savedState) {
        super.restoreState(savedState);

    }

    private void setHandleState(Gym gym) {
        if (gym == null) {
            return;
        }
        gym.setId((String) getHandle().get(ID_KEY));
        gym.setName((String) getHandle().get(NAME_KEY));
        gym.setAddress((String) getHandle().get(ADDRESS_KEY));
    }
}
