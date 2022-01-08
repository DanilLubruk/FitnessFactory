package com.example.fitnessfactory.ui.viewholders.lists;

import android.view.View;
import android.widget.TextView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.Client;
import com.example.fitnessfactory.ui.viewholders.BaseRecyclerViewHolder;

public class ClientsListViewHolder extends BaseRecyclerViewHolder<AppUser> {

    private TextView tvName;
    private TextView tvEmail;

    public ClientsListViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(AppUser data) {
        tvName.setText(data.getName());
        tvEmail.setText(data.getEmail());
    }

    @Override
    protected void bindView(View itemView) {
        tvName = itemView.findViewById(R.id.tvName);
        tvEmail = itemView.findViewById(R.id.tvEmail);
    }
}
