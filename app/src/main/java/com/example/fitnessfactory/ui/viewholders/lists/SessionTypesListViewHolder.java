package com.example.fitnessfactory.ui.viewholders.lists;

import android.view.View;
import android.widget.TextView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.SessionType;
import com.example.fitnessfactory.ui.viewholders.BaseRecyclerViewHolder;

public class SessionTypesListViewHolder extends BaseRecyclerViewHolder<SessionType> {

    private TextView tvName;
    private TextView tvPeopleAmount;
    private TextView tvPrice;

    public SessionTypesListViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(SessionType data) {
        tvName.setText(data.getName());
        tvPeopleAmount.setText(data.getPeopleAmountString());
        tvPrice.setText(data.getPriceString());
    }

    @Override
    protected void bindView(View itemView) {
        tvName = itemView.findViewById(R.id.tvName);
        tvPeopleAmount = itemView.findViewById(R.id.tvPeopleAmount);
        tvPrice = itemView.findViewById(R.id.tvPrice);
    }
}
