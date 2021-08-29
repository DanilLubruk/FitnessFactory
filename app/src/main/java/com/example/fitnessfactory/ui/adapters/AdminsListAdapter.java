package com.example.fitnessfactory.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.viewholders.lists.AdminsListViewHolder;
import java.util.List;

public class AdminsListAdapter extends RecyclerView.Adapter<AdminsListViewHolder> {

    private List<AppUser> admins;
    private final int layoutResoruce;

    public AdminsListAdapter(List<AppUser> admins, int layoutResoruce) {
        this.admins = admins;
        this.layoutResoruce = layoutResoruce;
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
                .inflate(layoutResoruce, parent, false);
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
