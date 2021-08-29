package com.example.fitnessfactory.ui.fragments;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.fitnessfactory.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;

public class MenuFragment extends BaseFragment {

    @BindView(R.id.tvGreeting)
    TextView tvGreeting;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBaseActivity().setTitle(R.string.app_name);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            tvGreeting.setText(user.getDisplayName() + " " + user.getEmail());
        }
    }

    public void closeProgress() {

    }

    public void showProgress() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_main_menu;
    }
}
