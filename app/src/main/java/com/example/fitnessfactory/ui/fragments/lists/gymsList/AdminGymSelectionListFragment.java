package com.example.fitnessfactory.ui.fragments.lists.gymsList;

import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.ui.activities.editors.admin.AdminEditorViewModelFactoryProvider;
import com.example.fitnessfactory.ui.viewmodels.editors.AdminEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.AdminGymsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelGymList.AdminGymsListTabViewModel;

public class AdminGymSelectionListFragment extends GymsListFragment {

    private AdminGymsListTabViewModel tabViewModel;

    private AdminEditorViewModel editorViewModel;

    @Override
    protected void defineViewModel() {
        super.defineViewModel();
        FFApp.get().getAppComponent().inject(this);
        tabViewModel = new ViewModelProvider(this, new AdminGymsListTabViewModelFactory()).get(AdminGymsListTabViewModel.class);
        editorViewModel = new ViewModelProvider(
                this,
                AdminEditorViewModelFactoryProvider.getFactory())
                .get(AdminEditorViewModel.class);
    }

    @Override
    protected void sendSelectResult(Gym gym) {
        editorViewModel.personnelId.observe(this, id -> {
            tabViewModel.addGym(id, gym.getId());
            closeFragment();
        });
    }
}
