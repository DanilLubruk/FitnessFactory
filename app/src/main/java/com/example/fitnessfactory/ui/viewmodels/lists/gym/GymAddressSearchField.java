package com.example.fitnessfactory.ui.viewmodels.lists.gym;

import androidx.annotation.NonNull;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.ui.viewmodels.lists.SearchFieldState;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.Locale;

public class GymAddressSearchField extends SearchFieldState<Gym> {

    @Override
    public String getSearchField(Gym gym) {
        return gym.getAddress().toLowerCase(Locale.ROOT);
    }

    @NonNull
    @Override
    public String toString() {
        return ResUtils.getString(R.string.caption_address);
    }
}