package com.example.fitnessfactory.ui.activities.editors;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.databinding.ActivitySessionTypeEditorBinding;
import com.example.fitnessfactory.ui.viewmodels.editors.SessionTypeEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.SessionTypeEditorViewModelFactory;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.google.android.material.textfield.TextInputEditText;

public class SessionTypeEditorActivity extends EditorActivity {

    private AppCompatEditText edtName;
    private AppCompatEditText edtPeopleAmount;
    private AppCompatEditText edtPrice;

    private String typeId;
    private SessionTypeEditorViewModel viewModel;
    private ActivitySessionTypeEditorBinding binding;

    @Override
    protected void initActivity() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_session_type_editor);
        viewModel = new ViewModelProvider(this, new SessionTypeEditorViewModelFactory()).get(SessionTypeEditorViewModel.class);
        super.initActivity();
        binding.setModel(viewModel);
        getViewModel().getSessionType(typeId);
    }

    @Override
    protected SessionTypeEditorViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected boolean isNewEntity() {
        return StringUtils.isEmpty(typeId);
    }

    @Override
    protected void initEntityKey() {
        typeId = getIntent().getExtras().getString(AppConsts.SESSION_TYPE_ID_EXTRA);
    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_session_type);
    }

    @Override
    protected boolean isDataValid() {
        return !StringUtils.isEmpty(edtName.getText())
                && !StringUtils.isEmpty(edtPeopleAmount.getText())
                && !StringUtils.isEmpty(edtPrice.getText());
    }

    @Override
    public void bindViews() {
        super.bindViews();
        edtName = findViewById(R.id.edtName);
        edtPeopleAmount = findViewById(R.id.edtPeopleAmount);
        edtPrice = findViewById(R.id.edtPrice);
    }
}
