package com.example.fitnessfactory.ui.fragments.lists;

import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.GymCoachesListListenerEvent;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachesListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.GymCoachesListTabViewModel;
import com.example.fitnessfactory.utils.ResUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class GymCoachesListTabFragment extends GymPersonnelListTabFragment {

    private GymCoachesListTabViewModel viewModel;

    @Override
    protected void defineViewModel() {
        viewModel = new ViewModelProvider(this, new CoachesListTabViewModelFactory()).get(GymCoachesListTabViewModel.class);
    }

    @Override
    protected GymCoachesListTabViewModel getViewModel() {
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
