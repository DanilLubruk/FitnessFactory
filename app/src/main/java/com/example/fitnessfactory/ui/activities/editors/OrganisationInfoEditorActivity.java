package com.example.fitnessfactory.ui.activities.editors;
import android.view.Menu;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.databinding.ActivityOrganisationInfoEditorBinding;
import com.example.fitnessfactory.ui.viewmodels.EditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.OrganisationInfoViewModel;
import com.example.fitnessfactory.utils.ResUtils;

public class OrganisationInfoEditorActivity extends EditorActivity {

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
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean isCreated = super.onCreateOptionsMenu(menu);
        menu.removeItem(MENU_DELETE);

        return isCreated;
    }
}
