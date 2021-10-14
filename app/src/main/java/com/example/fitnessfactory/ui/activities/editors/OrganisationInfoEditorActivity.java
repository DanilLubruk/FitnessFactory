package com.example.fitnessfactory.ui.activities.editors;
import android.view.Menu;
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

    private TextInputEditText edtOrgName;

    private OrganisationInfoViewModel viewModel;
    private ActivityOrganisationInfoEditorBinding binding;

    @Override
    public void initActivity() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_organisation_info_editor);
        viewModel = new ViewModelProvider(this).get(OrganisationInfoViewModel.class);
        viewModel.getData().observe(this, viewModel::setData);
        super.initActivity();
        binding.setModel(viewModel);
        setTitle(ResUtils.getString(R.string.title_organisation));
    }

    @Override
    protected EditorViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected String getDeleteMessage() {
        return "";
    }

    @Override
    protected boolean isDataValid() {
        return !StringUtils.isEmpty(edtOrgName.getText());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean isCreated = super.onCreateOptionsMenu(menu);
        menu.removeItem(MENU_DELETE);

        return isCreated;
    }

    @Override
    protected void bindViews() {
        super.bindViews();
        edtOrgName = findViewById(R.id.edtName);
    }
}
