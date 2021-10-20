package com.example.fitnessfactory.ui.activities.editors;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.databinding.ActivitySessionTypeEditorBinding;
import com.example.fitnessfactory.ui.viewmodels.editors.SessionTypeEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.SessionTypeEditorViewModelFactory;

public class SessionTypeEditorActivity extends EditorActivity {

    private SessionTypeEditorViewModel viewModel;

    private String typeId;
    private ActivitySessionTypeEditorBinding binding;

    @Override
    protected void initActivity() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_session_type_editor);
        viewModel = new ViewModelProvider(this, new SessionTypeEditorViewModelFactory()).get(SessionTypeEditorViewModel.class);
        super.initActivity();
        binding.setModel(viewModel);
        typeId = getIntent().getExtras().getString(AppConsts.SESSION_TYPE_ID_EXTRA);
        getViewModel().getSessionType(typeId);
    }

    @Override
    protected SessionTypeEditorViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected String getDeleteMessage() {
        return null;
    }

    @Override
    protected boolean isDataValid() {
        return false;
    }
}
