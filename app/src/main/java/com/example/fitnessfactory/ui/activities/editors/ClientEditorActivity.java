package com.example.fitnessfactory.ui.activities.editors;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.databinding.ActivityClientEditorBinding;
import com.example.fitnessfactory.ui.viewmodels.editors.ClientEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.ClientEditorViewModelFactory;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;

public class ClientEditorActivity extends EditorActivity {

    private String clientId;
    private ClientEditorViewModel viewModel;
    private ActivityClientEditorBinding binding;

    @Override
    public Toolbar getToolbar() {
        return binding.toolbar;
    }

    @Override
    protected void initActivity() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_client_editor);
        viewModel = new ViewModelProvider(this, new ClientEditorViewModelFactory()).get(ClientEditorViewModel.class);
        super.initActivity();
        binding.setModel(viewModel);
        getViewModel().getClientData(clientId);
    }

    @Override
    protected ClientEditorViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected boolean isNewEntity() {
        return StringUtils.isEmpty(clientId);
    }

    @Override
    protected void initEntityKey() {
        clientId = getIntent().getExtras().getString(AppConsts.CLIENT_ID_EXTRA);
    }

    @Override
    protected boolean isDataValid() {
        return !StringUtils.isEmpty(binding.container.edtName.getText())
                && !StringUtils.isEmpty(binding.container.edtEmail.getText());
    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_client);
    }
}
