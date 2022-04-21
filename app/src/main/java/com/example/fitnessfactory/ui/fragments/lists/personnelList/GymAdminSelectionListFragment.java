package com.example.fitnessfactory.ui.fragments.lists.personnelList;

import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.viewmodels.editors.GymEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.AdminsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.GymEditorViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.gymPersonnelList.GymAdminsListTabViewModel;

import javax.inject.Inject;

public class GymAdminSelectionListFragment extends AdminsListFragment {

    private GymAdminsListTabViewModel tabViewModel;

    private GymEditorViewModel editorViewModel;

    @Inject
    GymEditorViewModelFactory gymEditorViewModelFactory;

    @Override
    protected void defineViewModel() {
        super.defineViewModel();
        FFApp.get().getAppComponent().inject(this);
        tabViewModel = new ViewModelProvider(this, new AdminsListTabViewModelFactory()).get(GymAdminsListTabViewModel.class);
        editorViewModel = new ViewModelProvider(this, gymEditorViewModelFactory).get(GymEditorViewModel.class);
    }

    @Override
    protected void sendSelectResult(AppUser admin) {
        editorViewModel.getGymId().observe(this, gymId -> {
            tabViewModel.addPersonnelToGym(gymId, admin.getEmail());
            closeFragment();
        });
    }
}