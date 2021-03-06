package com.example.fitnessfactory.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.CurrentUserType;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.databinding.ActivityMainBinding;
import com.example.fitnessfactory.ui.activities.editors.OrganisationInfoEditorActivity;
import com.example.fitnessfactory.ui.fragments.FragmentProvider;
import com.example.fitnessfactory.ui.fragments.MenuFragment;
import com.example.fitnessfactory.ui.fragments.lists.ListListenerSelectFragment;
import com.example.fitnessfactory.ui.viewmodels.MainActivityViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;
import com.example.fitnessfactory.utils.dialogs.exceptions.DialogCancelledException;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private MainActivityViewModel viewModel;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setNotFullScreen();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public Toolbar getToolbar() {
        return binding.toolbar;
    }

    @Override
    public void initComponents() {
        super.initComponents();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawer, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawer.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(this);
        binding.navView.setItemIconTintList(null);
        binding.btnLogOut.setOnClickListener(view -> logOut());
        if (CurrentUserType.isOwner() && AppPrefs.askForOrganisationName().getValue()) {
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
                            if (throwable instanceof DialogCancelledException) {

                            } else {
                                throwable.printStackTrace();
                                GuiUtils.showMessage(throwable.getLocalizedMessage());
                            }
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
            case R.id.nav_admins:
                openAdminsPage();
                break;
            case R.id.nav_coaches:
                openCoachesPage();
                break;
            case R.id.nav_gyms:
                openGymsPage();
                break;
            case R.id.nav_clients:
                openClientsPage();
                break;
            case R.id.nav_session_types:
                openSessionTypesPage();
                break;
            case R.id.nav_organisation:
                openOrganisationPage();
                break;
            /*case R.id.nav_settings:
                openSettingsPage();
                break;*/
        }

        binding.drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openMainPage() {
        FragmentProvider.attachFragment(this, AppConsts.FRAGMENT_HOME_ID);
        setMenuChecked(R.id.nav_home);
    }

    private void openAdminsPage() {
        FragmentProvider.attachFragment(this, AppConsts.FRAGMENT_ADMINS_ID);
        setMenuChecked(R.id.nav_admins);
    }

    private void openCoachesPage() {
        FragmentProvider.attachFragment(this, AppConsts.FRAGMENT_COACHES_ID);
        setMenuChecked(R.id.nav_coaches);
    }

    private void openGymsPage() {
        FragmentProvider.attachFragment(this, AppConsts.FRAGMENT_GYMS_ID);
        setMenuChecked(R.id.nav_gyms);
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

        if (fragmentList.isEmpty()) {
            super.onBackPressed();
            return;
        }

        Fragment topFragment = fragmentList.get(fragmentList.size() - 1);

        if (topFragment instanceof MenuFragment) {
            askForLogout();
        } else {
            openMainPage();
        }
    }

    private void askForLogout() {
        subscribeInMainThread(
                DialogUtils.showAskDialog(
                        this,
                        ResUtils.getString(R.string.caption_log_out) + "?",
                        ResUtils.getString(R.string.caption_ok),
                        ResUtils.getString(R.string.caption_cancel)),
                new SingleData<>(
                        doLogout -> {
                            if (doLogout) {
                                logOut();
                            }
                        },
                        throwable -> {
                            throwable.printStackTrace();
                            GuiUtils.showMessage(throwable.getLocalizedMessage());
                        }
                ));
    }

    private void openClientsPage() {
        FragmentProvider.attachFragment(this, AppConsts.FRAGMENT_CLIENTS_ID);
        setMenuChecked(R.id.nav_clients);
    }

    private void openSessionTypesPage() {
        FragmentProvider.attachFragment(this, AppConsts.FRAGMENT_SESSION_TYPES_ID);
        setMenuChecked(R.id.nav_session_types);
    }

    private void openOrganisationPage() {
        Intent intent = new Intent(this, OrganisationInfoEditorActivity.class);
        startActivity(intent);
    }

   /* private void openSettingsPage() {
        FragmentProvider.attachFragment(this, AppConsts.FRAGMENT_SETTINGS_ID);
        setMenuChecked(R.id.nav_settings);
    }*/

    private void setMenuChecked(int menuId) {
        MenuItem item = binding.navView.getMenu().findItem(menuId);
        binding.navView.setCheckedItem(item);
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
