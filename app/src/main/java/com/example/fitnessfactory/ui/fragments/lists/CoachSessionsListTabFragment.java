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
import com.example.fitnessfactory.data.events.CoachDaysSessionsListDataListenerEvent;
import com.example.fitnessfactory.data.events.PersonnelEmailUpdateEvent;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.observers.SingleDialogEvent;
import com.example.fitnessfactory.data.views.SessionView;
import com.example.fitnessfactory.databinding.TabFragmentCoachSessionsListBinding;
import com.example.fitnessfactory.ui.activities.editors.SessionEditorActivity;
import com.example.fitnessfactory.ui.adapters.SessionsListAdapter;
import com.example.fitnessfactory.ui.viewholders.lists.SessionsListViewHolder;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachSessionsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.CoachSessionsListTabViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;
import com.github.clans.fab.FloatingActionButton;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class CoachSessionsListTabFragment extends ListListenerTabFragment<SessionView, SessionsListViewHolder, SessionsListAdapter> {

    private CoachSessionsListTabViewModel viewModel;
    private TabFragmentCoachSessionsListBinding tabBinding;

    @Override
    protected CoachSessionsListTabViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void defineViewModel() {
        viewModel = new ViewModelProvider(this, new CoachSessionsListTabViewModelFactory()).get(CoachSessionsListTabViewModel.class);
    }

    protected void initComponents() {
        super.initComponents();
        tabBinding.setModel(getViewModel());
        getFAB().setVisibility(View.GONE);
        tabBinding.edtDate.setOnClickListener(view -> trySelectDate());
        getViewModel().setDefaultDate();
        getViewModel().getSessions().observe(this, this::setListData);
        getViewModel().getDate().observe(this, date -> getViewModel().startDataListener());
    }

    private void trySelectDate() {
        getViewModel().changeSessionDate(new SingleDialogEvent<>(getBaseActivity(), DialogUtils::showDateSelectDialog));
    }

    @Override
    protected void initBinding(LayoutInflater inflater, ViewGroup container) {
        tabBinding = TabFragmentCoachSessionsListBinding.inflate(inflater, container, false);
    }

    @Override
    protected View getRootView() {
        return tabBinding.getRoot();
    }

    protected RecyclerView getRecyclerView() {
        return tabBinding.listContainer.rvData;
    }

    protected FloatingActionButton getFAB() {
        return tabBinding.listContainer.fabAddItem;
    }

    protected ProgressBar getProgressBar() {
        return tabBinding.listContainer.pkProgress;
    }

    protected TextView getEmptyDataLabel() {
        return tabBinding.listContainer.tvEmptyData;
    }

    @Override
    protected SessionsListAdapter createNewAdapter(List<SessionView> listData) {
        return new SessionsListAdapter(listData, R.layout.coach_sessions_list_item_view);
    }

    @Override
    protected void onListRowClicked(SessionView sessionView) {
        showEditorActivity(sessionView);
    }

    @Override
    protected void showEditorActivity(SessionView item) {
        if (item == null) {
            GuiUtils.showMessage(ResUtils.getString(R.string.message_error_session_null));
            return;
        }
        Session session = item.getSession();
        if (session == null) {
            GuiUtils.showMessage(ResUtils.getString(R.string.message_error_session_null));
            return;
        }

        Intent intent = new Intent(getBaseActivity(), SessionEditorActivity.class);
        intent.putExtra(AppConsts.SESSION_ID_EXTRA, session.getId());

        startActivity(intent);
    }

    @Override
    protected SessionView getNewItem() {
        return null;
    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_remove_coach_from_session);
    }

    @Override
    protected void openSelectionActivity() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCoachDaysSessionsListDataListenerEvent(CoachDaysSessionsListDataListenerEvent sessionListDataListenerEvent) {
        viewModel.getSessionsData(sessionListDataListenerEvent.getSessions());
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPersonnelEmailUpdateEvent(PersonnelEmailUpdateEvent personnelEmailUpdateEvent) {
        viewModel.resetCoachEmail(personnelEmailUpdateEvent.getPersonnelEmail());
        viewModel.startDataListener();
    }

    public void closeProgress() {
        getProgressBar().setVisibility(View.GONE);
        getRecyclerView().setVisibility(View.VISIBLE);
        if (adapter != null) {
            getEmptyDataLabel().setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);

        }
    }

    public void showProgress() {
        getProgressBar().setVisibility(View.VISIBLE);
        getRecyclerView().setVisibility(View.GONE);
    }
}
