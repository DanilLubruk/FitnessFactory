package com.example.fitnessfactory.ui.activities.editors;

import android.widget.TextView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.databinding.ActivityClientEditorBinding;
import com.example.fitnessfactory.databinding.ActivityGymEditorBinding;
import com.example.fitnessfactory.ui.viewmodels.editors.ClientEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.editors.EditorViewModel;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.google.android.material.textfield.TextInputEditText;

public class ClientEditorActivity extends EditorActivity {

    private TextInputEditText edtName;
    private TextInputEditText edtEmail;

    private ClientEditorViewModel viewModel;
    private ActivityClientEditorBinding binding;

    @Override
    protected EditorViewModel getViewModel() {
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
}
