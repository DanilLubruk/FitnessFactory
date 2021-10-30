package com.example.fitnessfactory.ui.fragments.lists;

import static android.app.Activity.RESULT_OK;
import static com.example.fitnessfactory.data.ActivityRequestCodes.REQUEST_CLIENT;
import static com.example.fitnessfactory.data.ActivityRequestCodes.REQUEST_GYM_ID;

import android.content.Intent;

import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.models.Client;
import com.example.fitnessfactory.ui.activities.SelectionActivity;
import com.example.fitnessfactory.ui.adapters.ClientsListAdapter;
import com.example.fitnessfactory.ui.viewholders.lists.ClientsListViewHolder;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.ui.viewmodels.factories.ClientsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.ClientsListTabViewModel;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.List;

public class ClientsListTabFragment extends ListListenerTabFragment<Client, ClientsListViewHolder, ClientsListAdapter> {

    private ClientsListTabViewModel viewModel;

    @Override
    public void closeProgress() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    protected ClientsListTabViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected String getTitle() {
        return null;
    }

    @Override
    protected void defineViewModel() {
        viewModel = new ViewModelProvider(this, new ClientsListTabViewModelFactory()).get(ClientsListTabViewModel.class);
    }

    @Override
    protected ClientsListAdapter createNewAdapter(List<Client> listData) {
        return new ClientsListAdapter(listData, R.layout.one_bg_button_list_item_view);
    }

    @Override
    protected void onListRowClicked(Client client) {

    }

    @Override
    protected void showEditorActivity(Client item) {

    }

    @Override
    protected Client getNewItem() {
        return new Client();
    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_remove_client_from_session);
    }

    @Override
    protected void refreshParentData() {

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
            case REQUEST_GYM_ID:
                if (resultCode == RESULT_OK) {
                    String clientId = data.getStringExtra(AppConsts.CLIENT_ID_EXTRA);
                    String sessionId = getBaseActivity().getIntent().getStringExtra(AppConsts.SESSION_ID_EXTRA);
                    getViewModel().addClientToSession(sessionId, clientId);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
