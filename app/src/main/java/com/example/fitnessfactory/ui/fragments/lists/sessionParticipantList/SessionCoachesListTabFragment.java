package com.example.fitnessfactory.ui.fragments.lists.sessionParticipantList;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.SessionsCoachesListDataListenerEvent;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.adapters.PersonnelListAdapter;
import com.example.fitnessfactory.ui.fragments.FragmentProvider;
import com.example.fitnessfactory.ui.fragments.lists.ListListenerTabFragment;
import com.example.fitnessfactory.ui.viewholders.lists.PersonnelListViewHolder;
import com.example.fitnessfactory.ui.viewmodels.editors.SessionEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.SessionCoachesListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.SessionEditorViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.sessionParticipantList.SessionCoachesListTabViewModel;
import com.example.fitnessfactory.utils.ResUtils;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

public class SessionCoachesListTabFragment extends ListListenerTabFragment<AppUser, PersonnelListViewHolder, PersonnelListAdapter> {

    private SessionCoachesListTabViewModel tabViewModel;

    private SessionEditorViewModel editorViewModel;

    @Inject
    SessionEditorViewModelFactory sessionEditorViewModelFactory;

    private final ActivityResultLauncher<Intent> openCoachesSelection = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String sessionId = result.getData().getStringExtra(AppConsts.SESSION_ID_EXTRA);
                    String coachEmail = result.getData().getStringExtra(AppConsts.COACH_EMAIL_EXTRA);
                    getViewModel().addParticipantToSession(sessionId, coachEmail);
                }
            }
    );

    protected void initComponents() {
        super.initComponents();
        tabViewModel.getCoaches().observe(this, this::setListData);
    }

    @Override
    protected SessionCoachesListTabViewModel getViewModel() {
        return tabViewModel;
    }

    @Override
    protected void defineViewModel() {
        FFApp.get().getAppComponent().inject(this);
        tabViewModel = new ViewModelProvider(this, new SessionCoachesListTabViewModelFactory()).get(SessionCoachesListTabViewModel.class);
        editorViewModel = new ViewModelProvider(this, sessionEditorViewModelFactory).get(SessionEditorViewModel.class);
    }

    @Override
    protected PersonnelListAdapter createNewAdapter(List<AppUser> listData) {
        return new PersonnelListAdapter(listData, R.layout.one_bg_button_list_item_view);
    }

    @Override
    protected void onListRowClicked(AppUser appUser) {

    }

    @Override
    protected void showEditorActivity(AppUser item) {

    }

    @Override
    protected AppUser getNewItem() {
        return new AppUser();
    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_remove_coach_from_session);
    }

    @Override
    protected void openSelectionActivity() {
        getBaseActivity().save(isSaved -> {
            if (!isSaved) {
                return;
            }
            getBaseActivity().getIntent().putExtra(AppConsts.IS_SELECT_MODE_EXTRA, true);
            FragmentProvider.attachFragment(getBaseActivity(), AppConsts.FRAGMENT_COACHES_ID);
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSessionsCoachesListDataListenerEvent(SessionsCoachesListDataListenerEvent sessionsCoachesListDataListenerEvent) {
        getViewModel().resetCoachesList(sessionsCoachesListDataListenerEvent.getCoachesIds());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getViewModel() != null) {
            editorViewModel.sessionId.observe(this, sessionId -> getViewModel().startDataListener(sessionId));
        } else {
            closeProgress();
        }
    }

    protected void deleteItem(AppUser item) {
        editorViewModel.sessionId.observe(this, sessionId -> getViewModel().deleteItem(sessionId, item));
    }
}
