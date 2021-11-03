package com.example.fitnessfactory.ui.viewmodels.editors;

import android.os.Bundle;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.managers.access.GymsAccessManager;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.observers.SingleLiveEvent;
import com.example.fitnessfactory.data.repositories.ownerData.OwnerGymRepository;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;

public class GymEditorViewModel extends EditorViewModel {

    private final OwnerGymRepository ownerGymRepository;
    private final GymsAccessManager gymsAccessManager;

    private Gym dbGym;
    public final ObservableField<Gym> gym = new ObservableField<>();
    private final MutableLiveData<String> gymId = new MutableLiveData<>();

    private final String DB_ID_KEY = "DB_ID_KEY";
    private final String DB_NAME_KEY = "DB_NAME_KEY";
    private final String DB_ADDRESS_KEY = "DB_ADDRESS_KEY";

    @Inject
    public GymEditorViewModel(OwnerGymRepository ownerGymRepository,
                              GymsAccessManager gymsAccessManager) {
        this.ownerGymRepository = ownerGymRepository;
        this.gymsAccessManager = gymsAccessManager;
    }

    public void getGymData(String gymId) {
        subscribeInIOThread(ownerGymRepository.getGymAsync(gymId),
                new SingleData<>(
                        this::setGym,
                        getErrorHandler()::handleError));
    }

    public MutableLiveData<String> getGymId() {
        return gymId;
    }

    private void setGym(Gym gym) {
        if (gym == null) {
            gym = new Gym();
        }
        if (dbGym == null) {
            dbGym = new Gym();
            dbGym.copy(gym);
        }
        if (hasHandle()) {
            setHandleState(gym);
            setDbGymState();
        }

        this.gym.set(gym);
    }

    @Override
    public SingleLiveEvent<Boolean> isModified() {
        SingleLiveEvent<Boolean> isModified = new SingleLiveEvent<>();

        Gym gym = this.gym.get();
        if (Gym.isNotNull(gym)) {
            isModified.setValue(!gym.equals(dbGym));
        } else {
            isModified.setValue(false);
        }

        return isModified;
    }

    @Override
    public SingleLiveEvent<Boolean> save() {
        SingleLiveEvent<Boolean> observer = new SingleLiveEvent<>();

        Gym gym = this.gym.get();
        if (gym == null) {
            return handleItemSavingNullError(observer);
        }

        subscribeInIOThread(ownerGymRepository.saveAsync(gym),
                new SingleData<>(
                        isSaved -> {
                            dbGym.copy(gym);
                            gymId.setValue(gym.getId());
                            observer.setValue(isSaved);
                        },
                        throwable -> getErrorHandler().handleError(observer, throwable)
                ));

        return observer;
    }

    @Override
    protected String getItemNullClause() {
        return getErrorMessageBreak()
                .concat(ResUtils.getString(R.string.message_error_gym_null));
    }

    @Override
    public SingleLiveEvent<Boolean> delete() {
        SingleLiveEvent<Boolean> observer = new SingleLiveEvent<>();

        Gym gym = this.gym.get();
        if (gym == null) {
            return handleItemDeletingNullError(observer);
        }

        subscribeInIOThread(gymsAccessManager.deleteGymSingle(gym.getId()),
                new SingleData<>(
                        observer::setValue,
                        throwable -> getErrorHandler().handleError(observer, throwable)));

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
        getHandle().put(DB_ID_KEY, gym.getId());
        getHandle().put(DB_NAME_KEY, gym.getName());
        getHandle().put(DB_ADDRESS_KEY, gym.getAddress());
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
            gym = new Gym();
        }

        gym.setId((String) getHandle().get(DB_ID_KEY));
        gym.setName((String) getHandle().get(DB_NAME_KEY));
        gym.setAddress((String) getHandle().get(DB_ADDRESS_KEY));
    }

    private void setDbGymState() {
        if (dbGym == null) {
            dbGym = new Gym();
        }

        dbGym.setId((String) getHandle().get(Gym.ID_FIELD));
        dbGym.setName((String) getHandle().get(Gym.NAME_FILED));
        dbGym.setAddress((String) getHandle().get(Gym.ADDRESS_FIELD));
    }
}
