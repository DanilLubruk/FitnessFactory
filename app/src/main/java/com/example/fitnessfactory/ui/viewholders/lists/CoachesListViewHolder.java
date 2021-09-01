package com.example.fitnessfactory.ui.viewholders.lists;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.viewholders.BaseRecyclerViewHolder;

import butterknife.BindView;

public class CoachesListViewHolder extends BaseRecyclerViewHolder<AppUser> {

    @BindView(R.id.tvFirstLine)
    TextView tvName;
    @BindView(R.id.tvSecondLine)
    TextView tvEmail;

    public CoachesListViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void setData(AppUser coach) {
        tvName.setText(coach.getName());
        tvEmail.setText(coach.getEmail());
    }
}
