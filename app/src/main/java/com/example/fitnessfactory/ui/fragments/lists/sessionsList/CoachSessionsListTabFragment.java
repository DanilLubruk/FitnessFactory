package com.example.fitnessfactory.ui.fragments.lists.sessionsList;

import static android.app.Activity.RESULT_OK;
import static com.example.fitnessfactory.data.ActivityRequestCodes.REQUEST_SESSION;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.CoachDaysSessionsListDataListenerEvent;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.observers.SingleDialogEvent;
import com.example.fitnessfactory.data.views.SessionView;
import com.example.fitnessfactory.databinding.FragmentDaysSessionsListBinding;
import com.example.fitnessfactory.ui.activities.SelectionActivity;
import com.example.fitnessfactory.ui.activities.editors.coach.CoachEditorViewModelFactoryProvider;
import com.example.fitnessfactory.ui.activities.editors.session.SessionEditorActivity;
import com.example.fitnessfactory.ui.adapters.SessionsListAdapter;
import com.example.fitnessfactory.ui.fragments.lists.ListListenerTabFragment;
import com.example.fitnessfactory.ui.viewholders.lists.SessionsListViewHolder;
import com.example.fitnessfactory.ui.viewmodels.editors.CoachEditorViewModel;
import com.example.fitnessfactory.ui.viewmodels.factories.CoachSessionsListTabViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.CoachSessionsListTabViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;
import com.github.clans.fab.FloatingActionMenu;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class CoachSessionsListTabFragment extends ListListenerTabFragment<SessionView, SessionsListViewHolder, SessionsListAdapter> {

    private CoachSessionsListTabViewModel viewModel;
    private FragmentDaysSessionsListBinding tabBinding;

    @Override
    protected CoachSessionsListTabViewModel getViewModel() {
        return viewModel;
    }

    private CoachEditorViewModel editorViewModel;

    @Override
    protected void defineViewModel() {
        FFApp.get().getAppComponent().inject(this);
        viewModel = new ViewModelProvider(this, new CoachSessionsListTabViewModelFactory()).get(CoachSessionsListTabViewModel.class);
        editorViewModel = new ViewModelProvider(
                this,
                CoachEditorViewModelFactoryProvider.getFactory())
                .get(CoachEditorViewModel.class);
    }

    protected void initComponents() {
        super.initComponents();
        tabBinding.setModel(getViewModel());
        tabBinding.edtDate.setOnClickListener(view -> trySelectDate());
        getViewModel().setDefaultDate();
        getViewModel().getSessions().observe(this, this::setListData);
        getViewModel().getDate().observe(this, date -> {
            editorViewModel.personnelId.observe(this, email -> {
                getViewModel().startDataListener(email);
            });
        });
        getFAB().setVisibility(View.GONE);
    }

    private void trySelectDate() {
        getViewModel().changeSessionDate(new SingleDialogEvent<>(getBaseActivity(), DialogUtils::showDateSelectDialog));
    }

    @Override
    protected void initBinding(LayoutInflater inflater, ViewGroup container) {
        tabBinding = FragmentDaysSessionsListBinding.inflate(inflater, container, false);
    }

    @Override
    protected View getRootView() {
        return tabBinding.getRoot();
    }

    protected RecyclerView getRecyclerView() {
        return tabBinding.listContainer.rvData;
    }

    protected FloatingActionMenu getFAB() {
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
        Intent intent = new Intent(getBaseActivity(), SelectionActivity.class);
        intent.putExtra(AppConsts.FRAGMENT_ID_EXTRA, AppConsts.FRAGMENT_DAYS_SESSIONS_ID);

        startActivityForResult(intent, REQUEST_SESSION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SESSION:
                if (resultCode == RESULT_OK) {
                    String sessionId = data.getStringExtra(AppConsts.SESSION_ID_EXTRA);
                    //getViewModel().addCoachToSession(sessionId);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCoachDaysSessionsListDataListenerEvent(CoachDaysSessionsListDataListenerEvent sessionListDataListenerEvent) {
        viewModel.getSessionsData(sessionListDataListenerEvent.getSessions());
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

    @Override
    public void onStart() {
        super.onStart();
        if (getViewModel() != null) {
            editorViewModel.personnelId.observe(this, email -> getViewModel().startDataListener(email));
        } else {
            closeProgress();
        }
    }

    protected void deleteItem(SessionView sessionView) {
        editorViewModel.personnelId.observe(this, id -> getViewModel().deleteItem(id, sessionView));
    }
}
