package com.example.fitnessfactory.ui.activities.editors;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.databinding.ActivityGymEditorBinding;
import com.example.fitnessfactory.ui.adapters.PersonnelPageAdapter;
import com.example.fitnessfactory.ui.viewmodels.editors.GymEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.GymEditorViewModelFactory;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;

public class GymEditorActivity extends EditorActivity {

    TextInputEditText edtName;
    TextInputEditText edtAddress;
    ViewPager2 vpPersonnel;
    TabLayout tlPersonnel;

    private String id;
    private GymEditorViewModel viewModel;
    private ActivityGymEditorBinding binding;
    private PersonnelPageAdapter pageAdapter;

    @Override
    protected GymEditorViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void initActivity() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gym_editor);
        viewModel = new ViewModelProvider(this, new GymEditorViewModelFactory()).get(GymEditorViewModel.class);
        super.initActivity();
        binding.setModel(viewModel);

        id = getIntent().getExtras().getString(AppConsts.GYM_ID_EXTRA);
        setTitle(id != null && !StringUtils.isEmpty(id) ? R.string.title_edit_item : R.string.title_add_item);
        viewModel.getGymData(id);
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
        return !StringUtils.isEmpty(edtName.getText())
                && !StringUtils.isEmpty(edtAddress.getText());
    }

    @Override
    protected void bindViews() {
        super.bindViews();
        edtName = findViewById(R.id.edtName);
        edtAddress = findViewById(R.id.edtAddress);
        vpPersonnel = findViewById(R.id.vpPersonnel);
        tlPersonnel = findViewById(R.id.tlPersonnel);
    }
}
