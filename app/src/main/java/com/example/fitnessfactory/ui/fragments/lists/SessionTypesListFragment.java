package com.example.fitnessfactory.ui.fragments.lists;

import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.SessionType;
import com.example.fitnessfactory.ui.adapters.SessionTypesListAdapter;
import com.example.fitnessfactory.ui.viewholders.lists.SessionTypesListViewHolder;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.ui.viewmodels.factories.SessionTypesListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.SessionTypesListViewModel;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.List;

public class SessionTypesListFragment extends
        ListListenerFragment<SessionType, SessionTypesListViewHolder, SessionTypesListAdapter> {

    private SessionTypesListViewModel viewModel;

    @Override
    public void closeProgress() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    protected DataListListener<SessionType> getViewModel() {
        return viewModel;
    }

    @Override
    protected String getTitle() {
        return ResUtils.getString(R.string.title_session_types);
    }

    @Override
    protected void defineViewModel() {
        viewModel = new ViewModelProvider(this, new SessionTypesListViewModelFactory()).get(SessionTypesListViewModel.class);
    }

    @Override
    protected SessionTypesListAdapter createNewAdapter(List<SessionType> listData) {
        return new SessionTypesListAdapter(listData, R.layout.session_types_list_item_view);
    }

    @Override
    protected void onListRowClicked(SessionType sessionType) {

    }

    @Override
    protected void showEditorActivity(SessionType item) {

    }

    @Override
    protected SessionType getNewItem() {
        return new SessionType();
    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_session_type);
    }
}
