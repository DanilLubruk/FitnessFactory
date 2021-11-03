package com.example.fitnessfactory.ui.activities.editors;

import static com.example.fitnessfactory.data.ActivityRequestCodes.REQUEST_GYM_NAME;
import static com.example.fitnessfactory.data.ActivityRequestCodes.REQUEST_SESSION_TYPE;

import android.content.Intent;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.SessionIdUpdateEvent;
import com.example.fitnessfactory.data.observers.SingleDialogEvent;
import com.example.fitnessfactory.databinding.ActivitySessionEditorBinding;
import com.example.fitnessfactory.ui.activities.SelectionActivity;
import com.example.fitnessfactory.ui.adapters.PersonnelPageAdapter;
import com.example.fitnessfactory.ui.adapters.SessionPageAdapter;
import com.example.fitnessfactory.ui.viewmodels.editors.SessionEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.SessionEditorViewModelFactory;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;
import com.google.android.material.tabs.TabLayoutMediator;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

public class SessionEditorActivity extends EditorActivity {

    private String id;
    private SessionEditorViewModel viewModel;
    private ActivitySessionEditorBinding binding;
    private SessionPageAdapter pageAdapter;

    @Override
    public Toolbar getToolbar() {
        return binding.toolbar;
    }

    @Override
    public void initActivity() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_session_editor);
        viewModel = new ViewModelProvider(this, new SessionEditorViewModelFactory()).get(SessionEditorViewModel.class);
        super.initActivity();
        binding.setModel(getViewModel());
        binding.container.edtDate.setOnClickListener(view -> trySelectDate());
        binding.container.edtStartTime.setOnClickListener(view -> trySelectStartTime());
        binding.container.edtEndTime.setOnClickListener(view -> trySelectEndTime());
        binding.container.edtGym.setOnClickListener(view -> showGymSelectorActivity());
        binding.container.edtSessionType.setOnClickListener(view -> showSessionTypeSelectorActivity());
        getViewModel().getSession(id)
                .observe(this, isObtained -> {
                    if (isObtained && isNewEntity()) {
                        getViewModel().setSessionDefaultTime(getIntentDefaultDate());
                    }
                });
        subscribeForSessionIdChangesForTabs();
        pageAdapter = new SessionPageAdapter(getSupportFragmentManager(), getLifecycle());
        binding.container.vpParticipants.setAdapter(pageAdapter);
        new TabLayoutMediator(binding.container.tlParticipants, binding.container.vpParticipants,
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
    }

    private void subscribeForSessionIdChangesForTabs() {
        getViewModel().getSessionId()
                .observe(this, sessionId -> EventBus.getDefault().post(new SessionIdUpdateEvent(sessionId)));
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
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(AppConsts.FRAGMENT_ID_EXTRA, AppConsts.FRAGMENT_GYMS_ID);
        intent.putExtra(AppConsts.REQUEST_CODE, REQUEST_GYM_NAME);
        startActivityForResult(intent, REQUEST_GYM_NAME);
    }

    private void showSessionTypeSelectorActivity() {
        Intent intent = new Intent(this, SelectionActivity.class);
        intent.putExtra(AppConsts.FRAGMENT_ID_EXTRA, AppConsts.FRAGMENT_SESSION_TYPES_ID);
        startActivityForResult(intent, REQUEST_SESSION_TYPE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            GuiUtils.showMessage(ResUtils.getString(R.string.message_selection_failed));
            return;
        }

        switch (requestCode) {
            case REQUEST_GYM_NAME:
                String gymName = data.getStringExtra(AppConsts.GYM_NAME_EXTRA);
                getViewModel().setGym(gymName);
                break;
            case REQUEST_SESSION_TYPE:
                String sessionTypeName = data.getStringExtra(AppConsts.SESSION_TYPE_NAME_EXTRA);
                getViewModel().setSessionType(sessionTypeName);
                break;
        }
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

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_session);
    }

    @Override
    protected boolean isDataValid() {
        return !StringUtils.isEmpty(binding.container.edtDate.getText())
                && !StringUtils.isEmpty(binding.container.edtStartTime.getText())
                && !StringUtils.isEmpty(binding.container.edtEndTime.getText())
                && !StringUtils.isEmpty(binding.container.edtGym.getText())
                && !StringUtils.isEmpty(binding.container.edtSessionType.getText());
    }
}
