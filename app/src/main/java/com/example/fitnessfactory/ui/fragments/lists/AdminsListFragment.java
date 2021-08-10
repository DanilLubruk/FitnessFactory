package com.example.fitnessfactory.ui.fragments.lists;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.events.AdminsListDataListenerEvent;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.ui.adapters.AdminsListAdapter;
import com.example.fitnessfactory.ui.fragments.BaseFragment;
import com.example.fitnessfactory.ui.viewmodels.lists.AdminListViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.IntentUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.StringUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;
import com.github.clans.fab.FloatingActionButton;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

public class AdminsListFragment extends BaseFragment {

    @BindView(R.id.rvAdmins)
    RecyclerView rvAdmins;
    @BindView(R.id.fabAddAdmin)
    FloatingActionButton fabAddAdmin;

    private AdminListViewModel viewModel;
    private AdminsListAdapter adapter;
    private RecyclerTouchListener touchListener;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBaseActivity().setTitle(R.string.title_admins);
        viewModel = new ViewModelProvider(this).get(AdminListViewModel.class);
        initComponents();
    }

    private void initComponents() {
        fabAddAdmin.setOnClickListener(view -> showSendEmailInvitationDialog());
        GuiUtils.initListView(getBaseActivity(), rvAdmins, true);
        touchListener = new RecyclerTouchListener(getBaseActivity(), rvAdmins);
        rvAdmins.addOnItemTouchListener(touchListener);
        touchListener.setSwipeOptionViews(R.id.btnEdit, R.id.btnDelete);
        touchListener.setSwipeable(R.id.rowFG, R.id.rowBG, (viewId, position) -> {
            switch (viewId) {
                case R.id.btnEdit:
                    AppUser admin = adapter.getAdmin(position);
                    break;
                case R.id.btnDelete:
                    admin = adapter.getAdmin(position);
                    askForDelete(admin);
                    break;
            }
        });
        touchListener.setClickable(new RecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
                AppUser admin = adapter.getAdmin(position);
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        });
        viewModel.getAdmins().observe(getViewLifecycleOwner(), this::setAdminsData);
    }

    private void askForDelete(AppUser admin) {
        subscribeInMainThread(
                DialogUtils.showAskDialog(
                        getBaseActivity(),
                        getDeleteMessage(),
                        ResUtils.getString(R.string.caption_ok),
                        ResUtils.getString(R.string.caption_cancel)),
                new SingleData<>(
                        doDelete -> {
                            if (doDelete) {
                                deleteAdmin(admin);
                            }
                        },
                        throwable -> {
                            throwable.printStackTrace();
                            GuiUtils.showMessage(throwable.getLocalizedMessage());
                        }
                ));
    }

    private String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_admin);
    }

    private void deleteAdmin(AppUser admin) {
        viewModel.deleteAdmin(admin.getEmail());
    }

    private void setAdminsData(List<AppUser> admins) {
        if (adapter == null) {
            adapter = new AdminsListAdapter(admins);
            rvAdmins.setAdapter(adapter);
        } else {
            adapter.setAdmins(admins);
        }
    }

    private void showSendEmailInvitationDialog() {
        subscribeInMainThread(DialogUtils.showOneLineEditDialog(
                getBaseActivity(),
                ResUtils.getString(R.string.title_invite_admin),
                ResUtils.getString(R.string.caption_email),
                ResUtils.getString(R.string.caption_send),
                ResUtils.getString(R.string.caption_cancel)),
                new SingleData<>(
                        email -> {
                            if (!StringUtils.isEmpty(email)) {
                                viewModel.registerAccess(email);
                                sendEmailInvitation(email);
                            }
                        },
                        throwable -> {
                            throwable.printStackTrace();
                            GuiUtils.showMessage(throwable.getLocalizedMessage());
                        }));
    }

    private void sendEmailInvitation(String email) {
        Intent emailIntent = IntentUtils.getEmailIntent(
                email,
                ResUtils.getString(R.string.caption_job_offer),
                ResUtils.getString(R.string.text_invitation_to_admin));

        startActivity(Intent.createChooser(emailIntent, ResUtils.getString(R.string.title_invite_admin)));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAdminsListDataListenerEvent(AdminsListDataListenerEvent adminsListDataListenerEvent) {
        viewModel.getAdminsListData();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_admins_list;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.addAdminsListListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.removeAdminsListListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
