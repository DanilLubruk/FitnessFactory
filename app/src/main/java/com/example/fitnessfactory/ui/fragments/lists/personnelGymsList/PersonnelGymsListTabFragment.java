package com.example.fitnessfactory.ui.fragments.lists.personnelGymsList;

import static android.app.Activity.RESULT_OK;
import static com.example.fitnessfactory.data.ActivityRequestCodes.REQUEST_GYM_ID;
import static com.example.fitnessfactory.data.ActivityRequestCodes.REQUEST_PERSONNEL;

import android.content.Intent;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.ui.activities.SelectionActivity;
import com.example.fitnessfactory.ui.adapters.GymsListAdapter;
import com.example.fitnessfactory.ui.fragments.lists.ListListenerTabFragment;
import com.example.fitnessfactory.ui.viewholders.lists.GymsListViewHolder;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelGymList.PersonnelGymsListTabViewModel;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.List;

public abstract class PersonnelGymsListTabFragment extends ListListenerTabFragment<Gym, GymsListViewHolder, GymsListAdapter> {

    @Override
    protected abstract PersonnelGymsListTabViewModel getViewModel();

    @Override
    protected GymsListAdapter createNewAdapter(List<Gym> listData) {
        return new GymsListAdapter(listData, R.layout.one_bg_button_list_item_view);
    }

    protected void initComponents() {
        super.initComponents();
        getViewModel().getGyms().observe(getViewLifecycleOwner(), this::setListData);
    }

    @Override
    protected void onListRowClicked(Gym gym) {

    }

    @Override
    protected void showEditorActivity(Gym item) {

    }

    @Override
    protected Gym getNewItem() {
        return new Gym();
    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_remove_gym);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_GYM_ID:
                if (resultCode == RESULT_OK) {
                    String gymId = data.getStringExtra(AppConsts.GYM_ID_EXTRA);
                    //getViewModel().addGym(gymId);
                }
                break;
        }
    }
}
