package com.example.fitnessfactory.ui.fragments.lists.helpers;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.ui.activities.BaseActivity;
import com.example.fitnessfactory.ui.adapters.ListAdapter;
import com.example.fitnessfactory.ui.fragments.lists.ListListenerFragment;
import com.example.fitnessfactory.ui.viewholders.BaseRecyclerViewHolder;
import com.github.clans.fab.FloatingActionMenu;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

public interface ListFragmentHelper<ItemType>  {

    ItemType getItem(int position);

    void setTouchListener(RecyclerTouchListener touchListener);

    RecyclerTouchListener getTouchListener();

    RecyclerView getRecyclerView();

    FloatingActionMenu getFab();

    BaseActivity getContext();

    void openItemsScreen(ItemType item);

    ItemType getNewItem();
}
