package com.example.fitnessfactory.ui.fragments.lists.gymPersonnelList;

import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.GymCoachesListListenerEvent;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.viewmodels.editors.GymEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.AdminsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachesListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.GymEditorViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.gymPersonnelList.GymAdminsListTabViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.gymPersonnelList.GymCoachesListTabViewModel;
import com.example.fitnessfactory.utils.ResUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

public class GymCoachesListTabFragment extends GymPersonnelListTabFragment {

    private GymCoachesListTabViewModel viewModel;

    private GymEditorViewModel editorViewModel;

    @Inject
    GymEditorViewModelFactory gymEditorViewModelFactory;

    @Override
    protected void defineViewModel() {
        FFApp.get().getAppComponent().inject(this);
        editorViewModel = new ViewModelProvider(this, gymEditorViewModelFactory).get(GymEditorViewModel.class);
        viewModel = new ViewModelProvider(this, new CoachesListTabViewModelFactory()).get(GymCoachesListTabViewModel.class);
    }

    @Override
    protected GymCoachesListTabViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected String getSingularPersonnelCaption() {
        return ResUtils.getString(R.string.caption_coach);
    }

    @Override
    protected int getSelectionFragmentId() {
        return AppConsts.FRAGMENT_GYMS_COACHES_ID;
    }

    @Override
    protected String getPersonnelEmailExtraKey() {
        return AppConsts.COACH_EMAIL_EXTRA;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGymCoachesListListenerEvent(GymCoachesListListenerEvent gymCoachesListListenerEvent) {
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
