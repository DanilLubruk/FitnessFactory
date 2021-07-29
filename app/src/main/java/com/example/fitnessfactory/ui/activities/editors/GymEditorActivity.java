package com.example.fitnessfactory.ui.activities.editors;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.databinding.ActivityGymEditorBinding;
import com.example.fitnessfactory.ui.viewmodels.GymEditorViewModel;

public class GymEditorActivity extends EditorActivity {

    private String id;
    private GymEditorViewModel viewModel;
    private ActivityGymEditorBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
