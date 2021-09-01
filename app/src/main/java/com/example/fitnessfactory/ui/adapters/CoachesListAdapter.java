package com.example.fitnessfactory.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.viewholders.lists.CoachesListViewHolder;

import java.util.List;

public class CoachesListAdapter extends RecyclerView.Adapter<CoachesListViewHolder> {

    private List<AppUser> coaches;
    private int layoutResource;

    public CoachesListAdapter(List<AppUser> coaches, int layoutResource) {
        this.coaches = coaches;
        this.layoutResource = layoutResource;
    }

    public AppUser getCoach(int position) {
        return coaches.get(position);
    }

    public void setCoaches(List<AppUser> coaches) {
        this.coaches = coaches;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CoachesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutResource, parent, false);
        return new CoachesListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CoachesListViewHolder holder, int position) {
        AppUser coach = coaches.get(position);
        holder.setData(coach);
    }

    @Override
    public int getItemCount() {
        return coaches.size();
    }

}
