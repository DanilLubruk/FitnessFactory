package com.example.fitnessfactory.ui.fragments.lists;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.GymsListDataListenerEvent;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.ui.activities.editors.AdminEditorActivity;
import com.example.fitnessfactory.ui.activities.editors.GymEditorActivity;
import com.example.fitnessfactory.ui.adapters.GymsListAdapter;
import com.example.fitnessfactory.ui.fragments.BaseFragment;
import com.example.fitnessfactory.ui.viewmodels.lists.GymsListViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;
import com.github.clans.fab.FloatingActionButton;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private boolean selectMode = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(GymsListViewModel.class);
        initComponents();
        getBaseActivity().setTitle(selectMode ? R.string.title_select_gyms : R.string.title_gyms);
    }

    private void initComponents() {
        if (getBaseActivity().getIntent().hasExtra(AppConsts.IS_SELECT_MODE_EXTRA)) {
            selectMode = getBaseActivity().getIntent().getBooleanExtra(AppConsts.IS_SELECT_MODE_EXTRA, false);
        }
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
                    gym = adapter.getGym(position);
                    askForDelete(gym);
                    break;
            }
        });
        touchListener.setClickable(new RecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
                Gym gym = adapter.getGym(position);
                GymsListFragment.this.onRowClicked(gym);
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        });
    }

    private void askForDelete(Gym gym) {
        subscribeInMainThread(
                DialogUtils.showAskDialog(
                        getBaseActivity(),
                        getDeleteMessage(),
                        ResUtils.getString(R.string.caption_ok),
                        ResUtils.getString(R.string.caption_cancel)),
                new SingleData<>(
                        doDelete -> {
                            if (doDelete) {
                                deleteGym(gym);
                            }
                        },
                        throwable -> {
                            throwable.printStackTrace();
                            GuiUtils.showMessage(throwable.getLocalizedMessage());
                        }
                ));
    }

    private void deleteGym(Gym gym) {
        viewModel.deleteGym(gym);
    }

    private String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_gym);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGymsListDataListenerEvent(GymsListDataListenerEvent gymsListDataListenerEvent) {
        setGymsData(gymsListDataListenerEvent.getGyms());
    }

    private void onRowClicked(Gym gym) {
        if (selectMode) {
            sendSelectResult(gym);
        } else {
            showEditorActivity(gym);
        }
    }

    private void sendSelectResult(Gym gym) {
        Intent result = new Intent();
        result.putExtra(AppConsts.GYM_ID_EXTRA, gym.getId());
        getBaseActivity().setResult(Activity.RESULT_OK, result);
        getBaseActivity().finish();
    }

    private void showEditorActivity(Gym gym) {
        Intent intent = new Intent(getBaseActivity(), GymEditorActivity.class);
        intent.putExtra(AppConsts.GYM_ID_EXTRA, gym.getId());
        startActivity(intent);
    }

    private void setGymsData(List<Gym> gyms) {
        if (adapter == null) {
            adapter = new GymsListAdapter(gyms, R.layout.gyms_list_item_view);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setGyms(gyms);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        viewModel.addGymsListDataListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        viewModel.removeGymsListDataListener();
    }


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_gyms_list;
    }
}
