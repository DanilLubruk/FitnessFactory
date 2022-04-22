package com.example.fitnessfactory.ui.viewmodels.lists.personnelList.client;

import androidx.annotation.NonNull;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.Locale;

public class ClientNameSearchField extends ClientSearchFieldState {

    @Override
    public String getSearchField(AppUser appUser) {
        return appUser.getName().toLowerCase(Locale.ROOT);
    }

    @Override
    public int getTitle() {
        return R.string.caption_name;
    }

    @NonNull
    @Override
    public String toString() {
        return ResUtils.getString(R.string.caption_name);
    }
}