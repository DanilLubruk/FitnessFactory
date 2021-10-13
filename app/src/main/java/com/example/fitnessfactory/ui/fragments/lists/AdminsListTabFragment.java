package com.example.fitnessfactory.ui.fragments.lists;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.GymAdminsListListenerEvent;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.viewmodels.factories.AdminsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.AdminsListTabViewModel;
import com.example.fitnessfactory.utils.ResUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AdminsListTabFragment extends PersonnelListTabFragment {

    private AdminsListTabViewModel viewModel;

    @Override
    protected void defineViewModel() {
        viewModel = new ViewModelProvider(this, new AdminsListTabViewModelFactory()).get(AdminsListTabViewModel.class);
    }

    @Override
    protected AdminsListTabViewModel getViewModel() {
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
}
