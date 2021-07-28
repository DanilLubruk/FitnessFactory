package com.example.fitnessfactory.ui.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;

public class FragmentProvider {

    public static void attachFragment(AppCompatActivity activity, int fragmentId) {
        Fragment curFragment = null;
        String fragmentName = "";

        switch (fragmentId) {
            case AppConsts.FRAGMENT_HOME_ID:
                curFragment = new MenuFragment();
                fragmentName = AppConsts.FRAGMENT_HOME_NAME;
                break;
            case AppConsts.FRAGMENT_GYMS_ID:

                break;
        }

        if (curFragment != null) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, curFragment, fragmentName);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}
