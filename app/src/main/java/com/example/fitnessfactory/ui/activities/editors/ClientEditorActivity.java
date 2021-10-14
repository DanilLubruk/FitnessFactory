package com.example.fitnessfactory.ui.activities.editors;

import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.databinding.ActivityClientEditorBinding;
import com.example.fitnessfactory.ui.viewmodels.editors.ClientEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.editors.EditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.ClientEditorViewModelFactory;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.google.android.material.textfield.TextInputEditText;

public class ClientEditorActivity extends EditorActivity {

    private TextInputEditText edtName;
    private TextInputEditText edtEmail;

    private String clientEmail;
    private ClientEditorViewModel viewModel;
    private ActivityClientEditorBinding binding;

    @Override
    protected void initActivity() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_client_editor);
        viewModel = new ViewModelProvider(this, new ClientEditorViewModelFactory()).get(ClientEditorViewModel.class);
        super.initActivity();
        binding.setModel(viewModel);
        clientEmail = getIntent().getExtras().getString(AppConsts.CLIENT_EMAIL_EXTRA);
        getViewModel().getClientData(clientEmail);
    }

    @Override
    protected ClientEditorViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected boolean isDataValid() {
        return !StringUtils.isEmpty(edtName.getText())
                && !StringUtils.isEmpty(edtEmail.getText());
    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_client);
    }

    @Override
    protected void bindViews() {
        super.bindViews();
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
    }
}
