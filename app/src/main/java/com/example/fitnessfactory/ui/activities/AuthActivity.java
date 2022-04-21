package com.example.fitnessfactory.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.beans.UsersList;
import com.example.fitnessfactory.data.callbacks.UsersListCallback;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.observers.SingleDialogEvent;
import com.example.fitnessfactory.databinding.ActivityAuthBinding;
import com.example.fitnessfactory.ui.viewmodels.AuthViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.AuthViewModelFactory;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;
import com.google.android.gms.common.SignInButton;

import java.util.List;

public class AuthActivity extends BaseActivity {

    private final int RC_SIGN_IN = 1;
    private AuthViewModel viewModel;
    private ActivityAuthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFullScreen();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        viewModel = new ViewModelProvider(this, new AuthViewModelFactory()).get(AuthViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public Toolbar getToolbar() {
        return null;
    }

    @Override
    public void initComponents() {
        super.initComponents();
        binding.container.btnSignIn.setOnClickListener(view -> googleSignIn());
    }

    private void googleSignIn() {
        showProgress();
        setSigningInText();
        viewModel.getSignInIntent().observe(this, this::startAuthentication);
    }

    private void startAuthentication(Intent signInIntent) {
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if(resultCode == RESULT_OK) {
                handleSignIn(data);
            }
            else if(resultCode == RESULT_CANCELED){
                viewModel.interruptSignIn();
                closeProgress();
            }
        }
    }

    private void handleSignIn(Intent data) {
        setObtainingDataText();
        SingleDialogEvent<Integer, List<AppUser>> dialogEvent =
                new SingleDialogEvent<>(this, DialogUtils::showAskOwnerDialog);
        viewModel.signInUser(data, dialogEvent)
                .observe(this, isRegistered -> {
            if (isRegistered) {
                closeProgress();
                showMainActivity();
            } else {
                signInFailed();
            }
        });
    }

    private void signInFailed() {
        closeProgress();
        viewModel.signOut();
    }

    private void showMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showProgress() {
        binding.container.pkProgress.setVisibility(View.VISIBLE);
        binding.container.tvLoadStatus.setVisibility(View.VISIBLE);
        binding.container.btnSignIn.setVisibility(View.GONE);
    }

    @Override
    public void closeProgress() {
        binding.container.pkProgress.setVisibility(View.GONE);
        binding.container.tvLoadStatus.setVisibility(View.GONE);
        binding.container.tvLoadStatus.setText("");
        binding.container.btnSignIn.setVisibility(View.VISIBLE);
    }

    private void setSigningInText() {
        binding.container.tvLoadStatus.setText(ResUtils.getString(R.string.message_singing_you_in));
    }

    private void setObtainingDataText() {
        binding.container.tvLoadStatus.setText(ResUtils.getString(R.string.message_obtaining_organistations));
    }
}