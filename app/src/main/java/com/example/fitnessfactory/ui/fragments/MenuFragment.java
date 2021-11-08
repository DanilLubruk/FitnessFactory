package com.example.fitnessfactory.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.DaysSessionListDataListenerEvent;
import com.example.fitnessfactory.data.events.SessionsCalendarDataListenerEvent;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.data.views.SessionView;
import com.example.fitnessfactory.databinding.FragmentMainMenuBinding;
import com.example.fitnessfactory.ui.activities.editors.SessionEditorActivity;
import com.example.fitnessfactory.ui.adapters.SessionsListAdapter;
import com.example.fitnessfactory.ui.viewmodels.factories.SessionsListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.SessionsListViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.TimeUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.marcohc.robotocalendar.RobotoCalendarView;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.List;

public class MenuFragment extends BaseFragment implements RobotoCalendarView.RobotoCalendarListener {

    private SessionsListViewModel viewModel;
    private FragmentMainMenuBinding binding;
    private SessionsListAdapter daysSessionsAdapter;
    private RecyclerTouchListener touchListener;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBaseActivity().setTitle(R.string.app_name);
        initComponents();
    }

    @Override
    protected void initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentMainMenuBinding.inflate(inflater, container, false);
    }

    @Override
    protected View getRootView() {
        return binding.getRoot();
    }

    private void initComponents() {
        viewModel = new ViewModelProvider(this, new SessionsListViewModelFactory()).get(SessionsListViewModel.class);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //binding.tvGreeting.setText(user.getDisplayName() + " " + user.getEmail());
        }
        binding.calendarView.setRobotoCalendarListener(this);
        binding.fabAddSession.setOnMenuButtonClickListener(view -> showSessionEditorActivity(new SessionView(new Session())));
        GuiUtils.initListView(getBaseActivity(), binding.rvSessions, false);
        GuiUtils.setListViewAnimation(binding.rvSessions, binding.fabAddSession);
        touchListener = new RecyclerTouchListener(getBaseActivity(), binding.rvSessions);
        binding.rvSessions.addOnItemTouchListener(touchListener);
        touchListener.setSwipeOptionViews(R.id.btnEdit, R.id.btnDelete);
        touchListener.setSwipeable(R.id.rowFG, R.id.rowBG, (viewId, position) -> {
            switch (viewId) {
                case R.id.btnEdit:
                    showSessionEditorActivity(daysSessionsAdapter.getItem(position));
                    break;
                case R.id.btnDelete:
                    askForDelete(daysSessionsAdapter.getItem(position));
                    break;
            }
        });
        touchListener.setClickable(new RecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
                showSessionEditorActivity(daysSessionsAdapter.getItem(position));
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        });
        viewModel.getSessions().observe(getBaseActivity(), this::updateDaysSessions);
        viewModel.getDate().observe(getBaseActivity(), date -> viewModel.startDataListener());
    }

    protected void askForDelete(SessionView item) {
        subscribeInMainThread(
                DialogUtils.showAskDialog(
                        getBaseActivity(),
                        ResUtils.getString(R.string.message_ask_delete_session),
                        ResUtils.getString(R.string.caption_ok),
                        ResUtils.getString(R.string.caption_cancel)),
                new SingleData<>(
                        doDelete -> {
                            if (doDelete) {
                                viewModel.deleteItem(item);
                            }
                        },
                        throwable -> {
                            throwable.printStackTrace();
                            GuiUtils.showMessage(throwable.getLocalizedMessage());
                        }
                ));
    }

    private void showSessionEditorActivity(SessionView sessionView) {
        if (sessionView == null) {
            GuiUtils.showMessage(ResUtils.getString(R.string.message_error_session_null));
            return;
        }
        Session session = sessionView.getSession();
        if (session == null) {
            GuiUtils.showMessage(ResUtils.getString(R.string.message_error_session_null));
            return;
        }

        Intent intent = new Intent(getBaseActivity(), SessionEditorActivity.class);
        intent.putExtra(AppConsts.SESSION_ID_EXTRA, session.getId());
        intent.putExtra(AppConsts.SESSION_DATE, getSelectedDate().getTime());

        startActivity(intent);
    }

    private Date getSelectedDate() {
        try {
            return binding.calendarView.getSelectedDay();
        } catch (NullPointerException exception) {
            return TimeUtils.getCurrentMoment();
        }
    }

    private void initCalendarData() {
        Date startDate = TimeUtils.getStartDate(binding.calendarView.getDate());
        Date endDate = TimeUtils.getEndDate(binding.calendarView.getDate());
        viewModel.startCalendarDataListener(startDate, endDate);
    }

    public void closeProgress() {

    }

    public void showProgress() {

    }

    @Override
    public void onDayClick(Date date) {
        viewModel.setDate(date);
    }

    @Override
    public void onDayLongClick(View view, Date date) {

    }

    @Override
    public void onRightButtonClick() {
        initCalendarData();
    }

    @Override
    public void onLeftButtonClick() {
        initCalendarData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSessionsCalendarListenerEvent(SessionsCalendarDataListenerEvent sessionsCalendarDataListenerEvent) {
        updateCalendar(sessionsCalendarDataListenerEvent.getSessions());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDaysSessionsListenerEvent(DaysSessionListDataListenerEvent sessionListDataListenerEvent) {
        viewModel.getSessionsData(sessionListDataListenerEvent.getSessions());
    }

    private void updateCalendar(List<Session> sessions) {
        for (Session session : sessions) {
            binding.calendarView.markCircleImage1(session.getDate());
        }
    }

    private void updateDaysSessions(List<SessionView> sessions) {
        binding.tvEmptyList.setVisibility(sessions.size() == 0 ? View.VISIBLE : View.GONE);
        if (daysSessionsAdapter == null) {
            daysSessionsAdapter = new SessionsListAdapter(sessions, R.layout.sessions_list_item_view);
            binding.rvSessions.setAdapter(daysSessionsAdapter);
        } else {
            daysSessionsAdapter.setListData(sessions);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        initCalendarData();
        viewModel.setDate(getSelectedDate());
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        viewModel.stopCalendarDataListener();
        viewModel.stopDataListener();
    }
}
