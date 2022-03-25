package com.example.fitnessfactory.ui.activities;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.databinding.SelectionActivityBinding;
import com.example.fitnessfactory.ui.fragments.FragmentProvider;

public class SelectionActivity extends BaseActivity {

    private SelectionActivityBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.selection_activity);
        super.onCreate(savedInstanceState);
    }

    @Override
    public Toolbar getToolbar() {
        return binding.toolbar;
    }

    @Override
    public void initActivity() {
        super.initActivity();
        int fragmentId = getIntent().getIntExtra(AppConsts.FRAGMENT_ID_EXTRA, AppConsts.UNDEFINED_VALUE);
        getIntent().putExtra(AppConsts.IS_SELECT_MODE_EXTRA, true);
        FragmentProvider.attachFragmentSelectActivity(this, fragmentId);
    }
}
