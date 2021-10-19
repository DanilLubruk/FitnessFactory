package com.example.fitnessfactory.ui.adapters;

import android.view.View;

import com.example.fitnessfactory.data.models.SessionType;
import com.example.fitnessfactory.ui.viewholders.lists.SessionTypesListViewHolder;

import java.util.List;

public class SessionTypesListAdapter extends ListAdapter<SessionType, SessionTypesListViewHolder> {

    public SessionTypesListAdapter(List<SessionType> listData, int layoutResource) {
        super(listData, layoutResource);
    }

    @Override
    protected SessionTypesListViewHolder getNewViewHolder(View itemView) {
        return new SessionTypesListViewHolder(itemView);
    }
}
