package com.example.fitnessfactory.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.viewholders.lists.PersonnelListViewHolder;

import java.util.List;

public class PersonnelListAdapter extends RecyclerView.Adapter<PersonnelListViewHolder> {

    private List<AppUser> personnel;
    private final int layoutResoruce;

    public PersonnelListAdapter(List<AppUser> personnel, int layoutResoruce) {
        this.personnel = personnel;
        this.layoutResoruce = layoutResoruce;
    }

    public AppUser getPersonnel(int position) {
        return personnel.get(position);
    }

    public void setPersonnel(List<AppUser> personnel) {
        this.personnel = personnel;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PersonnelListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutResoruce, parent, false);
        return new PersonnelListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonnelListViewHolder holder, int position) {
        AppUser personnel = this.personnel.get(position);
        holder.setData(personnel);
    }

    @Override
    public int getItemCount() {
        return personnel.size();
    }
}
