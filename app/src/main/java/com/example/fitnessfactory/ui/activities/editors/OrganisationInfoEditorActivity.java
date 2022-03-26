package com.example.fitnessfactory.ui.activities.editors;
import android.view.Menu;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.databinding.ActivityOrganisationInfoEditorBinding;
import com.example.fitnessfactory.ui.viewmodels.editors.EditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.editors.OrganisationInfoViewModel;
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
