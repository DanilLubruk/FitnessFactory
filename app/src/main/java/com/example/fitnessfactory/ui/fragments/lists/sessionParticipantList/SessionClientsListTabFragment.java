package com.example.fitnessfactory.ui.fragments.lists.sessionParticipantList;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.SessionsClientsListDataListenerEvent;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.adapters.ClientsListAdapter;
import com.example.fitnessfactory.ui.fragments.FragmentProvider;
import com.example.fitnessfactory.ui.fragments.lists.ListListenerTabFragment;
import com.example.fitnessfactory.ui.viewholders.lists.ClientsListViewHolder;
import com.example.fitnessfactory.ui.viewmodels.editors.SessionEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.ClientsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.factories.SessionEditorViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.sessionParticipantList.SessionClientsListTabViewModel;
import com.example.fitnessfactory.utils.ResUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

public class SessionClientsListTabFragment extends ListListenerTabFragment<AppUser, ClientsListViewHolder, ClientsListAdapter> {

    private SessionClientsListTabViewModel viewModel;

    private SessionEditorViewModel editorViewModel;

    @Inject
    SessionEditorViewModelFactory sessionEditorViewModelFactory;

    private final ActivityResultLauncher<Intent> openClientsSelection = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> editorViewModel.sessionId.observe(getViewLifecycleOwner(), sessionId -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String clientEmail = result.getData().getStringExtra(AppConsts.CLIENT_EMAIL_EXTRA);
                    getViewModel().addParticipantToSession(sessionId, clientEmail);
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
        FFApp.get().getAppComponent().inject(this);
        viewModel = new ViewModelProvider(this, new ClientsListTabViewModelFactory()).get(SessionClientsListTabViewModel.class);
        editorViewModel = new ViewModelProvider(this, sessionEditorViewModelFactory).get(SessionEditorViewModel.class);
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

            getBaseActivity().getIntent().putExtra(AppConsts.IS_SELECT_MODE_EXTRA, true);
            FragmentProvider.attachFragmentSelectActivity(getBaseActivity(), AppConsts.FRAGMENT_CLIENTS_ID);
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSessionsClientsListDataListenerEvent(SessionsClientsListDataListenerEvent sessionsClientsListDataListenerEvent) {
        getViewModel().resetClientsList(sessionsClientsListDataListenerEvent.getClientsEmails());
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        getViewModel().saveState(savedState);
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
