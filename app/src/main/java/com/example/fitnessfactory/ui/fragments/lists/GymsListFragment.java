package com.example.fitnessfactory.ui.fragments.lists;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.ui.adapters.GymsListAdapter;
import com.example.fitnessfactory.ui.fragments.BaseFragment;
import com.example.fitnessfactory.utils.GuiUtils;
import com.github.clans.fab.FloatingActionMenu;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import java.util.List;

import butterknife.BindView;

public class GymsListFragment extends BaseFragment {

    @BindView(R.id.rvGyms)
    RecyclerView recyclerView;
    @BindView(R.id.fabAddGym)
    FloatingActionMenu fabAddGym;

    private GymsListAdapter adapter;
    private RecyclerTouchListener touchListener;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBaseActivity().setTitle(R.string.title_gyms);

    }

    public void initComponents() {
        GuiUtils.initListView(getBaseActivity(), recyclerView, true);
        touchListener = new RecyclerTouchListener(getBaseActivity(), recyclerView);
        //setGymsData();
    }

    public void setGymsData(List<Gym> gyms) {
        if (adapter == null) {
            adapter = new GymsListAdapter(gyms);
        }

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_main_menu;
    }
}
