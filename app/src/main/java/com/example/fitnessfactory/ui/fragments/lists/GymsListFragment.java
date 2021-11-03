package com.example.fitnessfactory.ui.fragments.lists;

import static com.example.fitnessfactory.data.ActivityRequestCodes.REQUEST_GYM_ID;
import static com.example.fitnessfactory.data.ActivityRequestCodes.REQUEST_GYM_NAME;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.ActivityRequestCodes;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.GymsListDataListenerEvent;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.ui.activities.editors.GymEditorActivity;
import com.example.fitnessfactory.ui.adapters.GymsListAdapter;
import com.example.fitnessfactory.ui.viewholders.lists.GymsListViewHolder;
import com.example.fitnessfactory.ui.viewmodels.factories.GymsListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.GymsListViewModel;
import com.example.fitnessfactory.utils.ResUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class GymsListFragment extends ListListenerSelectFragment<Gym, GymsListViewHolder, GymsListAdapter> {

    private GymsListViewModel viewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected String getSelectTitle() {
        return ResUtils.getString(R.string.title_select_gyms);
    }

    @Override
    protected String getListTitle() {
        return ResUtils.getString(R.string.title_gyms);
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
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_gym);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGymsListDataListenerEvent(GymsListDataListenerEvent gymsListDataListenerEvent) {
        setListData(gymsListDataListenerEvent.getGyms());
    }

    @Override
    protected Intent getResultIntent(Gym gym) {
        Intent result = new Intent();

        int requestCode = getBaseActivity().getIntent().getIntExtra(AppConsts.REQUEST_CODE, REQUEST_GYM_ID);
        switch (requestCode) {
            case REQUEST_GYM_ID:
                result.putExtra(AppConsts.GYM_ID_EXTRA, gym.getId());
                break;
            case REQUEST_GYM_NAME:
                result.putExtra(AppConsts.GYM_NAME_EXTRA, gym.getName());
                break;
        }

        return result;
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
}
