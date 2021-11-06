package com.example.fitnessfactory.ui.viewholders.lists;

import android.view.View;
import android.widget.TextView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.views.SessionView;
import com.example.fitnessfactory.ui.viewholders.BaseRecyclerViewHolder;

public class SessionsListViewHolder extends BaseRecyclerViewHolder<SessionView> {

    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvGymName;
    private TextView tvSessionTypeName;

    public SessionsListViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(SessionView data) {
        tvStartTime.setText(data.getStartTimeString());
        tvEndTime.setText(data.getEndTimeString());
        tvGymName.setText(data.getGymName());
        tvSessionTypeName.setText(data.getSessionTypeName());
    }

    @Override
    protected void bindView(View itemView) {
        tvStartTime = itemView.findViewById(R.id.tvStartTime);
        tvEndTime = itemView.findViewById(R.id.tvEndTime);
        tvGymName = itemView.findViewById(R.id.tvGymName);
        tvSessionTypeName = itemView.findViewById(R.id.tvSessionTypeName);
    }
}
