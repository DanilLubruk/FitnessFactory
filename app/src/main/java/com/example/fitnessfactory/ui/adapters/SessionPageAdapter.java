package com.example.fitnessfactory.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.fitnessfactory.ui.fragments.lists.sessionParticipantList.SessionClientsListTabFragment;
import com.example.fitnessfactory.ui.fragments.lists.sessionParticipantList.SessionCoachesListTabFragment;

public class SessionPageAdapter extends FragmentStateAdapter {

    public SessionPageAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SessionClientsListTabFragment();
            case 1:
                return new SessionCoachesListTabFragment();
            default:
                return new SessionClientsListTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
