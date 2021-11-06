package com.example.fitnessfactory.ui.adapters;

import android.view.View;

import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.views.SessionView;
import com.example.fitnessfactory.ui.viewholders.lists.SessionsListViewHolder;

import java.util.List;

public class SessionsListAdapter extends ListAdapter<SessionView, SessionsListViewHolder> {

    public SessionsListAdapter(List<SessionView> sessionList, int layoutResource) {
        super(sessionList, layoutResource);
    }

    @Override
    protected SessionsListViewHolder getNewViewHolder(View itemView) {
        return new SessionsListViewHolder(itemView);
    }
}
