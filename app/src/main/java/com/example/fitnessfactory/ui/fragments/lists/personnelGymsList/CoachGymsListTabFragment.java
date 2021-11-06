package com.example.fitnessfactory.ui.fragments.lists.personnelGymsList;

import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.data.events.CoachGymsListListenerEvent;
import com.example.fitnessfactory.data.events.PersonnelEmailUpdateEvent;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachGymsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelGymList.CoachGymsListTabViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelGymList.PersonnelGymsListTabViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CoachGymsListTabFragment extends PersonnelGymsListTabFragment {

    private CoachGymsListTabViewModel viewModel;

    @Override
    protected void defineViewModel() {
        viewModel = new ViewModelProvider(this, new CoachGymsListTabViewModelFactory()).get(CoachGymsListTabViewModel.class);
    }

    @Override
    protected PersonnelGymsListTabViewModel getViewModel() {
        return viewModel;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCoachEditorGymsListenerEvent(CoachGymsListListenerEvent coachGymsListListenerEvent) {
        getViewModel().getGymsData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPersonnelEmailUpdateEvent(PersonnelEmailUpdateEvent personnelEmailUpdateEvent) {
        viewModel.resetPersonnelEmail(personnelEmailUpdateEvent.getPersonnelEmail());
        viewModel.startDataListener();
    }
}
