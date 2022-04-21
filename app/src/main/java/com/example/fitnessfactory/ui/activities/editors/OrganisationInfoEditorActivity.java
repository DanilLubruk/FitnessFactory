package com.example.fitnessfactory.ui.activities.editors;
import android.text.InputFilter;
import android.view.Menu;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.databinding.ActivityOrganisationInfoEditorBinding;
import com.example.fitnessfactory.ui.components.filters.InputFilterEmail;
import com.example.fitnessfactory.ui.components.filters.InputFilterMinMax;
import com.example.fitnessfactory.ui.components.filters.ValueCheckers.EmailValueChecker;
import com.example.fitnessfactory.ui.viewmodels.editors.EditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.editors.OrganisationInfoViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.google.android.material.textfield.TextInputEditText;

public class OrganisationInfoEditorActivity extends EditorActivity {

    private OrganisationInfoViewModel viewModel;
    private ActivityOrganisationInfoEditorBinding binding;

    @Override
    public Toolbar getToolbar() {
        return binding.toolbar;
    }

    @Override
    public void initActivity() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_organisation_info_editor);
        viewModel = new ViewModelProvider(this).get(OrganisationInfoViewModel.class);
        viewModel.getData().observe(this, viewModel::setData);
        super.initActivity();
        binding.setModel(viewModel);
    }

    @Override
    protected String getTitleCaption() {
        return ResUtils.getString(R.string.title_organisation);
    }

    @Override
    protected EditorViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected boolean isNewEntity() {
        return false;
    }

    @Override
    protected void initEntityKey() { }

    @Override
    protected String getDeleteMessage() {
        return "";
    }

    @Override
    protected boolean checkScreenDataValidity() {
        String email = binding.container.edtEmail.getText().toString().trim();

        if (!StringUtils.isEmpty(email) && !EmailValueChecker.getInstance().isValueValid(email)) {
            binding.container.edtEmail.setError(ResUtils.getString(R.string.message_error_email_invalid));
            return false;
        }

        return super.checkScreenDataValidity();
    }

    protected String getInvalidDataMessage() {
        return ResUtils.getString(R.string.caption_blank_organisation_name);
    }

    @Override
    protected boolean isDataValid() {
        return !StringUtils.isEmpty(binding.container.edtName.getText());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean isCreated = super.onCreateOptionsMenu(menu);
        menu.removeItem(MENU_DELETE);

        return isCreated;
    }
}
