package com.example.fitnessfactory.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.ui.fragments.FragmentProvider;
import com.example.fitnessfactory.utils.GuiUtils;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer)
    DrawerLayout drawer;
    @BindView(R.id.navView)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
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