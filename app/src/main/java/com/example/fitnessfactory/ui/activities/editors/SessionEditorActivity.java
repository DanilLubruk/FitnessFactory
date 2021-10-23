package com.example.fitnessfactory.ui.activities.editors;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.databinding.ActivitySessionEditorBinding;
import com.example.fitnessfactory.ui.viewmodels.editors.EditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.editors.SessionEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.SessionEditorViewModelFactory;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;

public class SessionEditorActivity extends EditorActivity {

    private String id;
    private SessionEditorViewModel viewModel;
    private ActivitySessionEditorBinding binding;

    @Override
    public void initActivity() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_session_editor);
        viewModel = new ViewModelProvider(this, new SessionEditorViewModelFactory()).get(SessionEditorViewModel.class);
        super.initActivity();
        binding.setModel(viewModel);
    }

    @Override
    protected EditorViewModel getViewModel() {
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

    @Override
    protected void bindViews() {
        super.bindViews();

    }
}
