package com.example.fitnessfactory.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fitnessfactory.ui.fragments.lists.sessionsList.CoachSessionsListTabFragment;
import com.example.fitnessfactory.ui.fragments.lists.personnelGymsList.CoachGymsListTabFragment;

public class CoachGymsPageAdapter extends FragmentStateAdapter {

    public CoachGymsPageAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CoachGymsListTabFragment();
            case 1:
                return new CoachSessionsListTabFragment();
            default:
                return new CoachGymsListTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
