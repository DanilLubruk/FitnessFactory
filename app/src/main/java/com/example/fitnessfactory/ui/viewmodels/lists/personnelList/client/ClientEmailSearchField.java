package com.example.fitnessfactory.ui.viewmodels.lists.personnelList.client;

import androidx.annotation.NonNull;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.Locale;

public class ClientEmailSearchField extends ClientSearchFieldState {

    @Override
    public String getSearchField(AppUser appUser) {
        return appUser.getEmail().toLowerCase(Locale.ROOT);
    }

    @Override
    public int getTitle() {
        return R.string.caption_email;
    }

    @NonNull
    @Override
    public String toString() {
        return ResUtils.getString(R.string.caption_email);
    }
}