package com.example.fitnessfactory.ui.activities;

import android.os.Bundle;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.ui.fragments.FragmentProvider;

public class SelectionActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.selection_activity);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initActivity() {
        int fragmentId = getIntent().getIntExtra(AppConsts.FRAGMENT_ID_EXTRA, AppConsts.UNDEFINED_VALUE);
        getIntent().putExtra(AppConsts.IS_SELECT_MODE_EXTRA, true);
        FragmentProvider.attachFragment(this, fragmentId);
    }
}
