package com.example.fitnessfactory.ui.activities.editors;

import static com.example.fitnessfactory.data.ActivityRequestCodes.REQUEST_GYM_ID;
import static com.example.fitnessfactory.data.ActivityRequestCodes.REQUEST_SESSION_TYPE;

import android.content.Intent;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.observers.SingleDialogEvent;
import com.example.fitnessfactory.databinding.ActivitySessionEditorBinding;
import com.example.fitnessfactory.ui.adapters.SessionPageAdapter;
import com.example.fitnessfactory.ui.fragments.FragmentProvider;
import com.example.fitnessfactory.ui.fragments.lists.ListListenerSelectFragment;
import com.example.fitnessfactory.ui.viewmodels.editors.SessionEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.SessionEditorViewModelFactory;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class SessionEditorActivity extends EditorActivity {

    private String id;
    private SessionEditorViewModel viewModel;
    private ActivitySessionEditorBinding binding;
    private SessionPageAdapter pageAdapter;

    @Inject
    SessionEditorViewModelFactory sessionEditorViewModelFactory;

    @Override
    public Toolbar getToolbar() {
        return binding.toolbar;
    }

    @Override
    public void initActivity() {
        FFApp.get().getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_session_editor);
        viewModel = new ViewModelProvider(this, sessionEditorViewModelFactory).get(SessionEditorViewModel.class);
        super.initActivity();
        binding.setModel(getViewModel());
        binding.content.edtDate.setOnClickListener(view -> trySelectDate());
        binding.content.edtStartTime.setOnClickListener(view -> trySelectStartTime());
        binding.content.edtEndTime.setOnClickListener(view -> trySelectEndTime());
        binding.content.edtGym.setOnClickListener(view -> showGymSelectorActivity());
        binding.content.edtSessionType.setOnClickListener(view -> showSessionTypeSelectorActivity());
        getViewModel().getSession(getSessionId())
                .observe(this, isObtained -> {
                    if (isObtained && isNewEntity()) {
                        getViewModel().setSessionDefaultTime(getIntentDefaultDate());
                    }
                });
        pageAdapter = new SessionPageAdapter(getSupportFragmentManager(), getLifecycle());
        binding.content.vpParticipants.setAdapter(pageAdapter);
        new TabLayoutMediator(binding.content.tlParticipants, binding.content.vpParticipants,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(ResUtils.getString(R.string.title_clients));
                            break;
                        case 1:
                            tab.setText(ResUtils.getString(R.string.title_coaches));
                            break;
                    }
                }
        ).attach();
        binding.content.vpParticipants.setUserInputEnabled(false);
    }

    private Date getIntentDefaultDate() {
        long time = getIntent().getLongExtra(AppConsts.SESSION_DATE, AppConsts.UNDEFINED_VALUE);

        return time != AppConsts.UNDEFINED_VALUE ? new Date(time) : new Date();
    }

    private void trySelectDate() {
        getViewModel().changeSessionDate(new SingleDialogEvent<>(this, DialogUtils::showDateSelectDialog));
    }

    private void trySelectStartTime() {
        getViewModel().changeSessionStartTime(new SingleDialogEvent<>(this, DialogUtils::showTimePickerDialog));
    }

    private void trySelectEndTime() {
        getViewModel().changeSessionEndTime(new SingleDialogEvent<>(this, DialogUtils::showTimePickerDialog));
    }

    private void showGymSelectorActivity() {
        getIntent().putExtra(AppConsts.IS_SELECT_MODE_EXTRA, true);
        FragmentProvider.attachFragment(this, AppConsts.FRAGMENT_SESSIONS_GYMS_ID);
    }

    private void showSessionTypeSelectorActivity() {
        getIntent().putExtra(AppConsts.IS_SELECT_MODE_EXTRA, true);
        FragmentProvider.attachFragment(this, AppConsts.FRAGMENT_SESSION_TYPES_ID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            GuiUtils.showMessage(ResUtils.getString(R.string.message_selection_failed));
            return;
        }
        getViewModel().getSession(getSessionId())
                .observe(this, isObtained -> {
                    if (isObtained && isNewEntity()) {
                        getViewModel().setSessionDefaultTime(getIntentDefaultDate());
                    }
                    if (!isObtained) {
                        return;
                    }
                    switch (requestCode) {
                        case REQUEST_GYM_ID:

                            String gymId = data.getStringExtra(AppConsts.GYM_ID_EXTRA);
                            getViewModel().setGym(gymId);
                            break;
                        case REQUEST_SESSION_TYPE:
                            String sessionTypeId = data.getStringExtra(AppConsts.SESSION_TYPE_ID_EXTRA);
                            getViewModel().setSessionType(sessionTypeId);
                            break;
                    }
                });

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected SessionEditorViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected boolean isNewEntity() {
        return StringUtils.isEmpty(id);
    }

    @Override
    protected void initEntityKey() {
        id = getIntent().getStringExtra(AppConsts.SESSION_ID_EXTRA);
    }

    private String getSessionId() {
        return getIntent().getStringExtra(AppConsts.SESSION_ID_EXTRA);
    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_session);
    }

    @Override
    protected boolean isDataValid() {
        return !StringUtils.isEmpty(binding.content.edtDate.getText())
                && !StringUtils.isEmpty(binding.content.edtStartTime.getText())
                && !StringUtils.isEmpty(binding.content.edtEndTime.getText())
                && !StringUtils.isEmpty(binding.content.edtGym.getText())
                && !StringUtils.isEmpty(binding.content.edtSessionType.getText());
    }

    @Override
    protected void close() {
        FFApp.get().initAppComponent();
        super.close();
    }
}
