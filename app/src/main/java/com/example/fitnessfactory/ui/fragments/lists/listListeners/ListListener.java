package com.example.fitnessfactory.ui.fragments.lists.listListeners;

import com.example.fitnessfactory.ui.adapters.ListAdapter;
import com.example.fitnessfactory.ui.viewholders.BaseRecyclerViewHolder;

import java.util.List;

public interface ListListener<ItemType,
        ViewHolderType extends BaseRecyclerViewHolder<ItemType>,
        AdapterType extends ListAdapter<ItemType, ViewHolderType>> {

    void openItemsScreen(ItemType item);

    void onRowClicked(ItemType item);

    ItemType getNewItem();

    AdapterType createNewAdapter(List<ItemType> listData);

    String getDeleteMessage();
}
