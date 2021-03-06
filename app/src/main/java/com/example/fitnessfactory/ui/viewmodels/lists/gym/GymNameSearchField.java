package com.example.fitnessfactory.ui.viewmodels.lists.gym;

import androidx.annotation.NonNull;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.ui.viewmodels.lists.SearchFieldState;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.Locale;

public class GymNameSearchField extends SearchFieldState<Gym> {

    @Override
    public String getSearchField(Gym gym) {
        return gym.getName().toLowerCase(Locale.ROOT);
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
