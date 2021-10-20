package com.example.fitnessfactory.ui.activities.editors;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.databinding.ActivityGymEditorBinding;
import com.example.fitnessfactory.ui.adapters.PersonnelPageAdapter;
import com.example.fitnessfactory.ui.viewmodels.editors.GymEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.GymEditorViewModelFactory;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;

public class GymEditorActivity extends EditorActivity {

    private AppCompatEditText edtName;
    private AppCompatEditText edtAddress;
    private ViewPager2 vpPersonnel;
    private TabLayout tlPersonnel;

    private String gymId;
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

        viewModel.getGymData(gymId);
        subscribeForGymIdChangesForTabs();

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

    private void subscribeForGymIdChangesForTabs() {
        viewModel.getGymId()
                .observe(this, gymId -> getIntent().putExtra(AppConsts.GYM_ID_EXTRA, gymId));
    }

    @Override
    protected boolean isNewEntity() {
        return StringUtils.isEmpty(gymId);
    }

    @Override
    protected void initEntityKey() {
        gymId = getIntent().getExtras().getString(AppConsts.GYM_ID_EXTRA);
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
