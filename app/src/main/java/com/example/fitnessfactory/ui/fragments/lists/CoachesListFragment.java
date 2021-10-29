package com.example.fitnessfactory.ui.fragments.lists;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.events.CoachesListDataListenerEvent;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.activities.editors.CoachEditorActivity;
import com.example.fitnessfactory.ui.adapters.PersonnelListAdapter;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachesListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.CoachesListViewModel;
import com.example.fitnessfactory.utils.ResUtils;
import com.tiromansev.prefswrapper.typedprefs.BooleanPreference;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class CoachesListFragment extends PersonnelListFragment {

    private CoachesListViewModel viewModel;

    @Override
    protected void defineViewModel() {
        viewModel = new ViewModelProvider(this, new CoachesListViewModelFactory()).get(CoachesListViewModel.class);
    }

    @Override
    protected CoachesListViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected String getSelectTitle() {
        return ResUtils.getString(R.string.title_select_coach);
    }

    @Override
    protected String getListTitle() {
        return ResUtils.getString(R.string.title_coaches);
    }

    @Override
    protected BooleanPreference getAskToSendInvitationPrefs() {
        return AppPrefs.askToSendCoachEmailInvite();
    }

    @Override
    protected Intent getEditorActivityIntent(AppUser personnel) {
        Intent intent = new Intent(getBaseActivity(), CoachEditorActivity.class);

        intent.putExtra(AppConsts.COACH_ID_EXTRA, personnel.getId());
        intent.putExtra(AppConsts.COACH_NAME_EXTRA, personnel.getName());
        intent.putExtra(AppConsts.COACH_EMAIL_EXTRA, personnel.getEmail());

        return intent;
    }

    @Override
    protected Intent getResultIntent(AppUser personnel) {
        Intent result = new Intent();
        result.putExtra(AppConsts.COACH_EMAIL_EXTRA, personnel.getEmail());

        return result;
    }

    @Override
    protected String getSingularPersonnelCaption() {
        return ResUtils.getString(R.string.caption_coach);
    }

    @Override
    protected String getPluralPersonnelCaption() {
        return ResUtils.getString(R.string.caption_coaches);
    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_coach);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCoachesListDataListenerEvent(CoachesListDataListenerEvent coachesListDataListenerEvent) {
        viewModel.getPersonnelListData();
    }

    public void closeProgress() {

    }

    public void showProgress() {

    }
}
