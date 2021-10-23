package com.example.fitnessfactory.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.SessionsCalendarDataListenerEvent;
import com.example.fitnessfactory.data.models.Session;
import com.example.fitnessfactory.databinding.FragmentMainMenuBinding;
import com.example.fitnessfactory.ui.activities.editors.SessionEditorActivity;
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

    private SessionsListViewModel viewModel;
    private FragmentMainMenuBinding binding;

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
        binding.fabAddSession.setOnClickListener(view -> showSessionEditorActivity(new Session()));
    }

    private void showSessionEditorActivity(Session session) {
        Intent intent = new Intent(getBaseActivity(), SessionEditorActivity.class);
        intent.putExtra(AppConsts.SESSION_ID_EXTRA, session.getId());
        startActivity(intent);
    }

    private void initCalendarData() {
        Date startDate = CommonUtils.getStartDate(binding.calendarView.getDate());
        Date endDate = CommonUtils.getEndDate(binding.calendarView.getDate());
        viewModel.startDataListener(startDate, endDate);
    }

    public void closeProgress() {

    }

    public void showProgress() {

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
        binding.tvEmptyList.setVisibility(sessions.size() == 0 ? View.VISIBLE : View.GONE);
        for (Session session : sessions) {
            binding.calendarView.markCircleImage1(session.getDate());
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
