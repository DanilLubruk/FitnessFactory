package com.example.fitnessfactory.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.events.SessionsCalendarDataListenerEvent;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.ui.viewmodels.factories.SessionsListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.SessionsListViewModel;
import com.example.fitnessfactory.utils.CommonUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.marcohc.robotocalendar.RobotoCalendarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.List;

public class MenuFragment extends BaseFragment implements RobotoCalendarView.RobotoCalendarListener {

    private RobotoCalendarView calendarView;
    private TextView tvGreeting;
    private SessionsListViewModel viewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBaseActivity().setTitle(R.string.app_name);
        initComponents();
    }

    private void initComponents() {
        viewModel = new ViewModelProvider(this, new SessionsListViewModelFactory()).get(SessionsListViewModel.class);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            tvGreeting.setText(user.getDisplayName() + " " + user.getEmail());
        }
        calendarView.setRobotoCalendarListener(this);
    }

    private void initCalendarData() {
        Date startDate = CommonUtils.getStartDate(calendarView.getDate());
        Date endDate = CommonUtils.getEndDate(calendarView.getDate());
        viewModel.startDataListener(startDate, endDate);
    }

    public void closeProgress() {

    }

    public void showProgress() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_main_menu;
    }

    @Override
    protected void bindView(View itemView) {
        tvGreeting = itemView.findViewById(R.id.tvGreeting);
        calendarView = itemView.findViewById(R.id.calendarView);
    }

    @Override
    public void onDayClick(Date date) {

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

    private void updateCalendar(List<Session> sessions) {
        for (Session session : sessions) {
            calendarView.markCircleImage1(session.getDate());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        initCalendarData();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        viewModel.stopDataListener();
    }
}
