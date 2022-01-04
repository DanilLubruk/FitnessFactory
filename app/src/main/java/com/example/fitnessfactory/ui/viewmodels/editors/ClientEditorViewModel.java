package com.example.fitnessfactory.ui.viewmodels.editors;

import android.content.Intent;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.data.managers.access.ClientsAccessManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.utils.ResUtils;

import javax.inject.Inject;

public class ClientEditorViewModel extends PersonnelEditorViewModel {

    @Inject
    public ClientEditorViewModel(ClientsAccessManager accessManager) {
        super(accessManager);
    }

    @Override
    protected String getItemNullClause() {
        return getErrorMessageBreak()
                .concat(ResUtils.getString(R.string.message_error_client_null));
    }

    @Override
    protected AppUser getPersonnelFromData(Intent personnelData) {
        AppUser client = new AppUser();
        client.setId(personnelData.getStringExtra(AppConsts.CLIENT_ID_EXTRA));
        client.setName(personnelData.getStringExtra(AppConsts.CLIENT_NAME_EXTRA));
        client.setEmail(personnelData.getStringExtra(AppConsts.CLIENT_EMAIL_EXTRA));

        return client;
    }
}
