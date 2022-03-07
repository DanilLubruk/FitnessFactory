package com.example.fitnessfactory.ui.fragments.lists.sessionParticipantList;

import static android.app.Activity.RESULT_OK;
import static com.example.fitnessfactory.data.ActivityRequestCodes.REQUEST_COACH;

import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.SessionIdUpdateEvent;
import com.example.fitnessfactory.data.events.SessionsCoachesListDataListenerEvent;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.activities.SelectionActivity;
import com.example.fitnessfactory.ui.adapters.PersonnelListAdapter;
import com.example.fitnessfactory.ui.fragments.lists.ListListenerTabFragment;
import com.example.fitnessfactory.ui.viewholders.lists.PersonnelListViewHolder;
import com.example.fitnessfactory.ui.viewmodels.factories.SessionCoachesListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.sessionParticipantList.SessionCoachesListTabViewModel;
import com.example.fitnessfactory.utils.ResUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class SessionCoachesListTabFragment extends ListListenerTabFragment<AppUser, PersonnelListViewHolder, PersonnelListAdapter> {

    private SessionCoachesListTabViewModel viewModel;

    private final ActivityResultLauncher<Intent> openCoachesSelection = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                getViewModel().resetSessionId(getSessionId());
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String coachEmail = result.getData().getStringExtra(AppConsts.COACH_EMAIL_EXTRA);
                    getViewModel().addParticipantToSession(coachEmail);
                }
            }
    );

    protected void initComponents() {
        super.initComponents();
        viewModel.getCoaches().observe(this, this::setListData);
    }

    @Override
    protected SessionCoachesListTabViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void defineViewModel() {
        viewModel = new ViewModelProvider(this, new SessionCoachesListTabViewModelFactory()).get(SessionCoachesListTabViewModel.class);
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

            Intent intent = new Intent(getBaseActivity(), SelectionActivity.class);
            intent.putExtra(AppConsts.FRAGMENT_ID_EXTRA, AppConsts.FRAGMENT_COACHES_ID);

            openCoachesSelection.launch(intent);
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSessionsCoachesListDataListenerEvent(SessionsCoachesListDataListenerEvent sessionsCoachesListDataListenerEvent) {
        getViewModel().resetCoachesList(sessionsCoachesListDataListenerEvent.getCoachesIds());
    }

    private boolean isSessionIdRegistered() {
        return getSessionId() != null && !getSessionId().isEmpty();
    }

    private String getSessionId() {
        return getBaseActivity().getIntent().getStringExtra(AppConsts.SESSION_ID_EXTRA);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onSessionIdUpdateEvent(SessionIdUpdateEvent sessionIdUpdateEvent) {
        getViewModel().resetSessionId(getSessionId());
        getViewModel().startDataListener();
    }
}
