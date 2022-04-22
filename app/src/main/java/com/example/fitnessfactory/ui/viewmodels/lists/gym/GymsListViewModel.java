package com.example.fitnessfactory.ui.viewmodels.lists.gym;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.dataListeners.GymsListDataListener;
import com.example.fitnessfactory.data.managers.access.GymsAccessManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.ui.viewmodels.BaseViewModel;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.ui.viewmodels.lists.ListViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.SearchFieldState;
import com.example.fitnessfactory.ui.viewmodels.lists.SearchViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;

public class GymsListViewModel extends SearchViewModel<Gym, SearchFieldState<Gym>> {

    private final GymsAccessManager gymsAccessManager;
    private final GymsListDataListener gymsListDataListener;

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
            handleItemDeletingNullError();
            return;
        }

        subscribeInIOThread(gymsAccessManager.deleteGymCompletable(gym));
    }

    @Override
    protected String getItemNullClause() {
        return getErrorMessageBreak().concat(ResUtils.getString(R.string.message_error_gym_null));
    }

    @Override
    protected SearchFieldState<Gym> getDefaultSearchField() {
        return new GymNameSearchField();
    }
}
