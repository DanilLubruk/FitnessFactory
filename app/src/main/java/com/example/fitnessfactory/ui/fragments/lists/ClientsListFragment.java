package com.example.fitnessfactory.ui.fragments.lists;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.ClientsListDataListenerEvent;
import com.example.fitnessfactory.data.models.Client;
import com.example.fitnessfactory.ui.activities.editors.ClientEditorActivity;
import com.example.fitnessfactory.ui.adapters.ClientsListAdapter;
import com.example.fitnessfactory.ui.viewholders.lists.ClientsListViewHolder;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.ui.viewmodels.factories.ClientsListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.ClientsListViewModel;
import com.example.fitnessfactory.utils.ResUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class ClientsListFragment extends ListListenerSelectFragment<Client, ClientsListViewHolder, ClientsListAdapter> {

    private ClientsListViewModel viewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected String getSelectTitle() {
        return ResUtils.getString(R.string.title_select_client);
    }

    @Override
    protected String getListTitle() {
        return ResUtils.getString(R.string.title_clients);
    }

    @Override
    protected ClientsListAdapter createNewAdapter(List<Client> listData) {
        return new ClientsListAdapter(listData, R.layout.two_bg_buttons_list_item_view);
    }

    @Override
    protected void defineViewModel() {
        viewModel = new ViewModelProvider(this, new ClientsListViewModelFactory()).get(ClientsListViewModel.class);
    }

    @Override
    protected Intent getResultIntent(Client item) {
        Intent intent = new Intent();
        intent.putExtra(AppConsts.CLIENT_ID_EXTRA, item.getId());

        return intent;
    }

    @Override
    protected void showEditorActivity(Client item) {
        Intent intent = new Intent(getBaseActivity(), ClientEditorActivity.class);
        intent.putExtra(AppConsts.CLIENT_ID_EXTRA, item.getId());
        startActivity(intent);
    }

    @Override
    protected Client getNewItem() {
        return new Client();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClientsListDataListenerEvent(ClientsListDataListenerEvent clientsListDataListenerEvent) {
        setListData(clientsListDataListenerEvent.getClientsList());
    }

    @Override
    protected DataListListener<Client> getViewModel() {
        return viewModel;
    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_client);
    }
}
