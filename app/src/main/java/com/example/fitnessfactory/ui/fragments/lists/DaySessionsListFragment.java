package com.example.fitnessfactory.ui.fragments.lists;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.DaysSessionListDataListenerEvent;
import com.example.fitnessfactory.data.observers.SingleDialogEvent;
import com.example.fitnessfactory.data.views.SessionView;
import com.example.fitnessfactory.databinding.FragmentDaysSessionsListBinding;
import com.example.fitnessfactory.ui.adapters.SessionsListAdapter;
import com.example.fitnessfactory.ui.viewholders.lists.SessionsListViewHolder;
import com.example.fitnessfactory.ui.viewmodels.factories.SessionsListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.SessionsListViewModel;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;
import com.github.clans.fab.FloatingActionMenu;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class DaySessionsListFragment extends ListListenerSelectFragment<SessionView, SessionsListViewHolder, SessionsListAdapter> {

    private FragmentDaysSessionsListBinding binding;
    private SessionsListViewModel viewModel;

    @Override
    protected SessionsListViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void defineViewModel() {
        viewModel = new ViewModelProvider(this, new SessionsListViewModelFactory()).get(SessionsListViewModel.class);
    }

    protected void initComponents() {
        super.initComponents();
        getFAB().setVisibility(View.GONE);
        touchListener.setSwipeable(false);
        getViewModel().getDate().observe(getBaseActivity(), date -> getViewModel().startDataListener());
        binding.edtDate.setOnClickListener(view -> trySelectDate());
    }

    @Override
    protected void initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentDaysSessionsListBinding.inflate(inflater, container, false);
    }

    private void trySelectDate() {
        getViewModel().changeSessionDate(new SingleDialogEvent<>(getBaseActivity(), DialogUtils::showDateSelectDialog));
    }

    @Override
    protected SessionsListAdapter createNewAdapter(List<SessionView> listData) {
        return new SessionsListAdapter(listData, R.layout.sessions_list_item_view);
    }

    @Override
    protected void showEditorActivity(SessionView item) { }

    @Override
    protected View getRootView() {
        return binding.getRoot();
    }

    protected RecyclerView getRecyclerView() {
        return binding.listContainer.rvData;
    }

    protected FloatingActionMenu getFAB() {
        return binding.listContainer.fabAddItem;
    }

    protected ProgressBar getProgressBar() {
        return binding.listContainer.pkProgress;
    }

    protected TextView getEmptyDataLabel() {
        return binding.listContainer.tvEmptyData;
    }

    @Override
    protected SessionView getNewItem() {
        return null;
    }

    @Override
    protected String getDeleteMessage() {
        return null;
    }

    @Override
    protected Intent getResultIntent(SessionView item) {
        Intent intent = new Intent();
        intent.putExtra(AppConsts.SESSION_ID_EXTRA, item.getId());

        return intent;
    }

    @Override
    protected String getSelectTitle() {
        return ResUtils.getString(R.string.title_select_session);
    }

    @Override
    protected String getListTitle() {
        return ResUtils.getString(R.string.title_sessions);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDaysSessionsListenerEvent(DaysSessionListDataListenerEvent sessionListDataListenerEvent) {
        viewModel.getSessionsData(sessionListDataListenerEvent.getSessions());
    }
}
