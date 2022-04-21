package com.example.fitnessfactory.ui.fragments.lists.gymPersonnelList;

import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.GymAdminsListListenerEvent;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.ui.activities.editors.gym.GymEditorViewModelFactoryProvider;
import com.example.fitnessfactory.ui.viewmodels.editors.GymEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.AdminsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.GymEditorViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.gymPersonnelList.GymAdminsListTabViewModel;
import com.example.fitnessfactory.utils.ResUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

public class GymAdminsListTabFragment extends GymPersonnelListTabFragment {

    private GymAdminsListTabViewModel viewModel;

    private GymEditorViewModel editorViewModel;

    @Override
    protected void defineViewModel() {
        FFApp.get().getAppComponent().inject(this);
        viewModel = new ViewModelProvider(this, new AdminsListTabViewModelFactory()).get(GymAdminsListTabViewModel.class);
        editorViewModel = new ViewModelProvider(
                this,
                GymEditorViewModelFactoryProvider.getFactory())
                .get(GymEditorViewModel.class);
    }

    @Override
    protected GymAdminsListTabViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected String getSingularPersonnelCaption() {
        return ResUtils.getString(R.string.caption_admin);
    }

    @Override
    protected int getSelectionFragmentId() {
        return AppConsts.FRAGMENT_GYMS_ADMINS_ID;
    }

    @Override
    protected String getPersonnelEmailExtraKey() {
        return AppConsts.ADMIN_EMAIL_EXTRA;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGymAdminsListListenerEvent(GymAdminsListListenerEvent gymAdminsListListenerEvent) {
        editorViewModel.getGymId().observe(this, gymId -> viewModel.getPersonnelData(gymId));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getViewModel() != null) {
            editorViewModel.getGymId().observe(this, gymId -> getViewModel().startDataListener(gymId));
        } else {
            closeProgress();
        }
    }

    protected void deleteItem(AppUser admin) {
        editorViewModel.getGymId().observe(this, gymId -> getViewModel().deleteItem(gymId, admin));
    }
}
