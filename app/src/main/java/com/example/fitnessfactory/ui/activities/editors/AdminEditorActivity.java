package com.example.fitnessfactory.ui.activities.editors;

import androidx.lifecycle.ViewModelProvider;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.events.AdminGymsListListenerEvent;
import com.example.fitnessfactory.ui.viewmodels.editors.AdminEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.AdminEditorViewModelFactory;
import com.example.fitnessfactory.utils.ResUtils;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AdminEditorActivity extends PersonnelEditorActivity {

    private AdminEditorViewModel viewModel;

    @Override
    protected AdminEditorViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected String getDeleteGymMessage() {
        return String.format(ResUtils.getString(R.string.message_ask_remove_personnel_from_gym), ResUtils.getString(R.string.caption_admin));
    }

    @Override
    public String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_admin);
    }

    @Override
    public void initActivity() {
        viewModel = new ViewModelProvider(this, new AdminEditorViewModelFactory()).get(AdminEditorViewModel.class);
        super.initActivity();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAdminEditorGymsListenerEvent(AdminGymsListListenerEvent adminGymsListListenerEvent) {
        viewModel.getGymsData();
    }
}


