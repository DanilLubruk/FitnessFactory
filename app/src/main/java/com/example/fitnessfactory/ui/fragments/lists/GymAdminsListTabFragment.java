package com.example.fitnessfactory.ui.fragments.lists;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.GymAdminsListListenerEvent;
import com.example.fitnessfactory.data.events.GymIdUpdateEvent;
import com.example.fitnessfactory.data.events.SessionIdUpdateEvent;
import com.example.fitnessfactory.ui.viewmodels.factories.AdminsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.GymAdminsListTabViewModel;
import com.example.fitnessfactory.utils.ResUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class GymAdminsListTabFragment extends GymPersonnelListTabFragment {

    private GymAdminsListTabViewModel viewModel;

    @Override
    protected void defineViewModel() {
        viewModel = new ViewModelProvider(this, new AdminsListTabViewModelFactory()).get(GymAdminsListTabViewModel.class);
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
        return AppConsts.FRAGMENT_ADMINS_ID;
    }

    @Override
    protected String getPersonnelEmailExtraKey() {
        return AppConsts.ADMIN_EMAIL_EXTRA;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGymAdminsListListenerEvent(GymAdminsListListenerEvent gymAdminsListListenerEvent) {
        viewModel.getPersonnelData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGymIdUpdateEvent(GymIdUpdateEvent gymIdUpdateEvent) {
        getViewModel().resetGymId(gymIdUpdateEvent.getGymId());
        getViewModel().startDataListener();
    }
}
