package com.example.fitnessfactory.ui.fragments;

import android.content.Context;

import com.example.fitnessfactory.ui.activities.BaseActivity;

public abstract class BaseFragment extends BaseAppFragment<BaseActivity> {

    private BaseActivity activity;

    @Override
    public BaseActivity getBaseActivity() {
        return activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (BaseActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }
}