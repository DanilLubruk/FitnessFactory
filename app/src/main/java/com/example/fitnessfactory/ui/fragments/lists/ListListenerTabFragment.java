package com.example.fitnessfactory.ui.fragments.lists;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.AppConsts;
import com.example.fitnessfactory.ui.activities.editors.EditorActivity;
import com.example.fitnessfactory.ui.adapters.ListAdapter;
import com.example.fitnessfactory.ui.viewholders.BaseRecyclerViewHolder;

public abstract class ListListenerTabFragment<
        ItemType,
        ViewHolderType extends BaseRecyclerViewHolder<ItemType>,
        AdapterType extends ListAdapter<ItemType, ViewHolderType>> extends ListListenerFragment<ItemType, ViewHolderType, AdapterType> {

    protected void initComponents() {
        super.initComponents();
        binding.fabAddItem.setOnClickListener(view -> tryToShowSelectionActivity());
        touchListener.setSwipeOptionViews(R.id.btnRemove);
        touchListener.setSwipeable(R.id.rowFG, R.id.rowBG, (viewId, position) -> {
            switch (viewId) {
                case R.id.btnRemove:
                    askForDelete(adapter.getItem(position));
                    break;
            }
        });
    }

    @Override
    protected String getTitle() {
        return getBaseActivity().getTitle().toString();
    }

    @Override
    public EditorActivity getBaseActivity() {
        return (EditorActivity) getActivity();
    }

    private void tryToShowSelectionActivity() {
        getBaseActivity().save(isSaved -> {
            if (isSaved) {
                openSelectionActivity();
            }
        });
    }

    protected abstract void openSelectionActivity();

    protected boolean doStartListenerInitially() {
        return false;
    }
}
