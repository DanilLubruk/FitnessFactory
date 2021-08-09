package com.example.fitnessfactory.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.system.FirebaseAuthManager;
import com.example.fitnessfactory.ui.fragments.FragmentProvider;
import com.example.fitnessfactory.ui.viewmodels.MainActivityViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer)
    DrawerLayout drawer;
    @BindView(R.id.navView)
    NavigationView navigationView;
    @BindView(R.id.btnLogOut)
    Button btnLogOut;

    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
    }

    @Override
    public void initComponents() {
        super.initComponents();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        btnLogOut.setOnClickListener(view -> logOut());
        if (AppPrefs.isUserOwner().getValue() && AppPrefs.askForOrganisationName().getValue()) {
            showAskOrgNameDialog();
        }
    }

    private void showAskOrgNameDialog() {
        subscribeInMainThread(DialogUtils.showOneLineEditDialog(
                this,
                ResUtils.getString(R.string.title_ask_org_name),
                ResUtils.getString(R.string.caption_name),
                ResUtils.getString(R.string.caption_ok),
                ResUtils.getString(R.string.caption_cancel)),
                new SingleData<>(
                        organisationName -> {
                            viewModel.setOrganisationName(organisationName);
                            AppPrefs.askForOrganisationName().setValue(false);
                        },
                        throwable -> {
                            throwable.printStackTrace();
                            GuiUtils.showMessage(throwable.getLocalizedMessage());
                        }));
    }

    private void logOut() {
        viewModel.signOut()
                .observe(this, isSignedOut -> {
                    if (isSignedOut) {
                        showAuthActivity();
                    }
                });
    }

    private void showAuthActivity() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getCurrFragment() == null) {
            openMainPage();
        }
    }

    private Fragment getCurrFragment() {
        try {
            return getSupportFragmentManager().findFragmentById(R.id.container);
        } catch (Exception e) {
            e.printStackTrace();
            GuiUtils.showMessage(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                openMainPage();
                break;
            case R.id.nav_gyms:
                openGymsPage();
                break;
            case R.id.nav_admins:
                openAdminsPage();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openMainPage() {
        FragmentProvider.attachFragment(this, AppConsts.FRAGMENT_HOME_ID);
        setMenuChecked(R.id.nav_home);
    }

    private void openGymsPage() {
        FragmentProvider.attachFragment(this, AppConsts.FRAGMENT_GYMS_ID);
        setMenuChecked(R.id.nav_gyms);
    }

    private void openAdminsPage() {
        FragmentProvider.attachFragment(this, AppConsts.FRAGMENT_ADMINS_ID);
        setMenuChecked(R.id.nav_admins);
    }

    private void setMenuChecked(int menuId) {
        MenuItem item = navigationView.getMenu().findItem(menuId);
        navigationView.setCheckedItem(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedState) {
        super.onSaveInstanceState(savedState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
    }
}
