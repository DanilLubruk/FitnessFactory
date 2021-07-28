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

    public GymsListAdapter(List<Gym> gyms) {
        this.gyms = gyms;
    }

    public void setGyms(List<Gym> gyms) {
        this.gyms = gyms;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GymsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gyms_list_item_view, parent, false);
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
