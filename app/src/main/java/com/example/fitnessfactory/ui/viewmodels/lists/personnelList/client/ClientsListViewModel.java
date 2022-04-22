package com.example.fitnessfactory.ui.viewmodels.lists.personnelList.client;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.dataListeners.ClientsListDataListener;
import com.example.fitnessfactory.data.managers.access.ClientsAccessManager;
import com.example.fitnessfactory.data.managers.data.ClientsDataManager;
import com.example.fitnessfactory.data.models.AppUser;
import com.example.fitnessfactory.ui.viewmodels.lists.personnelList.PersonnelListViewModel;
import com.example.fitnessfactory.utils.ResUtils;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class ClientsListViewModel extends PersonnelListViewModel {

    public MutableLiveData<Boolean> showSearch = new MutableLiveData<>(false);
    public MutableLiveData<String> searchText = new MutableLiveData<>("");
    public ObservableField<ClientSearchFieldState> searchField = new ObservableField<>(new ClientNameSearchField());
    private MutableLiveData<List<AppUser>> filteredPersonnel = new MutableLiveData<>();

    @Inject
    public ClientsListViewModel(ClientsAccessManager accessManager,
                                ClientsDataManager dataManager,
                                ClientsListDataListener dataListener) {
        super(accessManager, dataManager, dataListener);
    }

    public void changeSearchField(ClientSearchFieldState selectedSearchField) {
        searchField.set(selectedSearchField);
        applySearch(searchText.getValue());
    }

    public void switchSearch() {
        showSearch.setValue(!showSearch.getValue());
        if (!showSearch.getValue()) {
            searchText.setValue("");
            filteredPersonnel.setValue(personnel.getValue());
        }
    }

    public void applySearch(String text) {
        if (showSearch.getValue()) {
            List<AppUser> filteredList =
                    personnel.getValue().stream().filter(appUser ->
                    searchField.get().getSearchField(appUser).contains(
                            text.toLowerCase(Locale.ROOT))
            ).collect(Collectors.toList());

            filteredPersonnel.setValue(filteredList);
        }
    }

    @Override
    protected void setPersonnel(List<AppUser> appUsers) {
        super.setPersonnel(appUsers);
        filteredPersonnel.setValue(appUsers);
    }

    @Override
    public MutableLiveData<List<AppUser>> getPersonnel() {
        return filteredPersonnel;
    }

    @Override
    protected String getItemNullClause() {
        return ResUtils.getString(R.string.message_error_client_null);
    }
}
