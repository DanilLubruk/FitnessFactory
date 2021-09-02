package com.example.fitnessfactory.ui.viewholders.lists;

import android.view.View;
import android.widget.TextView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.ui.viewholders.BaseRecyclerViewHolder;

import butterknife.BindView;

public class GymsListViewHolder extends BaseRecyclerViewHolder<Gym> {

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvEmail)
    TextView tvAddress;

    public GymsListViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Gym gym) {
        tvName.setText(gym.getName());
        tvAddress.setText(gym.getAddress());
    }
}
