package com.example.fitnessfactory.ui.fragments;

import com.example.fitnessfactory.ui.activities.editors.EditorActivity;

public abstract class EditorTabFragment extends BaseFragment {

    @Override
    public EditorActivity getBaseActivity() {
        return (EditorActivity) getActivity();
    }
}
