package com.example.fitnessfactory.ui.fragments.lists;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.ui.activities.editors.GymEditorActivity;
import com.example.fitnessfactory.ui.adapters.GymsListAdapter;
import com.example.fitnessfactory.ui.fragments.BaseFragment;
import com.example.fitnessfactory.ui.viewmodels.lists.GymsListViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.github.clans.fab.FloatingActionButton;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import java.util.List;

import butterknife.BindView;

public class GymsListFragment extends BaseFragment {

    @BindView(R.id.rvGyms)
    RecyclerView recyclerView;
    @BindView(R.id.fabAddGym)
    FloatingActionButton fabAddGym;

    private GymsListViewModel viewModel;
    private GymsListAdapter adapter;
    private RecyclerTouchListener touchListener;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBaseActivity().setTitle(R.string.title_gyms);
        viewModel = new ViewModelProvider(this).get(GymsListViewModel.class);
        initComponents();
    }

    private void initComponents() {
        fabAddGym.setOnClickListener(view -> showEditorActivity(new Gym()));
        GuiUtils.initListView(getBaseActivity(), recyclerView, true);
        touchListener = new RecyclerTouchListener(getBaseActivity(), recyclerView);
        recyclerView.addOnItemTouchListener(touchListener);
        touchListener.setSwipeOptionViews(R.id.btnEdit, R.id.btnDelete);
        touchListener.setSwipeable(R.id.rowFG, R.id.rowBG, (viewId, position) -> {
            switch (viewId) {
                case R.id.btnEdit:
                    Gym gym = adapter.getGym(position);
                    showEditorActivity(gym);
                    break;
                case R.id.btnDelete:

                    break;
            }
        });
        touchListener.setClickable(new RecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
                Gym gym = adapter.getGym(position);
                showEditorActivity(gym);
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        });
        viewModel.getGymsList().observe(this, this::setGymsData);
    }

    private void showEditorActivity(Gym gym) {
        Intent intent = new Intent(getBaseActivity(), GymEditorActivity.class);
        intent.putExtra(AppConsts.GYM_ID_EXTRA, gym.getId());
        startActivity(intent);
    }

    private void setGymsData(List<Gym> gyms) {
        if (adapter == null) {
            adapter = new GymsListAdapter(gyms);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setGyms(gyms);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.refreshGymsData();
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_gyms_list;
    }
}
