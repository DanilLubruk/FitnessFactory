package com.example.fitnessfactory.ui.fragments.lists;

import android.app.Activity;
import android.content.Intent;

import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.ui.adapters.ListAdapter;
import com.example.fitnessfactory.ui.viewholders.BaseRecyclerViewHolder;

public abstract class ListListenerSelectFragment<ItemType,
        ViewHolderType extends BaseRecyclerViewHolder<ItemType>,
        AdapterType extends ListAdapter<ItemType, ViewHolderType>> extends ListListenerFragment<ItemType, ViewHolderType, AdapterType> {

    protected boolean selectMode = false;

    @Override
    protected void initComponents() {
        super.initComponents();
        if (getBaseActivity().getIntent().hasExtra(AppConsts.IS_SELECT_MODE_EXTRA)) {
            selectMode = getBaseActivity().getIntent().getBooleanExtra(AppConsts.IS_SELECT_MODE_EXTRA, false);
        }
    }

    @Override
    protected void onListRowClicked(ItemType item) {
        if (selectMode) {
            sendSelectResult(item);
        } else {
            showEditorActivity(item);
        }
    }

    protected void sendSelectResult(ItemType item) {
        getBaseActivity().setResult(Activity.RESULT_OK, getResultIntent(item));
        getBaseActivity().finish();
    }

    protected abstract Intent getResultIntent(ItemType item);

    @Override
    protected String getTitle() {
        return selectMode ?
                getSelectTitle()
                : getListTitle();
    }

    protected abstract String getSelectTitle();

    protected abstract String getListTitle();
}
