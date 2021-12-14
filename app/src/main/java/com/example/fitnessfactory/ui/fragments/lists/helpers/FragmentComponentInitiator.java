package com.example.fitnessfactory.ui.fragments.lists.helpers;

import com.example.fitnessfactory.ui.adapters.ListAdapter;
import com.example.fitnessfactory.ui.viewholders.BaseRecyclerViewHolder;

public interface FragmentComponentInitiator {

    <ItemType,
    ViewHolderType extends BaseRecyclerViewHolder<ItemType>,
    AdapterType extends ListAdapter<ItemType, ViewHolderType>>
    void initComponents(ListListenerFragmentHelper<ItemType, ViewHolderType, AdapterType> fragmentHelper);
}
