package com.example.fitnessfactory.ui.activities.editors;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.GymIdUpdateEvent;
import com.example.fitnessfactory.data.events.SessionIdUpdateEvent;
import com.example.fitnessfactory.databinding.ActivityGymEditorBinding;
import com.example.fitnessfactory.ui.adapters.PersonnelPageAdapter;
import com.example.fitnessfactory.ui.viewmodels.editors.GymEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.GymEditorViewModelFactory;
import com.example.fitnessfactory.utils.EventUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.google.android.material.tabs.TabLayoutMediator;

import org.greenrobot.eventbus.EventBus;

public class GymEditorActivity extends TabParentEditorActivity<GymIdUpdateEvent> {

    private String gymId;
    private GymEditorViewModel viewModel;
    private ActivityGymEditorBinding binding;
    private PersonnelPageAdapter pageAdapter;

    @Override
    public Toolbar getToolbar() {
        return binding.toolbar;
    }

    @Override
    protected GymEditorViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void initActivity() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gym_editor);
        viewModel = new ViewModelProvider(this, new GymEditorViewModelFactory()).get(GymEditorViewModel.class);
        super.initActivity();
        binding.setModel(viewModel);

        viewModel.setGymData(gymId);
        subscribeForGymIdChangesForTabs();

        pageAdapter = new PersonnelPageAdapter(getSupportFragmentManager(), getLifecycle());
        binding.container.vpPersonnel.setAdapter(pageAdapter);
        new TabLayoutMediator(binding.container.tlPersonnel, binding.container.vpPersonnel,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(ResUtils.getString(R.string.title_admins));
                            break;
                        case 1:
                            tab.setText(ResUtils.getString(R.string.title_coaches));
                            break;
                    }
                }
        ).attach();
        binding.container.vpPersonnel.setUserInputEnabled(false);
    }

    private void subscribeForGymIdChangesForTabs() {
        viewModel.getGymId()
                .observe(this, gymId -> EventBus.getDefault().postSticky(new GymIdUpdateEvent(gymId)));
    }

    @Override
    protected boolean isNewEntity() {
        return StringUtils.isEmpty(gymId);
    }

    @Override
    protected void initEntityKey() {
        gymId = getIntent().getExtras().getString(AppConsts.GYM_ID_EXTRA);
    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_gym);
    }

    @Override
    protected boolean isDataValid() {
        return !StringUtils.isEmpty(binding.container.edtName.getText())
                && !StringUtils.isEmpty(binding.container.edtAddress.getText());
    }

    @Override
    protected Class<GymIdUpdateEvent> getEventType() {
        return GymIdUpdateEvent.class;
    }
}
