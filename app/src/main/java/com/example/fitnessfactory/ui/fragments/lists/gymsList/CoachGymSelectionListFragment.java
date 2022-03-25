package com.example.fitnessfactory.ui.fragments.lists.gymsList;

import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.ui.viewmodels.editors.CoachEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.AdminGymsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachEditorViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachGymsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachesListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelGymList.AdminGymsListTabViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelGymList.CoachGymsListTabViewModel;

import javax.inject.Inject;

public class CoachGymSelectionListFragment extends GymsListFragment {

    private CoachGymsListTabViewModel tabViewModel;

    private CoachEditorViewModel editorViewModel;

    @Inject
    CoachEditorViewModelFactory coachEditorViewModelFactory;

    @Override
    protected void defineViewModel() {
        super.defineViewModel();
        FFApp.get().getAppComponent().inject(this);
        tabViewModel = new ViewModelProvider(this, new CoachGymsListTabViewModelFactory()).get(CoachGymsListTabViewModel.class);
        editorViewModel = new ViewModelProvider(this, coachEditorViewModelFactory).get(CoachEditorViewModel.class);
    }

    @Override
    protected void sendSelectResult(Gym gym) {
        editorViewModel.personnelEmail.observe(this, email -> {
            tabViewModel.addGym(email, gym.getId());
            closeFragment();
        });
    }
}
