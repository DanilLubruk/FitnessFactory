package com.example.fitnessfactory.ui.activities.editors.admin;

import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.events.AdminGymsListListenerEvent;
import com.example.fitnessfactory.ui.activities.editors.PersonnelEditorActivity;
import com.example.fitnessfactory.ui.activities.editors.session.SessionEditorViewModelFactoryProvider;
import com.example.fitnessfactory.ui.adapters.AdminGymsPageAdapter;
import com.example.fitnessfactory.ui.adapters.CoachGymsPageAdapter;
import com.example.fitnessfactory.ui.viewmodels.editors.AdminEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.AdminEditorViewModelFactory;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.android.material.tabs.TabLayoutMediator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

public class AdminEditorActivity extends PersonnelEditorActivity {

    private AdminEditorViewModel viewModel;

    @Override
    protected AdminEditorViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected FragmentStateAdapter getPageAdapter() {
        return new AdminGymsPageAdapter(getSupportFragmentManager(), getLifecycle());
    }

    @Override
    public String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_admin);
    }

    @Override
    public void initActivity() {
        FFApp.get().getAppComponent().inject(this);
        viewModel = new ViewModelProvider(
                this,
                AdminEditorViewModelFactoryProvider.getFactory())
                .get(AdminEditorViewModel.class);
        super.initActivity();
    }

    @Override
    protected String getTitleCaption() {
        return ResUtils.getString(R.string.caption_admin_capital);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AdminEditorViewModelFactoryProvider.clear();
    }
}


