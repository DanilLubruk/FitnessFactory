package com.example.fitnessfactory.ui.fragments.lists;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.GymsListDataListenerEvent;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.ui.activities.editors.GymEditorActivity;
import com.example.fitnessfactory.ui.adapters.GymsListAdapter;
import com.example.fitnessfactory.ui.adapters.ListAdapter;
import com.example.fitnessfactory.ui.viewholders.lists.GymsListViewHolder;
import com.example.fitnessfactory.ui.viewmodels.factories.GymsListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.GymsListViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class GymsListFragment extends ListListenerFragment<Gym, GymsListViewHolder, GymsListAdapter> {

    private GymsListViewModel viewModel;
    private boolean selectMode = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected String getTitle() {
        return selectMode ?
                ResUtils.getString(R.string.title_select_gyms)
                : ResUtils.getString(R.string.title_gyms);
    }

    @Override
    protected void defineViewModel() {
        viewModel = new ViewModelProvider(this, new GymsListViewModelFactory()).get(GymsListViewModel.class);
    }

    @Override
    protected GymsListViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        if (getBaseActivity().getIntent().hasExtra(AppConsts.IS_SELECT_MODE_EXTRA)) {
            selectMode = getBaseActivity().getIntent().getBooleanExtra(AppConsts.IS_SELECT_MODE_EXTRA, false);
        }
    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_gym);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGymsListDataListenerEvent(GymsListDataListenerEvent gymsListDataListenerEvent) {
        setListData(gymsListDataListenerEvent.getGyms());
    }

    @Override
    protected void onListRowClicked(Gym gym) {
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

    @Override
    protected void showEditorActivity(Gym gym) {
        Intent intent = new Intent(getBaseActivity(), GymEditorActivity.class);
        intent.putExtra(AppConsts.GYM_ID_EXTRA, gym.getId());
        startActivity(intent);
    }

    @Override
    protected Gym getNewItem() {
        return new Gym();
    }

    @Override
    protected GymsListAdapter createNewAdapter(List<Gym> listData) {
        return new GymsListAdapter(listData, R.layout.two_bg_buttons_list_item_view);
    }

    public void closeProgress() {

    }

    public void showProgress() {

    }

    @Override
    protected void bindView(View itemView) {
        super.bindView(itemView);
    }
}
