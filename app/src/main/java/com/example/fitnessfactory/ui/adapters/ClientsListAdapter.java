package com.example.fitnessfactory.ui.adapters;

import android.view.View;

import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.Client;
import com.example.fitnessfactory.ui.viewholders.lists.ClientsListViewHolder;

import java.util.List;

public class ClientsListAdapter extends ListAdapter<AppUser, ClientsListViewHolder> {

    public ClientsListAdapter(List<AppUser> listData, int layoutResource) {
        super(listData, layoutResource);
    }

    @Override
    protected ClientsListViewHolder getNewViewHolder(View itemView) {
        return new ClientsListViewHolder(itemView);
    }
}
