package com.example.fitnessfactory.ui.fragments.lists;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.fragments.ListListenerFragment;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.utils.ResUtils;

public class CoachesListFragment extends ListListenerFragment<AppUser> {

    @Override
    protected DataListListener getViewModel() {
        return null;
    }

    public void closeProgress() {

    }

    public void showProgress() {

    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_coach);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_list;
    }
}
