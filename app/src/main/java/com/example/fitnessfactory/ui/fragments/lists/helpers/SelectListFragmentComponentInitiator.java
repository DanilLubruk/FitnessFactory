package com.example.fitnessfactory.ui.fragments.lists.helpers;

import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.ui.adapters.ListAdapter;
import com.example.fitnessfactory.ui.viewholders.BaseRecyclerViewHolder;

public class SelectListFragmentComponentInitiator implements FragmentComponentInitiator {

    private ListFragmentComponentInitiator initiator;

    @Override
    public <ItemType, ViewHolderType extends BaseRecyclerViewHolder<ItemType>, AdapterType extends ListAdapter<ItemType, ViewHolderType>>
    void initComponents(SelectListListenerFragmentHelper<ItemType, ViewHolderType, AdapterType> fragmentHelper) {
        initiator.initComponents(fragmentHelper);
        if (fragmentHelper.context.getIntent().hasExtra(AppConsts.IS_SELECT_MODE_EXTRA)) {
            fragmentHelper.getBaseActivity().getIntent().getBooleanExtra(AppConsts.IS_SELECT_MODE_EXTRA, false);
        }
    }
}
