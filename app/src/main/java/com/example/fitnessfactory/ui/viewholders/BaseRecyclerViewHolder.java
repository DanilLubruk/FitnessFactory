package com.example.fitnessfactory.ui.viewholders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        bindView(itemView);
    }

    public abstract void setData(T data);

    protected abstract void bindView(View itemView);
}