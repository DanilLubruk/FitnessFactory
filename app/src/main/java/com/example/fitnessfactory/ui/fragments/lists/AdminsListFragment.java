package com.example.fitnessfactory.ui.fragments.lists;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.events.AdminsListDataListenerEvent;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.activities.editors.AdminEditorActivity;
import com.example.fitnessfactory.ui.adapters.AdminsListAdapter;
import com.example.fitnessfactory.ui.fragments.ListListenerFragment;
import com.example.fitnessfactory.ui.viewmodels.lists.AdminListViewModel;
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

public class AdminsListFragment extends ListListenerFragment<AppUser> {

    @BindView(R.id.rvData)
    RecyclerView rvAdmins;
    @BindView(R.id.fabAddItem)
    FloatingActionButton fabAddAdmin;

    private AdminListViewModel viewModel;
    private AdminsListAdapter adapter;
    private RecyclerTouchListener touchListener;
    private boolean selectMode = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBaseActivity().setTitle(selectMode ? R.string.title_select_admins : R.string.title_admins);
        viewModel = new ViewModelProvider(this).get(AdminListViewModel.class);
        initComponents();
    }

    @Override
    protected AdminListViewModel getViewModel() {
        return viewModel;
    }

    private void initComponents() {
        selectMode = getBaseActivity().getIntent().getBooleanExtra(AppConsts.IS_SELECT_MODE_EXTRA, false);
        fabAddAdmin.setOnClickListener(view -> showSendEmailInvitationDialog());
        GuiUtils.initListView(getBaseActivity(), rvAdmins, true);
        touchListener = new RecyclerTouchListener(getBaseActivity(), rvAdmins);
        rvAdmins.addOnItemTouchListener(touchListener);
        touchListener.setSwipeOptionViews(R.id.btnEdit, R.id.btnDelete);
        touchListener.setSwipeable(R.id.rowFG, R.id.rowBG, (viewId, position) -> {
            switch (viewId) {
                case R.id.btnEdit:
                    AppUser admin = adapter.getAdmin(position);
                    showEditorActivity(admin);
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
                AdminsListFragment.this.onRowClicked(admin);
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        });
        viewModel.getAdmins().observe(getViewLifecycleOwner(), this::setAdminsData);
    }

    private void onRowClicked(AppUser admin) {
        if (selectMode) {
            sendSelectResult(admin);
        } else {
            showEditorActivity(admin);
        }
    }

    private void sendSelectResult(AppUser admin) {
        Intent result = new Intent();
        result.putExtra(AppConsts.ADMIN_EMAIL_EXTRA, admin.getEmail());
        getBaseActivity().setResult(Activity.RESULT_OK, result);
        getBaseActivity().finish();
    }

    private void showEditorActivity(AppUser admin) {
        Intent intent = new Intent(getBaseActivity(), AdminEditorActivity.class);

        intent.putExtra(AppConsts.ADMIN_ID_EXTRA, admin.getId());
        intent.putExtra(AppConsts.ADMIN_NAME_EXTRA, admin.getName());
        intent.putExtra(AppConsts.ADMIN_EMAIL_EXTRA, admin.getEmail());

        startActivity(intent);
    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_admin);
    }

    private void setAdminsData(List<AppUser> admins) {
        if (adapter == null) {
            adapter = new AdminsListAdapter(admins, R.layout.admins_list_item_view);
            rvAdmins.setAdapter(adapter);
        } else {
            adapter.setAdmins(admins);
        }
    }

    private void showSendEmailInvitationDialog() {
        viewModel.registerAdmin(
                getAskEmailDialog(),
                getAskToSendInvitationDialog())
                .observe(this, email -> {
                    if (!TextUtils.isEmpty(email)) {
                        sendEmailInvitation(email);
                    }
                });
    }

    private Single<String> getAskEmailDialog() {
        return DialogUtils.showOneLineEditDialog(
                getBaseActivity(),
                ResUtils.getString(R.string.title_invite_admin),
                ResUtils.getString(R.string.caption_email),
                ResUtils.getString(R.string.caption_send),
                ResUtils.getString(R.string.caption_cancel));
    }

    private Single<Boolean> getAskToSendInvitationDialog() {
        return DialogUtils.showAskNoMoreDialog(
                getBaseActivity(),
                ResUtils.getString(R.string.message_send_invitation),
                AppPrefs.askForSendingAdminEmailInvite());
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

    public void closeProgress() {

    }

    public void showProgress() {

    }
}
