package com.example.fitnessfactory.ui.activities.editors;

import android.text.InputFilter;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.databinding.ActivitySessionTypeEditorBinding;
import com.example.fitnessfactory.ui.components.filters.InputFilterMinMax;
import com.example.fitnessfactory.ui.viewmodels.editors.SessionTypeEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.SessionTypeEditorViewModelFactory;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;

public class SessionTypeEditorActivity extends EditorActivity {

    private String typeId;
    private SessionTypeEditorViewModel viewModel;
    private ActivitySessionTypeEditorBinding binding;

    @Override
    public Toolbar getToolbar() {
        return binding.toolbar;
    }

    @Override
    protected void initActivity() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_session_type_editor);
        viewModel = new ViewModelProvider(this, new SessionTypeEditorViewModelFactory()).get(SessionTypeEditorViewModel.class);
        super.initActivity();
        binding.setModel(viewModel);
        binding.container.edtPeopleAmount.setFilters(
                new InputFilter[] {
                        new InputFilterMinMax(
                                binding.container.edtPeopleAmount,
                                AppConsts.MIN_PEOPLE_AMOUNT,
                                AppPrefs.maxPeopleAmount().getValue())});

        getViewModel().getSessionType(typeId);
    }

    @Override
    protected String getTitleCaption() {
        return ResUtils.getString(R.string.caption_session_type);
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
        return !StringUtils.isEmpty(binding.container.edtName.getText())
                && !StringUtils.isEmpty(binding.container.edtPeopleAmount.getText())
                && !StringUtils.isEmpty(binding.container.edtPrice.getText());
    }
}
