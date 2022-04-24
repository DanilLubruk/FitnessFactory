package com.example.fitnessfactory.ui.fragments.lists.personnelList;

import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.activities.editors.gym.GymEditorViewModelFactoryProvider;
import com.example.fitnessfactory.ui.viewmodels.editors.GymEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachesListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.GymEditorViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.gymPersonnelList.GymCoachesListTabViewModel;

import javax.inject.Inject;

public class GymCoachSelectionListFragment extends CoachesListFragment {

    private GymCoachesListTabViewModel tabViewModel;

    private GymEditorViewModel editorViewModel;

    @Override
    protected void defineViewModel() {
        super.defineViewModel();
        FFApp.get().getAppComponent().inject(this);
        tabViewModel = new ViewModelProvider(this, new CoachesListTabViewModelFactory()).get(GymCoachesListTabViewModel.class);
        editorViewModel = new ViewModelProvider(
                this,
                GymEditorViewModelFactoryProvider.getFactory())
                .get(GymEditorViewModel.class);
    }

    @Override
    protected void sendSelectResult(AppUser admin) {
        editorViewModel.getGymId().observe(this, gymId -> {
            tabViewModel.addPersonnelToGym(gymId, admin.getId());
            closeFragment();
        });
    }
}
