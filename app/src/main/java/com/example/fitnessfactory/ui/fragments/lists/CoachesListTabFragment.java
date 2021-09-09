package com.example.fitnessfactory.ui.fragments.lists;

import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.GymCoachesListListenerEvent;
import com.example.fitnessfactory.ui.viewmodels.lists.CoachesListTabViewModel;
import com.example.fitnessfactory.utils.ResUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CoachesListTabFragment extends PersonnelListTabFragment {

    private CoachesListTabViewModel viewModel;

    @Override
    protected void defineViewModel() {
        viewModel = new ViewModelProvider(this).get(CoachesListTabViewModel.class);
    }

    @Override
    protected CoachesListTabViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected String getSingularPersonnelCaption() {
        return ResUtils.getString(R.string.caption_coach);
    }

    @Override
    protected int getSelectionFragmentId() {
        return AppConsts.FRAGMENT_COACHES_ID;
    }

    @Override
    protected String getPersonnelEmailExtraKey() {
        return AppConsts.COACH_EMAIL_EXTRA;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGymCoachesListListenerEvent(GymCoachesListListenerEvent gymCoachesListListenerEvent) {
        viewModel.getPersonnelData();
    }
}
