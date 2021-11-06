package com.example.fitnessfactory.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fitnessfactory.ui.fragments.lists.gymPersonnelList.GymAdminsListTabFragment;
import com.example.fitnessfactory.ui.fragments.lists.gymPersonnelList.GymCoachesListTabFragment;

public class PersonnelPageAdapter extends FragmentStateAdapter {

    public PersonnelPageAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new GymAdminsListTabFragment();
            case 1:
                return new GymCoachesListTabFragment();
            default:
                return new GymAdminsListTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
