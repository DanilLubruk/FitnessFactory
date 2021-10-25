package com.example.fitnessfactory.ui.activities.editors;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.observers.SingleDialogEvent;
import com.example.fitnessfactory.databinding.ActivitySessionEditorBinding;
import com.example.fitnessfactory.ui.viewmodels.editors.EditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.editors.SessionEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.SessionEditorViewModelFactory;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.example.fitnessfactory.utils.TimeUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;

import java.util.Date;
import java.util.List;

public class SessionEditorActivity extends EditorActivity {

    private String id;
    private SessionEditorViewModel viewModel;
    private ActivitySessionEditorBinding binding;

    @Override
    public Toolbar getToolbar() {
        return binding.toolbar;
    }

    @Override
    public void initActivity() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_session_editor);
        viewModel = new ViewModelProvider(this, new SessionEditorViewModelFactory()).get(SessionEditorViewModel.class);
        super.initActivity();
        binding.setModel(viewModel);
        binding.container.edtDate.setOnClickListener(view -> trySelectDate());
        binding.container.edtStartTime.setOnClickListener(view -> trySelectStartTime());
        viewModel.getSession(id)
                .observe(this, isObtained -> {
                    if (isObtained && isNewEntity()) {
                        Date defaultDate = getIntentDefaultDate();
                        viewModel.setSessionDate(defaultDate);
                        viewModel.setSessionStartTime(defaultDate);
                    }
                });
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
        return true;
    }
}
