package com.example.fitnessfactory.ui.fragments.lists;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.events.CoachesListDataListenerEvent;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.adapters.CoachesListAdapter;
import com.example.fitnessfactory.ui.fragments.ListListenerFragment;
import com.example.fitnessfactory.ui.viewmodels.lists.CoachesListViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.IntentUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;
import com.github.clans.fab.FloatingActionButton;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Single;

public class CoachesListFragment extends ListListenerFragment<AppUser> {

    @BindView(R.id.rvData)
    RecyclerView rvCoaches;
    @BindView(R.id.fabAddItem)
    FloatingActionButton fabAddCoach;

    private CoachesListAdapter adapter;
    private CoachesListViewModel viewModel;
    private RecyclerTouchListener touchListener;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CoachesListViewModel.class);
        initComponents();
    }

    private void initComponents() {
        fabAddCoach.setOnClickListener(view -> showSendEmailInvitationDialog());
        GuiUtils.initListView(getBaseActivity(), rvCoaches, true);
        touchListener = new RecyclerTouchListener(getBaseActivity(), rvCoaches);
        rvCoaches.addOnItemTouchListener(touchListener);
        touchListener.setSwipeOptionViews(R.id.btnEdit, R.id.btnDelete);
        touchListener.setSwipeable(R.id.rowFG, R.id.rowBG, (viewId, position) -> {
            switch (viewId) {
                case R.id.btnEdit:
                    AppUser coach = adapter.getCoach(position);
                    showEditorActivity(coach);
                    break;
                case R.id.btnDelete:
                    coach = adapter.getCoach(position);
                    askForDelete(coach);
                    break;
            }
        });
        touchListener.setClickable(new RecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
                AppUser coach = adapter.getCoach(position);
                CoachesListFragment.this.onRowClicked(coach);
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        });
        viewModel.getCoaches().observe(getViewLifecycleOwner(), this::setCoachesData);
    }

    private void onRowClicked(AppUser coach) {
        showEditorActivity(coach);
    }

    private void showEditorActivity(AppUser coach) {
       /* Intent intent = new Intent(getBaseActivity(), CoachEditorActivity.class);

        intent.putExtra(AppConsts.COACH_ID_EXTRA, coach.getId());
        intent.putExtra(AppConsts.COACH_NAME_EXTRA, coach.getName());
        intent.putExtra(AppConsts.COACH_EMAIL_EXTRA, coach.getEmail());

        startActivity(intent);*/
    }

    private void setCoachesData(List<AppUser> coaches) {
        if (adapter == null) {
            adapter = new CoachesListAdapter(coaches, R.layout.two_bg_buttons_list_item_view);
            rvCoaches.setAdapter(adapter);
        } else {
            adapter.setCoaches(coaches);
        }
    }

    private void showSendEmailInvitationDialog() {
        viewModel.registerCoach(
                DialogUtils.getAskEmailDialog(getBaseActivity()),
                getAskToSendInvitationDialog())
                .observe(this, this::sendEmailInvitation);
    }

    private Single<Boolean> getAskToSendInvitationDialog() {
        return DialogUtils.showAskNoMoreDialog(
                getBaseActivity(),
                String.format(ResUtils.getString(R.string.message_send_invitation), ResUtils.getString(R.string.caption_coach)),
                AppPrefs.askForSendingCoachEmailInvite());
    }

    private void sendEmailInvitation(String email) {
        Intent emailIntent = IntentUtils.getEmailIntent(
                email,
                ResUtils.getString(R.string.caption_job_offer),
                String.format(ResUtils.getString(R.string.text_invitation_to_personnel), ResUtils.getString(R.string.caption_coaches)));

        startActivity(Intent.createChooser(emailIntent, ResUtils.getString(R.string.title_invite_admin)));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCoachesListDataListenerEvent(CoachesListDataListenerEvent coachesListDataListenerEvent) {
        viewModel.getCoachesData();
    }

    @Override
    protected CoachesListViewModel getViewModel() {
        return viewModel;
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
