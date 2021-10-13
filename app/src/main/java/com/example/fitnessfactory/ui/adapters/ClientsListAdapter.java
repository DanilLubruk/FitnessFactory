package com.example.fitnessfactory.ui.adapters;

import android.view.View;
import com.example.fitnessfactory.data.models.Client;
import com.example.fitnessfactory.ui.viewholders.lists.ClientsListViewHolder;

import java.util.List;

public class ClientsListAdapter extends ListAdapter<Client, ClientsListViewHolder> {

    public ClientsListAdapter(List<Client> listData, int layoutResource) {
        super(listData, layoutResource);
    }

    @Override
    protected ClientsListViewHolder getNewViewHolder(View itemView) {
        return new ClientsListViewHolder(itemView);
    }
}
