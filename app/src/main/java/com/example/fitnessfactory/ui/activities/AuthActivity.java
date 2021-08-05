package com.example.fitnessfactory.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.system.FirebaseAuthManager;
import com.example.fitnessfactory.ui.viewmodels.AuthViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;

import java.util.List;

import butterknife.BindView;

public class AuthActivity extends BaseActivity {

    @BindView(R.id.btnSignIn)
    SignInButton btnSignIn;
    @BindView(R.id.pkProgress)
    ProgressBar pkProgress;
    @BindView(R.id.tvAppName)
    TextView tvAppName;
    @BindView(R.id.imgLogo)
    ImageView imgLogo;

    private final int RC_SIGN_IN = 1;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFullScreen();
        setContentView(R.layout.activity_auth);
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initComponents() {
        super.initComponents();
        if (FirebaseAuthManager.isLoggedIn()) {
            showMainActivity();
        }
        btnSignIn.setOnClickListener(view -> googleSignIn());
        hideToolbar();
    }

    private void hideToolbar() {
        getToolbar().setVisibility(View.GONE);
    }

    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void googleSignIn() {
        showProgress();
        viewModel.getSignInIntent().observe(this, this::startAuthentication);
    }

    private void startAuthentication(Intent signInIntent) {
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if(resultCode == RESULT_OK){
                handleSignIn(data);
            }
            else if(resultCode == RESULT_CANCELED){
                viewModel.interruptSignIn();
                closeProgress();
            }
        }
    }

    private void showAskUserTypeDialog(List<AppUser> gymOwners) {
        subscribeInMainThread(DialogUtils.showAskOwnerDialog(this, gymOwners),
                () -> {
                    closeProgress();
                    showMainActivity();
                },
                throwable -> {
                    signInFailed();
                    throwable.printStackTrace();
                    GuiUtils.showMessage(throwable.getLocalizedMessage());
                });
    }

    private void handleSignIn(Intent data) {
        viewModel.handleSignIn(data)
                .observe(this, owners -> {
                    if (owners != null) {
                        showAskUserTypeDialog(owners);
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
        pkProgress.setVisibility(View.VISIBLE);
        tvAppName.setVisibility(View.GONE);
        imgLogo.setVisibility(View.GONE);
        btnSignIn.setVisibility(View.GONE);
    }

    @Override
    public void closeProgress() {
        pkProgress.setVisibility(View.GONE);
        tvAppName.setVisibility(View.VISIBLE);
        imgLogo.setVisibility(View.VISIBLE);
        btnSignIn.setVisibility(View.VISIBLE);
    }

    private void showFailedAuthMessage() {
        showFailedAuthMessage("");
    }

    private void showFailedAuthMessage(String errorMessage) {
        GuiUtils.showMessage(ResUtils.getString(R.string.caption_wrong_auth).concat(errorMessage));
    }
}