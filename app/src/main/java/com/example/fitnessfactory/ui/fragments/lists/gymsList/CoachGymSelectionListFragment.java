package com.example.fitnessfactory.ui.fragments.lists.gymsList;

import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.ui.activities.editors.coach.CoachEditorViewModelFactoryProvider;
import com.example.fitnessfactory.ui.viewmodels.editors.CoachEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachGymsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelGymList.CoachGymsListTabViewModel;

public class CoachGymSelectionListFragment extends GymsListFragment {

    private CoachGymsListTabViewModel tabViewModel;

    private CoachEditorViewModel editorViewModel;

    @Override
    protected void defineViewModel() {
        super.defineViewModel();
        FFApp.get().getAppComponent().inject(this);
        tabViewModel = new ViewModelProvider(this, new CoachGymsListTabViewModelFactory()).get(CoachGymsListTabViewModel.class);
        editorViewModel = new ViewModelProvider(
                this,
                CoachEditorViewModelFactoryProvider.getFactory())
                .get(CoachEditorViewModel.class);
    }

    @Override
    protected void sendSelectResult(Gym gym) {
        editorViewModel.personnelId.observe(this, id -> {
            tabViewModel.addGym(id, gym.getId());
            closeFragment();
        });
    }
}
