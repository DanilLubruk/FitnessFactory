package com.example.fitnessfactory.ui.fragments.lists.sessionParticipantList;

import static android.app.Activity.RESULT_OK;
import static com.example.fitnessfactory.data.ActivityRequestCodes.REQUEST_CLIENT;

import android.content.Intent;

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
        Intent intent = new Intent(getBaseActivity(), SelectionActivity.class);
        intent.putExtra(AppConsts.FRAGMENT_ID_EXTRA, AppConsts.FRAGMENT_CLIENTS_ID);

        startActivityForResult(intent, REQUEST_CLIENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CLIENT:
                if (resultCode == RESULT_OK) {
                    String clientEmail = data.getStringExtra(AppConsts.CLIENT_EMAIL_EXTRA);
                    getViewModel().addParticipantToSession(clientEmail);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSessionsClientsListDataListenerEvent(SessionsClientsListDataListenerEvent sessionsClientsListDataListenerEvent) {
        getViewModel().resetClientsList(sessionsClientsListDataListenerEvent.getClientsEmails());
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onSessionIdUpdateEvent(SessionIdUpdateEvent sessionIdUpdateEvent) {
        getViewModel().resetSessionId(sessionIdUpdateEvent.getSessionId());
        getViewModel().startDataListener();
    }
}
