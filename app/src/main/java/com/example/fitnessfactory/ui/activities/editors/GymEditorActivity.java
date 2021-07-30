package com.example.fitnessfactory.ui.activities.editors;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.databinding.ActivityGymEditorBinding;
import com.example.fitnessfactory.ui.viewmodels.GymEditorViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;

public class GymEditorActivity extends EditorActivity {

    @BindView(R.id.edtName)
    TextInputEditText edtName;
    @BindView(R.id.edtAddress)
    TextInputEditText edtAddress;

    private String id;
    private GymEditorViewModel viewModel;
    private ActivityGymEditorBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected GymEditorViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void initActivity() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gym_editor);
        viewModel = new ViewModelProvider(this).get(GymEditorViewModel.class);
        super.initActivity();
        binding.setModel(viewModel);
        id = getIntent().getExtras().getString(AppConsts.GYM_ID_EXTRA);
        viewModel.getGym(id)
                .observe(this, viewModel::setGym);
    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_gym);
    }

    @Override
    protected boolean isDataValid() {
        if (!StringUtils.isEmpty(edtName.getText().toString()) &&
        !StringUtils.isEmpty(edtAddress.getText().toString())) {
            return true;
        } else {
            GuiUtils.showMessage(ResUtils.getString(R.string.caption_blank_fields));
            return false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        viewModel.saveState(savedState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        viewModel.restoreState(savedState);
    }
}
