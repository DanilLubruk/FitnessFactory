package com.example.fitnessfactory.ui.fragments.lists.personnelList;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.events.CoachesListDataListenerEvent;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.databinding.FragmentCoachesSearchListBinding;
import com.example.fitnessfactory.ui.activities.editors.coach.CoachEditorActivity;
import com.example.fitnessfactory.ui.activities.editors.session.SessionEditorViewModelFactoryProvider;
import com.example.fitnessfactory.ui.viewmodels.editors.SessionEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachesListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.SessionCoachesListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelList.CoachesListViewModel;
import com.example.fitnessfactory.ui.viewmodels.lists.sessionParticipantList.SessionCoachesListTabViewModel;
import com.example.fitnessfactory.utils.ResUtils;
import com.github.clans.fab.FloatingActionMenu;
import com.tiromansev.prefswrapper.typedprefs.BooleanPreference;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CoachesListFragment extends PersonnelListFragment {

    private CoachesListViewModel viewModel;

    private SessionCoachesListTabViewModel tabViewModel;

    private SessionEditorViewModel editorViewModel;

    public FragmentCoachesSearchListBinding binding;

    @Override
    protected void initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentCoachesSearchListBinding.inflate(inflater, container, false);
    }

    @Override
    protected View getRootView() {
        return binding.getRoot();
    }

    protected RecyclerView getRecyclerView() {
        return binding.rvData;
    }

    protected FloatingActionMenu getFAB() {
        return binding.fabAddItem;
    }

    protected ProgressBar getProgressBar() {
        return binding.pkProgress;
    }

    protected TextView getEmptyDataLabel() {
        return binding.tvEmptyData;
    }

    protected AppCompatEditText getEdtSearch() {
        return binding.edtSearch;
    }

    protected LinearLayoutCompat getLlSearch() {
        return binding.llSearch;
    }

    protected ImageButton getIbSearchOptions() {
        return binding.ibSearchOptions;
    }

    @Override
    protected void defineViewModel() {
        FFApp.get().getAppComponent().inject(this);
        viewModel = new ViewModelProvider(this, new CoachesListViewModelFactory()).get(CoachesListViewModel.class);
        tabViewModel = new ViewModelProvider(this, new SessionCoachesListTabViewModelFactory()).get(SessionCoachesListTabViewModel.class);
        editorViewModel = new ViewModelProvider(
                this,
                SessionEditorViewModelFactoryProvider.getFactory())
                .get(SessionEditorViewModel.class);
        binding.setModel(viewModel);
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
    protected void sendSelectResult(AppUser personnel) {
        editorViewModel.sessionId.observe(this, sessionId -> {
            tabViewModel.addParticipantToSession(sessionId, personnel.getId()).observe(this,
                    isSaved -> closeFragment());
        });
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
}
