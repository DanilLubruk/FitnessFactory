package com.example.fitnessfactory.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.ui.viewholders.lists.GymsListViewHolder;

import java.util.List;

public class GymsListAdapter extends RecyclerView.Adapter<GymsListViewHolder> {

    private List<Gym> gyms;
    private final int layoutResource;

    public GymsListAdapter(List<Gym> gyms, int layoutResource) {
        this.gyms = gyms;
        this.layoutResource = layoutResource;
    }

    public void setGyms(List<Gym> gyms) {
        this.gyms = gyms;
        notifyDataSetChanged();
    }

    public Gym getGym(int position) {
        return gyms.get(position);
    }

    @NonNull
    @Override
    public GymsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutResource, parent, false);
        return new GymsListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GymsListViewHolder holder, int position) {
        Gym gym = gyms.get(position);
        holder.setData(gym);
    }

    @Override
    public int getItemCount() {
        return gyms.size();
    }
}
