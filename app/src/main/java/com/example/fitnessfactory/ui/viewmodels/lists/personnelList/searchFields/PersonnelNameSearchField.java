package com.example.fitnessfactory.ui.viewmodels.lists.personnelList.searchFields;

import androidx.annotation.NonNull;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.viewmodels.lists.SearchFieldState;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.Locale;

public class PersonnelNameSearchField extends SearchFieldState<AppUser> {

    @Override
    public String getSearchField(AppUser appUser) {
        return appUser.getName().toLowerCase(Locale.ROOT);
    }

    @NonNull
    @Override
    public String toString() {
        return ResUtils.getString(R.string.caption_name);
    }
}