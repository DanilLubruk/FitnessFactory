package com.example.fitnessfactory.ui.activities.editors;

import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.ui.adapters.CoachGymsPageAdapter;
import com.example.fitnessfactory.ui.viewmodels.editors.CoachEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachEditorViewModelFactory;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.android.material.tabs.TabLayoutMediator;

public class CoachEditorActivity extends PersonnelEditorActivity {

    private CoachEditorViewModel viewModel;

    @Override
    protected CoachEditorViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected FragmentStateAdapter getPageAdapter() {
        return new CoachGymsPageAdapter(getSupportFragmentManager(), getLifecycle());
    }

    @Override
    public String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_coach);
    }

    @Override
    public void initActivity() {
        viewModel = new ViewModelProvider(this, new CoachEditorViewModelFactory()).get(CoachEditorViewModel.class);
        super.initActivity();
    }

    @Override
    protected TabLayoutMediator.TabConfigurationStrategy getTabsList() {
        return (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(ResUtils.getString(R.string.title_gyms));
                    break;
                case 1:
                    tab.setText(ResUtils.getString(R.string.title_sessions));
                    break;
            }
        };
    }
}
