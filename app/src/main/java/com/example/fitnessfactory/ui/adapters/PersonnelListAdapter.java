package com.example.fitnessfactory.ui.adapters;

import android.view.View;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.viewholders.lists.PersonnelListViewHolder;

import java.util.List;

public class PersonnelListAdapter extends ListAdapter<AppUser, PersonnelListViewHolder> {

    public PersonnelListAdapter(List<AppUser> listData, int layoutResource) {
        super(listData, layoutResource);
    }

    @Override
    protected PersonnelListViewHolder getNewViewHolder(View itemView) {
        return new PersonnelListViewHolder(itemView);
    }
}
