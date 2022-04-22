package com.example.fitnessfactory.ui.viewmodels.lists;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public abstract class SearchViewModel<ItemType, FieldStateType extends SearchFieldState<ItemType>>
        extends ListViewModel<ItemType> {
    protected final MutableLiveData<List<ItemType>> items = new MutableLiveData<>();

    public MutableLiveData<Boolean> showSearch = new MutableLiveData<>(false);
    public MutableLiveData<String> searchText = new MutableLiveData<>("");
    public ObservableField<FieldStateType> searchField = new ObservableField<>(getDefaultSearchField());
    private MutableLiveData<List<ItemType>> filteredItems = new MutableLiveData<>();

    protected abstract FieldStateType getDefaultSearchField();

    public void changeSearchField(FieldStateType selectedSearchField) {
        searchField.set(selectedSearchField);
        applySearch(searchText.getValue());
    }

    public void switchSearch() {
        showSearch.setValue(!showSearch.getValue());
        if (!showSearch.getValue()) {
            searchText.setValue("");
            filteredItems.setValue(items.getValue());
        }
    }

    public void applySearch(String text) {
        if (showSearch.getValue()) {
            List<ItemType> filteredList =
                    items.getValue().stream().filter(item ->
                            searchField.get().getSearchField(item).contains(
                                    text.toLowerCase(Locale.ROOT))
                    ).collect(Collectors.toList());

            filteredItems.setValue(filteredList);
        }
    }

    public void setItems(List<ItemType> itemsList) {
        items.setValue(itemsList);
        filteredItems.setValue(itemsList);
    }

    public MutableLiveData<List<ItemType>> getItems() {
        return filteredItems;
    }
}
