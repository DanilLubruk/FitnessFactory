package com.example.fitnessfactory.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fitnessfactory.ui.fragments.lists.AdminsListFragment;
import com.example.fitnessfactory.ui.fragments.lists.AdminsListTabFragment;
import com.example.fitnessfactory.ui.fragments.lists.CoachesListTabFragment;

public class PersonnelPageAdapter extends FragmentStateAdapter {

    public PersonnelPageAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AdminsListTabFragment();
            case 1:
                return new CoachesListTabFragment();
            default:
                return new AdminsListTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
