package com.example.fitnessfactory.ui.viewmodels;

import android.os.Bundle;

import androidx.databinding.ObservableField;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.bondingRepositories.GymAccessRepository;
import com.example.fitnessfactory.data.repositories.GymRepository;
import com.example.fitnessfactory.utils.RxUtils;

import javax.inject.Inject;

public class GymEditorViewModel extends EditorViewModel {

    @Inject
    GymRepository gymRepository;
    @Inject
    GymAccessRepository gymAccessRepository;

    private Gym dbGym;
    public ObservableField<Gym> gym = new ObservableField<>();

    private final String ID_KEY = "ID_KEY";
    private final String NAME_KEY = "NAME_KEY";
    private final String ADDRESS_KEY = "ADDRESS_KEY";

    public GymEditorViewModel() {
        FFApp.get().getAppComponent().inject(this);
    }

    public SingleLiveEvent<Gym> getGym(String id) {
        SingleLiveEvent<Gym> observer = new SingleLiveEvent<>();

        subscribeInIOThread(gymRepository.getGymAsync(id),
                new SingleData<>(
                        observer::setValue,
                        RxUtils::handleError));

        return observer;
    }

    public void setGym(Gym gym) {
        if (gym == null) {
            return;
        }
        if (dbGym == null) {
            dbGym = new Gym();
            dbGym.setName(gym.getName());
            dbGym.setAddress(gym.getAddress());
        }

        if (hasHandle()) {
            setHandleState(gym);
            setDbGymState();
        }

        this.gym.set(gym);
    }

    @Override
    public SingleLiveEvent<Boolean> isModified() {
        SingleLiveEvent<Boolean> observer = new SingleLiveEvent<>();
        observer.setValue(false);

        Gym gym = this.gym.get();
        if (gym != null &&
                gym.getName() != null &&
                gym.getAddress() != null) {
            boolean isModified =
                    !gym.getName().equals(dbGym.getName()) ||
                    !gym.getAddress().equals(dbGym.getAddress());

            observer.setValue(isModified);
        }

        return observer;
    }

    @Override
    public SingleLiveEvent<Boolean> save() {
        SingleLiveEvent<Boolean> observer = new SingleLiveEvent<>();

        subscribeInIOThread(gymRepository.saveAsync(gym.get()),
                new SingleData<>(
                        id -> {
                            this.gym.get().setId(id);
                            observer.setValue(true);
                        },
                        throwable -> RxUtils.handleError(observer, throwable)
                ));

        return observer;
    }

    @Override
    public SingleLiveEvent<Boolean> delete() {
        SingleLiveEvent<Boolean> observer = new SingleLiveEvent<>();

        subscribeInIOThread(gymAccessRepository.deleteGymSingle(gym.get()),
                new SingleData<>(observer::setValue, RxUtils::handleError));

        return observer;
    }

    @Override
    public void saveState(Bundle savedState) {
        super.saveState(savedState);
        saveGymState();
        saveGymDbState();
    }

    private void saveGymState() {
        Gym gym = this.gym.get();
        if (gym == null) {
            return;
        }
        getHandle().put(ID_KEY, gym.getId());
        getHandle().put(NAME_KEY, gym.getName());
        getHandle().put(ADDRESS_KEY, gym.getAddress());
    }

    private void saveGymDbState() {
        if (dbGym == null) {
            return;
        }
        getHandle().put(Gym.ID_FIELD, dbGym.getId());
        getHandle().put(Gym.NAME_FILED, dbGym.getName());
        getHandle().put(Gym.ADDRESS_FIELD, dbGym.getAddress());
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

    private void setDbGymState() {
        if (dbGym == null) {
            return;
        }
        dbGym.setId((String) getHandle().get(Gym.ID_FIELD));
        dbGym.setName((String) getHandle().get(Gym.NAME_FILED));
        dbGym.setAddress((String) getHandle().get(Gym.ADDRESS_FIELD));
    }
}
