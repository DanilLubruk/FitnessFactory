package com.example.fitnessfactory.ui.viewholders.lists;
import android.view.View;
import android.widget.TextView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.viewholders.BaseRecyclerViewHolder;

import butterknife.BindView;

public class PersonnelListViewHolder extends BaseRecyclerViewHolder<AppUser> {

    @BindView(R.id.tvFirstLine)
    TextView tvName;
    @BindView(R.id.tvSecondLine)
    TextView tvEmail;

    public PersonnelListViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(AppUser user) {
        tvName.setText(user.getName());
        tvEmail.setText(user.getEmail());
    }
}
