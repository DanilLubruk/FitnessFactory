package com.example.fitnessfactory.ui.adapters;

import android.view.View;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.ui.viewholders.lists.GymsListViewHolder;

import java.util.List;

public class GymsListAdapter extends ListAdapter<Gym, GymsListViewHolder> {

    public GymsListAdapter(List<Gym> listData, int layoutResource) {
        super(listData, layoutResource);
    }

    @Override
    protected GymsListViewHolder getNewViewHolder(View itemView) {
        return new GymsListViewHolder(itemView);
    }
}
