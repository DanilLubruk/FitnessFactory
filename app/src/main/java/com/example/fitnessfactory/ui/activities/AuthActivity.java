package com.example.fitnessfactory.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.security.ObfuscateData;
import com.example.fitnessfactory.ui.viewmodels.AuthViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import io.michaelrocks.paranoid.Obfuscate;

public class AuthActivity extends BaseActivity {

    @BindView(R.id.btnSignIn)
    SignInButton btnSignIn;

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
        Intent signInIntent = viewModel.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> completedTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            viewModel.handleSignIn(completedTask).observe(this, isHandled -> {
                if (isHandled) {
                    showMainActivity();
                }
            });
        }
    }

    private void showMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showFailedAuthMessage() {
        showFailedAuthMessage("");
    }

    private void showFailedAuthMessage(String errorMessage) {
        GuiUtils.showMessage(ResUtils.getString(R.string.caption_wrong_auth).concat(errorMessage));
    }
}