package com.example.fitnessfactory.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.viewholders.lists.AdminsListViewHolder;

import java.util.ArrayList;
import java.util.List;

public class AdminsListAdapter extends RecyclerView.Adapter<AdminsListViewHolder> {

    private List<AppUser> admins;

    public AdminsListAdapter(List<AppUser> admins) {
        this.admins = admins;
    }

    public AppUser getAdmin(int position) {
        return admins.get(position);
    }

    public void setAdmins(List<AppUser> admins) {
        this.admins = admins;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdminsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gyms_list_item_view, parent, false);
        return new AdminsListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminsListViewHolder holder, int position) {
        AppUser admin = admins.get(position);
        holder.setData(admin);
    }

    @Override
    public int getItemCount() {
        return admins.size();
    }
}
