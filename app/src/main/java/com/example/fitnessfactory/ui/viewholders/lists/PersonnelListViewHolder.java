package com.example.fitnessfactory.ui.viewholders.lists;
import android.view.View;
import android.widget.TextView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.viewholders.BaseRecyclerViewHolder;

public class PersonnelListViewHolder extends BaseRecyclerViewHolder<AppUser> {

    TextView tvName;
    TextView tvEmail;

    public PersonnelListViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(AppUser user) {
        tvName.setText(user.getName());
        tvEmail.setText(user.getEmail());
    }

    @Override
    protected void bindView(View itemView) {
        tvName = itemView.findViewById(R.id.tvName);
        tvEmail = itemView.findViewById(R.id.tvEmail);
    }
}
