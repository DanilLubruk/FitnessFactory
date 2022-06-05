package com.example.fitnessfactory.ui.viewmodels.lists;

import android.os.Bundle;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.data.models.Session;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public abstract class SearchViewModel<ItemType, FieldStateType extends SearchFieldState<ItemType>>
        extends ListViewModel<ItemType> {
    private static final String SHOW_SEARCH_KEY = "SHOW_SEARCH_KEY";
    private static final String SEARCH_TEXT_KEY = "SEARCH_TEXT_KEY";
    private static final String SEARCH_FIELD_KEY = "SEARCH_FIELD_KEY";

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
        if (hasHandle() && showSearch.getValue()) {
            applySearch(searchText.getValue());
        } else {
            filteredItems.setValue(itemsList);
        }
    }

    public MutableLiveData<List<ItemType>> getItems() {
        return filteredItems;
    }

    @Override
    public void saveState(Bundle savedState) {
        super.saveState(savedState);
        getHandle().put(SEARCH_FIELD_KEY, searchField.get().getIndex());
        getHandle().put(SHOW_SEARCH_KEY, showSearch.getValue());
        getHandle().put(SEARCH_TEXT_KEY, searchText.getValue());
    }

    @Override
    public void restoreState(Bundle savedState) {
        super.restoreState(savedState);
        searchField.set(getRestoredSearchField((int) getHandle().get(SEARCH_FIELD_KEY)));
        showSearch.setValue((boolean) getHandle().get(SHOW_SEARCH_KEY));
        searchText.setValue((String) getHandle().get(SEARCH_TEXT_KEY));
    }

    protected abstract FieldStateType getRestoredSearchField(int index);
}
