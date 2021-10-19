package com.example.fitnessfactory.ui.viewholders.lists;

import android.view.View;
import android.widget.TextView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.ui.viewholders.BaseRecyclerViewHolder;

public class GymsListViewHolder extends BaseRecyclerViewHolder<Gym> {

    private TextView tvName;
    private TextView tvAddress;

    public GymsListViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Gym gym) {
        tvName.setText(gym.getName());
        tvAddress.setText(gym.getAddress());
    }

    @Override
    protected void bindView(View itemView) {
        tvName = itemView.findViewById(R.id.tvName);
        tvAddress = itemView.findViewById(R.id.tvEmail);
    }
}
