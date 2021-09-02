package com.example.fitnessfactory.ui.fragments.lists;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.adapters.PersonnelListAdapter;
import com.example.fitnessfactory.ui.fragments.ListListenerFragment;
import com.example.fitnessfactory.ui.viewmodels.lists.PersonnelListViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.IntentUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;
import com.github.clans.fab.FloatingActionButton;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;
import com.tiromansev.prefswrapper.typedprefs.BooleanPreference;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Single;

public abstract class PersonnelListFragment extends ListListenerFragment<AppUser> {

    @BindView(R.id.rvData)
    RecyclerView rvPersonnel;
    @BindView(R.id.fabAddItem)
    FloatingActionButton fabAddPersonnel;

    private PersonnelListAdapter adapter;
    private RecyclerTouchListener touchListener;
    private boolean selectMode = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBaseActivity().setTitle(selectMode ? getSelectTitle() : getTitle());
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

    private void initComponents() {
        selectMode = getBaseActivity().getIntent().getBooleanExtra(AppConsts.IS_SELECT_MODE_EXTRA, false);
        fabAddPersonnel.setOnClickListener(view -> showSendEmailInvitationDialog());
        GuiUtils.initListView(getBaseActivity(), rvPersonnel, true);
        touchListener = new RecyclerTouchListener(getBaseActivity(), rvPersonnel);
        rvPersonnel.addOnItemTouchListener(touchListener);
        touchListener.setSwipeOptionViews(R.id.btnEdit, R.id.btnDelete);
        touchListener.setSwipeable(R.id.rowFG, R.id.rowBG, (viewId, position) -> {
            switch (viewId) {
                case R.id.btnEdit:
                    AppUser personnel = adapter.getPersonnel(position);
                    showEditorActivity(personnel);
                    break;
                case R.id.btnDelete:
                    personnel = adapter.getPersonnel(position);
                    askForDelete(personnel);
                    break;
            }
        });
        touchListener.setClickable(new RecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
                AppUser personnel = adapter.getPersonnel(position);
                PersonnelListFragment.this.onRowClicked(personnel);
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        });
        getViewModel().getPersonnel().observe(getViewLifecycleOwner(), this::setPersonnelData);
    }

    private void onRowClicked(AppUser personnel) {
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

    private void showEditorActivity(AppUser personnel) {
        Intent intent = getEditorActivityIntent(personnel);

        startActivity(intent);
    }

    private void setPersonnelData(List<AppUser> personnel) {
        if (adapter == null) {
            adapter = new PersonnelListAdapter(personnel, R.layout.two_bg_buttons_list_item_view);
            rvPersonnel.setAdapter(adapter);
        } else {
            adapter.setPersonnel(personnel);
        }
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
}
