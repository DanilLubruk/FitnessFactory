package com.example.fitnessfactory.ui.viewmodels.lists;

import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.models.SessionType;
import com.example.fitnessfactory.ui.viewmodels.lists.gym.GymAddressSearchField;
import com.example.fitnessfactory.ui.viewmodels.lists.gym.GymNameSearchField;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelList.searchFields.PersonnelEmailSearchField;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelList.searchFields.PersonnelNameSearchField;
import com.example.fitnessfactory.ui.viewmodels.lists.sessionTypes.SessionTypeNameSearchField;

import java.util.ArrayList;
import java.util.List;

public abstract class SearchFieldState<ItemType> {
    public abstract String getSearchField(ItemType item);
    public abstract int getIndex();

    public static SearchFieldState<AppUser> getPersonnelSearchField(int index) {
        return getPersonnelSearchFields().get(index);
    }

    public static List<SearchFieldState<AppUser>> getPersonnelSearchFields() {
        List<SearchFieldState<AppUser>> list = new ArrayList<>();
        list.add(new PersonnelNameSearchField());
        list.add(new PersonnelEmailSearchField());
        return list;
    }

    public static SearchFieldState<SessionType> getSessionTypeSearchField(int index) {
        return getSessionTypesSearchFields().get(index);
    }

    public static List<SearchFieldState<SessionType>> getSessionTypesSearchFields() {
        List<SearchFieldState<SessionType>> list = new ArrayList<>();
        list.add(new SessionTypeNameSearchField());
        return list;
    }

    public static SearchFieldState<Gym> getGymSearchField(int index) {
        return getGymsSearchFields().get(index);
    }

    public static List<SearchFieldState<Gym>> getGymsSearchFields() {
        List<SearchFieldState<Gym>> list = new ArrayList<>();
        list.add(new GymNameSearchField());
        list.add(new GymAddressSearchField());
        return list;
    }
}
