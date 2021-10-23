package com.example.fitnessfactory.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitnessfactory.ui.viewholders.BaseRecyclerViewHolder;

import java.util.List;

public abstract class ListAdapter<ItemType, ViewHolderType extends BaseRecyclerViewHolder<ItemType>>
        extends RecyclerView.Adapter<ViewHolderType> {

    private List<ItemType> listData;
    private final int layoutResource;

    public ListAdapter(List<ItemType> listData, int layoutResource) {
        this.listData = listData;
        this.layoutResource = layoutResource;
    }

    public void setListData(List<ItemType> listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

    public ItemType getItem(int position) {
        return listData.get(position);
    }

    @NonNull
    @Override
    public ViewHolderType onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutResource, parent, false);
        return getNewViewHolder(itemView);
    }

    protected abstract ViewHolderType getNewViewHolder(View itemView);

    @Override
    public void onBindViewHolder(@NonNull ViewHolderType holder, int position) {
        holder.setData(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
