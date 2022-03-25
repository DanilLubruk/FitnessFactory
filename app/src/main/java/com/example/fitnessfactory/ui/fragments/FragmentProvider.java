package com.example.fitnessfactory.ui.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.ui.fragments.lists.DaySessionsListFragment;
import com.example.fitnessfactory.ui.fragments.lists.personnelList.AdminsListFragment;
import com.example.fitnessfactory.ui.fragments.lists.personnelList.ClientsListFragment;
import com.example.fitnessfactory.ui.fragments.lists.personnelList.CoachesListFragment;
import com.example.fitnessfactory.ui.fragments.lists.GymsListFragment;
import com.example.fitnessfactory.ui.fragments.lists.SessionTypesListFragment;

public class FragmentProvider {

    public static <FragmentType extends Fragment> void attachFragmentSelectActivity(AppCompatActivity activity, int fragmentId) {
        FragmentType curFragment = null;
        String fragmentName = "";

        switch (fragmentId) {
            case AppConsts.FRAGMENT_HOME_ID:
                curFragment = (FragmentType) new MenuFragment();
                fragmentName = AppConsts.FRAGMENT_HOME_NAME;
                break;
            case AppConsts.FRAGMENT_ADMINS_ID:
                curFragment = (FragmentType) new AdminsListFragment();
                fragmentName = AppConsts.FRAGMENT_ADMINS_NAME;
                break;
            case AppConsts.FRAGMENT_COACHES_ID:
                curFragment = (FragmentType) new CoachesListFragment();
                fragmentName = AppConsts.FRAGMENT_COACHES_NAME;
                break;
            case AppConsts.FRAGMENT_GYMS_ID:
                curFragment = (FragmentType) new GymsListFragment();
                fragmentName = AppConsts.FRAGMENT_GYMS_NAME;
                break;
            case AppConsts.FRAGMENT_CLIENTS_ID:
                curFragment = (FragmentType) new ClientsListFragment();
                fragmentName = AppConsts.FRAGMENT_CLIENTS_NAME;
                break;
            case AppConsts.FRAGMENT_SESSION_TYPES_ID:
                curFragment = (FragmentType) new SessionTypesListFragment();
                fragmentName = AppConsts.FRAGMENT_SESSION_TYPES_NAME;
                break;
            case AppConsts.FRAGMENT_DAYS_SESSIONS_ID:
                curFragment = (FragmentType) new DaySessionsListFragment();
                fragmentName = AppConsts.FRAGMENT_DAYS_SESSIONS_NAME;
                break;
            case AppConsts.FRAGMENT_SETTINGS_ID:
                curFragment = (FragmentType) new SettingsFragment();
                fragmentName = AppConsts.FRAGMENT_SETTINGS_NAME;
                break;
        }

        if (curFragment != null) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, curFragment, fragmentName);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public static void attachFragment(AppCompatActivity activity, int fragmentId) {
        Fragment curFragment = null;
        String fragmentName = "";

        switch (fragmentId) {
            case AppConsts.FRAGMENT_HOME_ID:
                curFragment = new MenuFragment();
                fragmentName = AppConsts.FRAGMENT_HOME_NAME;
                break;
            case AppConsts.FRAGMENT_ADMINS_ID:
                curFragment = new AdminsListFragment();
                fragmentName = AppConsts.FRAGMENT_ADMINS_NAME;
                break;
            case AppConsts.FRAGMENT_COACHES_ID:
                curFragment = new CoachesListFragment();
                fragmentName = AppConsts.FRAGMENT_COACHES_NAME;
                break;
            case AppConsts.FRAGMENT_GYMS_ID:
                curFragment = new GymsListFragment();
                fragmentName = AppConsts.FRAGMENT_GYMS_NAME;
                break;
            case AppConsts.FRAGMENT_CLIENTS_ID:
                curFragment = new ClientsListFragment();
                fragmentName = AppConsts.FRAGMENT_CLIENTS_NAME;
                break;
            case AppConsts.FRAGMENT_SESSION_TYPES_ID:
                curFragment = new SessionTypesListFragment();
                fragmentName = AppConsts.FRAGMENT_SESSION_TYPES_NAME;
                break;
            case AppConsts.FRAGMENT_DAYS_SESSIONS_ID:
                curFragment = new DaySessionsListFragment();
                fragmentName = AppConsts.FRAGMENT_DAYS_SESSIONS_NAME;
                break;
        }

        if (curFragment != null) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.attach(curFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public static void detachFragment(AppCompatActivity activity, Fragment fragment) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.detach(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }
}
