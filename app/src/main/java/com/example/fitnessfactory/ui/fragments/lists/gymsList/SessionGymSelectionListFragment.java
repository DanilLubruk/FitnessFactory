package com.example.fitnessfactory.ui.fragments.lists.gymsList;

import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.ui.activities.editors.session.SessionEditorViewModelFactoryProvider;
import com.example.fitnessfactory.ui.viewmodels.editors.SessionEditorViewModel;

public class SessionGymSelectionListFragment extends GymsListFragment {

    private SessionEditorViewModel editorViewModel;

    @Override
    protected void defineViewModel() {
        super.defineViewModel();
        FFApp.get().getAppComponent().inject(this);
        editorViewModel = new ViewModelProvider(
                this,
                SessionEditorViewModelFactoryProvider.getFactory())
                .get(SessionEditorViewModel.class);
    }

    @Override
    protected void sendSelectResult(Gym gym) {
        editorViewModel.setGym(gym).observe(this, isSet -> {
            if (isSet) {
                closeFragment();
            }
        });
    }
}
