package com.example.fitnessfactory.ui.fragments.lists;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.adapters.ListAdapter;
import com.example.fitnessfactory.ui.adapters.PersonnelListAdapter;
import com.example.fitnessfactory.ui.viewholders.BaseRecyclerViewHolder;
import com.example.fitnessfactory.ui.viewholders.lists.PersonnelListViewHolder;
import com.example.fitnessfactory.ui.viewmodels.lists.PersonnelListViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.IntentUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;
import com.tiromansev.prefswrapper.typedprefs.BooleanPreference;

import java.util.List;
import io.reactivex.Single;

public abstract class PersonnelListFragment
        extends ListListenerFragment<AppUser, PersonnelListViewHolder, PersonnelListAdapter> {

    private boolean selectMode = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBaseActivity().setTitle(selectMode ? getSelectTitle() : getTitle());
        defineViewModel();
        initComponents();
    }

    @Override
    protected abstract PersonnelListViewModel getViewModel();

    protected abstract String getSelectTitle();

    protected abstract String getTitle();

    protected abstract BooleanPreference getAskToSendInvitationPrefs();

    protected abstract Intent getEditorActivityIntent(AppUser personnel);

    protected abstract Intent getSendResultIntent(AppUser personnel);

    protected abstract String getSingularPersonnelCaption();

    protected abstract String getPluralPersonnelCaption();

    protected abstract void defineViewModel();

    @Override
    protected void initComponents() {
        super.initComponents();
        selectMode = getBaseActivity().getIntent().getBooleanExtra(AppConsts.IS_SELECT_MODE_EXTRA, false);
        fabAddItem.setOnClickListener(view -> showSendEmailInvitationDialog());
        getViewModel().getPersonnel().observe(getViewLifecycleOwner(), this::setListData);
    }

    protected void onRowClicked(AppUser personnel) {
        if (selectMode) {
            sendSelectResult(personnel);
        } else {
            showEditorActivity(personnel);
        }
    }

    private void sendSelectResult(AppUser personnel) {
        Intent result = getSendResultIntent(personnel);
        getBaseActivity().setResult(Activity.RESULT_OK, result);
        getBaseActivity().finish();
    }

    @Override
    protected void showEditorActivity(AppUser personnel) {
        Intent intent = getEditorActivityIntent(personnel);

        startActivity(intent);
    }

    private void showSendEmailInvitationDialog() {
        getViewModel().registerPersonnel(
                DialogUtils.getAskEmailDialog(getBaseActivity()),
                getAskToSendInvitationDialog(),
                getAskToSendInvitationPrefs().getValue())
                .observe(this, this::sendEmailInvitation);
    }

    private Single<Boolean> getAskToSendInvitationDialog() {
        return DialogUtils.showAskNoMoreDialog(
                getBaseActivity(),
                String.format(ResUtils.getString(R.string.message_send_invitation), getSingularPersonnelCaption()),
                getAskToSendInvitationPrefs());
    }

    private void sendEmailInvitation(String email) {
        Intent emailIntent = IntentUtils.getEmailIntent(
                email,
                ResUtils.getString(R.string.caption_job_offer),
                String.format(ResUtils.getString(R.string.text_invitation_to_personnel), getPluralPersonnelCaption()));

        startActivity(Intent.createChooser(emailIntent, ResUtils.getString(R.string.title_invite_personnel)));
    }

    @Override
    protected PersonnelListAdapter createNewAdapter(List<AppUser> listData) {
        return new PersonnelListAdapter(listData, R.layout.two_bg_buttons_list_item_view);
    }

    @Override
    protected AppUser getNewItem() {
        return new AppUser();
    }

    @Override
    protected void bindView(View itemView) {
        super.bindView(itemView);
    }
}
