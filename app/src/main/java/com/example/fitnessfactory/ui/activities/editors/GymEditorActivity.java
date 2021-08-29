package com.example.fitnessfactory.ui.activities.editors;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.databinding.ActivityGymEditorBinding;
import com.example.fitnessfactory.ui.adapters.PersonnelPageAdapter;
import com.example.fitnessfactory.ui.viewmodels.GymEditorViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;

public class GymEditorActivity extends EditorActivity {

    @BindView(R.id.edtName)
    TextInputEditText edtName;
    @BindView(R.id.edtAddress)
    TextInputEditText edtAddress;
    @BindView(R.id.vpPersonnel)
    ViewPager2 vpPersonnel;
    @BindView(R.id.tlPersonnel)
    TabLayout tlPersonnel;

    private String id;
    private GymEditorViewModel viewModel;
    private ActivityGymEditorBinding binding;
    private PersonnelPageAdapter pageAdapter;

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
        setTitle(id != null && !StringUtils.isEmpty(id) ? R.string.title_edit_item : R.string.title_add_item);
        viewModel.getGym(id)
                .observe(this, viewModel::setGym);
        viewModel.getGymId()
                .observe(this, gymId -> getIntent().putExtra(AppConsts.GYM_ID_EXTRA, gymId));

        pageAdapter = new PersonnelPageAdapter(getSupportFragmentManager(), getLifecycle());
        vpPersonnel.setAdapter(pageAdapter);
        new TabLayoutMediator(tlPersonnel, vpPersonnel,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(ResUtils.getString(R.string.title_admins));
                            break;
                        case 1:
                            tab.setText(ResUtils.getString(R.string.title_coaches));
                            break;
                    }
                }
        ).attach();
    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_gym);
    }

    @Override
    protected boolean isDataValid() {
        if (edtName.getText() != null &&
                edtAddress.getText() != null &&
                !StringUtils.isEmpty(edtName.getText().toString()) &&
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
