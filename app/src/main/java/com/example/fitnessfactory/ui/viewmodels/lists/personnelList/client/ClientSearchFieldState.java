package com.example.fitnessfactory.ui.viewmodels.lists.personnelList.client;

import androidx.annotation.NonNull;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class ClientSearchFieldState {
    public abstract String getSearchField(AppUser appUser);
    public abstract int getTitle();

    public static List<ClientSearchFieldState> getSearchFields() {
        List<ClientSearchFieldState> list = new ArrayList<>();
        list.add(new ClientNameSearchField());
        list.add(new ClientEmailSearchField());
        return list;
    }
}
