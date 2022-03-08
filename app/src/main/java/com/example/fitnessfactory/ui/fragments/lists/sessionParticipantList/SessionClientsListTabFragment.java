package com.example.fitnessfactory.ui.fragments.lists.sessionParticipantList;

import static android.app.Activity.RESULT_OK;
import static com.example.fitnessfactory.data.ActivityRequestCodes.REQUEST_CLIENT;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.SessionIdUpdateEvent;
import com.example.fitnessfactory.data.events.SessionsClientsListDataListenerEvent;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.activities.SelectionActivity;
import com.example.fitnessfactory.ui.adapters.ClientsListAdapter;
import com.example.fitnessfactory.ui.fragments.lists.ListListenerTabFragment;
import com.example.fitnessfactory.ui.viewholders.lists.ClientsListViewHolder;
import com.example.fitnessfactory.ui.viewmodels.factories.ClientsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.sessionParticipantList.SessionClientsListTabViewModel;
import com.example.fitnessfactory.utils.ResUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class SessionClientsListTabFragment extends ListListenerTabFragment<AppUser, ClientsListViewHolder, ClientsListAdapter> {

    private SessionClientsListTabViewModel viewModel;

    private final ActivityResultLauncher<Intent> openClientsSelection = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> getBaseActivity().getItemId().observe(getViewLifecycleOwner(), (sessionId) -> {
                getViewModel().resetSessionId(sessionId);
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String clientEmail = result.getData().getStringExtra(AppConsts.CLIENT_EMAIL_EXTRA);
                    getViewModel().addParticipantToSession(clientEmail);
                }
            })
    );

    protected void initComponents() {
        super.initComponents();
        viewModel.getClients().observe(this, this::setListData);
    }

    @Override
    protected SessionClientsListTabViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void defineViewModel() {
        viewModel = new ViewModelProvider(this, new ClientsListTabViewModelFactory()).get(SessionClientsListTabViewModel.class);
    }

    @Override
    protected ClientsListAdapter createNewAdapter(List<AppUser> listData) {
        return new ClientsListAdapter(listData, R.layout.one_bg_button_list_item_view);
    }

    @Override
    protected void onListRowClicked(AppUser client) {

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
        return ResUtils.getString(R.string.message_ask_remove_client_from_session);
    }

    @Override
    protected void openSelectionActivity() {
        getBaseActivity().save(isSaved -> {
            if (!isSaved) {
                return;
            }

            Intent intent = new Intent(getBaseActivity(), SelectionActivity.class);
            intent.putExtra(AppConsts.FRAGMENT_ID_EXTRA, AppConsts.FRAGMENT_CLIENTS_ID);

            openClientsSelection.launch(intent);
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSessionsClientsListDataListenerEvent(SessionsClientsListDataListenerEvent sessionsClientsListDataListenerEvent) {
        getViewModel().resetClientsList(sessionsClientsListDataListenerEvent.getClientsEmails());
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onSessionIdUpdateEvent(SessionIdUpdateEvent sessionIdUpdateEvent) {
        getViewModel().resetSessionId(getSessionId());
        getViewModel().startDataListener();
    }

    private boolean isSessionIdRegistered() {
        return getSessionId() != null && !getSessionId().isEmpty();
    }

    private String getSessionId() {
        return getBaseActivity().getIntent().getStringExtra(AppConsts.SESSION_ID_EXTRA);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        getViewModel().saveState(savedState);
    }
}
