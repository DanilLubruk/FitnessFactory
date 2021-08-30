package com.example.fitnessfactory.ui.fragments.lists;

import static android.app.Activity.RESULT_OK;
import static com.example.fitnessfactory.data.ActivityRequestCodes.REQUEST_GYM;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.events.GymAdminsListListenerEvent;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.ui.activities.SelectionActivity;
import com.example.fitnessfactory.ui.adapters.AdminsListAdapter;
import com.example.fitnessfactory.ui.fragments.EditorTabFragment;
import com.example.fitnessfactory.ui.viewmodels.lists.AdminsListTabViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;
import com.github.clans.fab.FloatingActionButton;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

public class AdminsListTabFragment extends EditorTabFragment {

    @BindView(R.id.rvAdmins)
    RecyclerView rvAdmins;
    @BindView(R.id.fabAddAdmin)
    FloatingActionButton fabAddAdmin;

    private AdminsListAdapter adapter;
    private RecyclerTouchListener touchListener;
    private AdminsListTabViewModel viewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AdminsListTabViewModel.class);
        initComponents();
    }

    private void initComponents() {
        viewModel.setGymData(getBaseActivity().getIntent().getStringExtra(AppConsts.GYM_ID_EXTRA));
        fabAddAdmin.setOnClickListener(view -> tryToShowSelectionActivity());
        GuiUtils.initListView(getBaseActivity(), rvAdmins, true);
        touchListener = new RecyclerTouchListener(getBaseActivity(), rvAdmins);
        rvAdmins.addOnItemTouchListener(touchListener);
        touchListener.setSwipeOptionViews(R.id.btnRemove);
        touchListener.setSwipeable(R.id.rowFG, R.id.rowBG, (viewId, position) -> {
            switch (viewId) {
                case R.id.btnRemove:
                    AppUser admin = adapter.getAdmin(position);
                    askForDeleteAdmin(admin);
                    break;
            }
        });
        viewModel.getAdmins().observe(getViewLifecycleOwner(), this::setAdminsData);
    }

    private void askForDeleteAdmin(AppUser admin) {
        subscribeInMainThread(
                DialogUtils.showAskDialog(
                        getBaseActivity(),
                        getDeleteAdminMessage(),
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

    private String getDeleteAdminMessage() {
        return ResUtils.getString(R.string.message_ask_remove_admin_from_gym);
    }

    private void deleteAdmin(AppUser admin) {
        viewModel.removeAdminFromGym(admin.getEmail());
    }

    private void tryToShowSelectionActivity() {
        getBaseActivity().save(isSaved -> {
            if (isSaved) {
                showSelectionActivity();
            }
        });
    }

    private void showSelectionActivity() {
        Intent intent = new Intent(getBaseActivity(), SelectionActivity.class);
        intent.putExtra(AppConsts.FRAGMENT_ID_EXTRA, AppConsts.FRAGMENT_ADMINS_ID);
        viewModel.setGymData(getBaseActivity().getIntent().getStringExtra(AppConsts.GYM_ID_EXTRA));

        startActivityForResult(intent, REQUEST_GYM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_GYM:
                if (resultCode == RESULT_OK) {
                    String adminEmail = data.getStringExtra(AppConsts.ADMIN_EMAIL_EXTRA);
                    viewModel.addAdminToGym(adminEmail);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setAdminsData(List<AppUser> admins) {
        if (adapter == null) {
            adapter = new AdminsListAdapter(admins, R.layout.admins_list_tab_view_item);
            rvAdmins.setAdapter(adapter);
        } else {
            adapter.setAdmins(admins);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGymAdminsListListenerEvent(GymAdminsListListenerEvent gymAdminsListListenerEvent) {
        viewModel.getAdminsData();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.startDataListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.stopDataListener();
    }

    public void closeProgress() {

    }

    public void showProgress() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_admins_list;
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
