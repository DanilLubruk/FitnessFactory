package com.example.fitnessfactory.ui.viewmodels.lists.sessionTypes;

import androidx.annotation.NonNull;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.models.SessionType;
import com.example.fitnessfactory.ui.viewmodels.lists.SearchFieldState;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.Locale;

public class SessionTypeNameSearchField extends SearchFieldState<SessionType> {

    @Override
    public String getSearchField(SessionType sessionType) {
        return sessionType.getName().toLowerCase(Locale.ROOT);
    }

    @Override
    public int getIndex() {
        return 0;
    }

    @NonNull
    @Override
    public String toString() {
        return ResUtils.getString(R.string.caption_name);
    }
}