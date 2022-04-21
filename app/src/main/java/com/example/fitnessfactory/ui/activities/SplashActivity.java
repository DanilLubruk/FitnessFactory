package com.example.fitnessfactory.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.databinding.ActivitySplashBinding;
import com.example.fitnessfactory.ui.viewmodels.AuthViewModel;
import com.example.fitnessfactory.ui.viewmodels.SplashActivityViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.AuthViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.SplashActivityViewModelFactory;

public class SplashActivity extends BaseActivity {

    private SplashActivityViewModel viewModel;
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFullScreen();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        viewModel = new ViewModelProvider(this, new SplashActivityViewModelFactory()).get(SplashActivityViewModel.class);
        super.onCreate(savedInstanceState);
        viewModel.isLoggedIn().observe(this, isLoggedIn -> {
            if (isLoggedIn) {
                showMainActivity();
            } else {
                showSignInActivity();
            }
        });
    }

    private void showMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showSignInActivity() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public Toolbar getToolbar() {
        return null;
    }
}
