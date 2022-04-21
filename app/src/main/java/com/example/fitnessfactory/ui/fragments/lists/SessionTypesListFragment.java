package com.example.fitnessfactory.ui.fragments.lists;

import android.content.Intent;

import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.SessionTypesListDataListenerEvent;
import com.example.fitnessfactory.data.models.SessionType;
import com.example.fitnessfactory.ui.activities.editors.session.SessionEditorViewModelFactoryProvider;
import com.example.fitnessfactory.ui.activities.editors.SessionTypeEditorActivity;
import com.example.fitnessfactory.ui.adapters.SessionTypesListAdapter;
import com.example.fitnessfactory.ui.viewholders.lists.SessionTypesListViewHolder;
import com.example.fitnessfactory.ui.viewmodels.editors.SessionEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.SessionTypesListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.SessionTypesListViewModel;
import com.example.fitnessfactory.utils.ResUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class SessionTypesListFragment extends
        ListListenerSelectFragment<SessionType, SessionTypesListViewHolder, SessionTypesListAdapter> {

    private SessionTypesListViewModel viewModel;

    private SessionEditorViewModel editorViewModel;

    @Override
    protected SessionTypesListViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected String getSelectTitle() {
        return ResUtils.getString(R.string.title_select_session_type);
    }

    @Override
    protected String getListTitle() {
        return ResUtils.getString(R.string.title_session_types);
    }

    @Override
    protected void defineViewModel() {
        FFApp.get().getAppComponent().inject(this);
        viewModel = new ViewModelProvider(this, new SessionTypesListViewModelFactory()).get(SessionTypesListViewModel.class);
        editorViewModel = new ViewModelProvider(
                this,
                SessionEditorViewModelFactoryProvider.getFactory())
                .get(SessionEditorViewModel.class);
    }

    @Override
    protected SessionTypesListAdapter createNewAdapter(List<SessionType> listData) {
        return new SessionTypesListAdapter(listData, R.layout.session_types_list_item_view);
    }

    @Override
    protected void sendSelectResult(SessionType sessionType) {
        editorViewModel.setSessionType(sessionType.getId());
        closeFragment();
    }

    @Override
    protected Intent getResultIntent(SessionType item) {
        Intent result = new Intent();
        result.putExtra(AppConsts.SESSION_TYPE_ID_EXTRA, item.getId());

        return result;
    }

    @Override
    protected void showEditorActivity(SessionType item) {
        Intent intent = new Intent(getBaseActivity(), SessionTypeEditorActivity.class);
        intent.putExtra(AppConsts.SESSION_TYPE_ID_EXTRA, item.getId());
        startActivity(intent);
    }

    @Override
    protected SessionType getNewItem() {
        return new SessionType();
    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_session_type);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSessionTypesListDataListenerEvent(SessionTypesListDataListenerEvent sessionTypesListDataListenerEvent) {
        setListData(sessionTypesListDataListenerEvent.getSessionTypes());
    }
}
