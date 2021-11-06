package com.example.fitnessfactory.ui.fragments.lists.personnelGymsList;

import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.data.events.AdminGymsListListenerEvent;
import com.example.fitnessfactory.data.events.PersonnelEmailUpdateEvent;
import com.example.fitnessfactory.ui.viewmodels.factories.AdminGymsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelGymList.AdminGymsListTabViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelGymList.PersonnelGymsListTabViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AdminGymsListTabFragment extends PersonnelGymsListTabFragment {

    private AdminGymsListTabViewModel viewModel;

    @Override
    protected void defineViewModel() {
        viewModel = new ViewModelProvider(this, new AdminGymsListTabViewModelFactory()).get(AdminGymsListTabViewModel.class);
    }

    @Override
    protected PersonnelGymsListTabViewModel getViewModel() {
        return viewModel;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAdminEditorGymsListenerEvent(AdminGymsListListenerEvent adminGymsListListenerEvent) {
        getViewModel().getGymsData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPersonnelEmailUpdateEvent(PersonnelEmailUpdateEvent personnelEmailUpdateEvent) {
        viewModel.resetPersonnelEmail(personnelEmailUpdateEvent.getPersonnelEmail());
        viewModel.startDataListener();
    }
}
