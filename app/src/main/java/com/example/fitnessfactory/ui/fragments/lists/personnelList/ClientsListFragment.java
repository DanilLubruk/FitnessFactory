package com.example.fitnessfactory.ui.fragments.lists.personnelList;

import android.content.Intent;

import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.AppPrefs;
import com.example.fitnessfactory.data.events.AdminsListDataListenerEvent;
import com.example.fitnessfactory.data.events.ClientsListDataListenerEvent;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.activities.editors.ClientEditorActivity;
import com.example.fitnessfactory.ui.viewmodels.factories.ClientsListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelList.ClientsListViewModel;
import com.example.fitnessfactory.utils.ResUtils;
import com.tiromansev.prefswrapper.typedprefs.BooleanPreference;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ClientsListFragment extends PersonnelListFragment {

    private ClientsListViewModel viewModel;

    @Override
    protected void defineViewModel() {
        viewModel = new ViewModelProvider(this, new ClientsListViewModelFactory()).get(ClientsListViewModel.class);
    }

    @Override
    protected String getDeleteMessage() {
        return ResUtils.getString(R.string.message_ask_delete_client);
    }

    @Override
    protected String getSelectTitle() {
        return ResUtils.getString(R.string.title_select_client);
    }

    @Override
    protected String getListTitle() {
        return ResUtils.getString(R.string.title_clients);
    }

    @Override
    protected ClientsListViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected BooleanPreference getAskToSendInvitationPrefs() {
        return AppPrefs.askToSendClientEmailInvite();
    }

    @Override
    protected Intent getEditorActivityIntent(AppUser personnel) {
        Intent intent = new Intent(getBaseActivity(), ClientEditorActivity.class);

        intent.putExtra(AppConsts.CLIENT_ID_EXTRA, personnel.getId());
        intent.putExtra(AppConsts.CLIENT_NAME_EXTRA, personnel.getName());
        intent.putExtra(AppConsts.CLIENT_EMAIL_EXTRA, personnel.getEmail());

        return intent;
    }

    @Override
    protected Intent getResultIntent(AppUser personnel) {
        Intent intent = new Intent();
        intent.putExtra(AppConsts.CLIENT_EMAIL_EXTRA, personnel.getEmail());

        return intent;
    }

    @Override
    protected String getSingularPersonnelCaption() {
        return ResUtils.getString(R.string.caption_client);
    }

    @Override
    protected String getPluralPersonnelCaption() {
        return ResUtils.getString(R.string.caption_clients);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClientsListDataListenerEvent(ClientsListDataListenerEvent clientsListDataListenerEvent) {
        viewModel.getPersonnelListData();
    }
}