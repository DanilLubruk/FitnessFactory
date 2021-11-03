package com.example.fitnessfactory.ui.fragments.lists;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.events.AdminsListDataListenerEvent;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.activities.editors.AdminEditorActivity;
import com.example.fitnessfactory.ui.viewmodels.factories.AdminsListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.AdminListViewModel;
import com.example.fitnessfactory.utils.ResUtils;
import com.tiromansev.prefswrapper.typedprefs.BooleanPreference;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AdminsListFragment extends PersonnelListFragment {

    private AdminListViewModel viewModel;

    protected void defineViewModel() {
        viewModel = new ViewModelProvider(this, new AdminsListViewModelFactory()).get(AdminListViewModel.class);
    }

    @Override
    protected AdminListViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected String getSelectTitle() {
        return ResUtils.getString(R.string.title_select_admins);
    }

    @Override
    protected String getListTitle() {
        return ResUtils.getString(R.string.title_admins);
    }

    @Override
    protected BooleanPreference getAskToSendInvitationPrefs() {
        return AppPrefs.askToSendAdminEmailInvite();
    }

    @Override
    protected Intent getEditorActivityIntent(AppUser personnel) {
        Intent intent = new Intent(getBaseActivity(), AdminEditorActivity.class);

        intent.putExtra(AppConsts.ADMIN_ID_EXTRA, personnel.getId());
        intent.putExtra(AppConsts.ADMIN_NAME_EXTRA, personnel.getName());
        intent.putExtra(AppConsts.ADMIN_EMAIL_EXTRA, personnel.getEmail());

        return intent;
    }

    @Override
    protected Intent getResultIntent(AppUser personnel) {
        Intent result = new Intent();
        result.putExtra(AppConsts.ADMIN_EMAIL_EXTRA, personnel.getEmail());

        return result;
    }

    @Override
    protected String getSingularPersonnelCaption() {
        return ResUtils.getString(R.string.caption_admin);
    }

    @Override
    protected String getPluralPersonnelCaption() {
        return ResUtils.getString(R.string.caption_admins);
    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_admin);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAdminsListDataListenerEvent(AdminsListDataListenerEvent adminsListDataListenerEvent) {
        viewModel.getPersonnelListData();
    }
}
