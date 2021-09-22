package com.example.fitnessfactory.ui.activities.editors;

import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.events.CoachGymsListListenerEvent;
import com.example.fitnessfactory.ui.viewmodels.CoachEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachEditorViewModelFactory;
import com.example.fitnessfactory.utils.ResUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CoachEditorActivity extends PersonnelEditorActivity {

    private CoachEditorViewModel viewModel;

    @Override
    protected CoachEditorViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected String getDeleteGymMessage() {
        return String.format(ResUtils.getString(R.string.message_ask_remove_personnel_from_gym), ResUtils.getString(R.string.caption_coach));
    }

    @Override
    public String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_coach);
    }

    @Override
    public void initActivity() {
        viewModel = new ViewModelProvider(this, new CoachEditorViewModelFactory()).get(CoachEditorViewModel.class);
        super.initActivity();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCoachEditorGymsListenerEvent(CoachGymsListListenerEvent coachGymsListListenerEvent) {
        viewModel.getGymsData();
    }
}
