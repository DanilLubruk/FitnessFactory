package com.example.fitnessfactory.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.ui.activities.AuthActivity;
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

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_main_menu;
    }
}
