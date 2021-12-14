package com.example.fitnessfactory.ui.fragments.lists.helpers;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.ui.adapters.ListAdapter;
import com.example.fitnessfactory.ui.viewholders.BaseRecyclerViewHolder;
import com.example.fitnessfactory.utils.GuiUtils;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

public class ListFragmentComponentInitiator implements FragmentComponentInitiator {

    @Override
    public <ItemType,
    ViewHolderType extends BaseRecyclerViewHolder<ItemType>,
    AdapterType extends ListAdapter<ItemType, ViewHolderType>>
    void initComponents(ListFragmentHelper<ItemType> fragmentHelper) {
        fragmentHelper.fab.setOnMenuButtonClickListener(view -> fragmentHelper.openItemsScreen(fragmentHelper.getNewItem()));
        GuiUtils.initListView(fragmentHelper.getContext(), fragmentHelper.getRecyclerView(), false);
        GuiUtils.setListViewAnimation(fragmentHelper.getRecyclerView(), fragmentHelper.getFab());
        fragmentHelper.setTouchListener(new RecyclerTouchListener(fragmentHelper.getContext(), fragmentHelper.getRecyclerView()));
        fragmentHelper.getRecyclerView().addOnItemTouchListener(fragmentHelper.getTouchListener());
        fragmentHelper.getTouchListener().setSwipeOptionViews(R.id.btnEdit, R.id.btnDelete);
        fragmentHelper.getTouchListener().setSwipeable(R.id.rowFG, R.id.rowBG, (viewId, position) -> {
            switch (viewId) {
                case R.id.btnEdit:
                    fragmentHelper.openItemsScreen(fragmentHelper.getItem(position));
                    break;
                case R.id.btnDelete:
                    fragmentHelper.askForDelete(fragmentHelper.getItem(position));
                    break;
            }
        });
        fragmentHelper.getTouchListener().setClickable(new RecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
                fragmentHelper.onRowClicked(fragmentHelper.getItem(position));
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        });
        fragmentHelper.viewModel.doInterruptProgress.observe(fragmentHelper.getContext(), doInterrupt -> {
            if (doInterrupt) {
                fragmentHelper.closeProgress();
            }
        });
    }
}
