package com.example.fitnessfactory.ui.fragments.lists;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.events.ClientsListDataListenerEvent;
import com.example.fitnessfactory.data.models.Client;
import com.example.fitnessfactory.ui.adapters.ClientsListAdapter;
import com.example.fitnessfactory.ui.viewholders.lists.ClientsListViewHolder;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.ui.viewmodels.factories.ClientsListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.ClientsListViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class ClientsListFragment extends ListListenerFragment<Client, ClientsListViewHolder, ClientsListAdapter> {

    private ClientsListViewModel viewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this, new ClientsListViewModelFactory()).get(ClientsListViewModel.class);
        getBaseActivity().setTitle(R.string.title_clients);
    }

    @Override
    protected ClientsListAdapter createNewAdapter(List<Client> listData) {
        return new ClientsListAdapter(listData, R.layout.two_bg_buttons_list_item_view);
    }

    @Override
    protected void onRowClicked(Client client) {

    }

    @Override
    protected void showEditorActivity(Client item) {

    }

    @Override
    protected Client getNewItem() {
        return new Client();
    }

    @Override
    public void closeProgress() {

    }

    @Override
    public void showProgress() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClientsListDataListenerEvent(ClientsListDataListenerEvent clientsListDataListenerEvent) {
        setListData(clientsListDataListenerEvent.getClientsList());
    }

    @Override
    protected void bindView(View itemView) {
        super.bindView(itemView);
    }

    @Override
    protected DataListListener<Client> getViewModel() {
        return viewModel;
    }

    @Override
    protected String getDeleteMessage() {
        return null;
    }
}
