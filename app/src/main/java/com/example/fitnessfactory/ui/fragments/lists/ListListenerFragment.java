package com.example.fitnessfactory.ui.fragments.lists;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.Gym;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.ui.adapters.GymsListAdapter;
import com.example.fitnessfactory.ui.adapters.ListAdapter;
import com.example.fitnessfactory.ui.fragments.BaseFragment;
import com.example.fitnessfactory.ui.fragments.lists.GymsListFragment;
import com.example.fitnessfactory.ui.viewholders.BaseRecyclerViewHolder;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.ui.viewmodels.factories.ClientsListViewModelFactory;
import com.example.fitnessfactory.ui.viewmodels.lists.ClientsListViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;
import com.github.clans.fab.FloatingActionButton;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public abstract class ListListenerFragment<
        ItemType,
        ViewHolderType extends BaseRecyclerViewHolder<ItemType>,
        AdapterType extends ListAdapter<ItemType, ViewHolderType>> extends BaseFragment {

    protected RecyclerView recyclerView;
    protected FloatingActionButton fabAddItem;
    protected AdapterType adapter;
    protected RecyclerTouchListener touchListener;

    protected abstract DataListListener<ItemType> getViewModel();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initComponents();
    }

    protected void initComponents() {
        fabAddItem.setOnClickListener(view -> showEditorActivity(getNewItem()));
        GuiUtils.initListView(getBaseActivity(), recyclerView, true);
        touchListener = new RecyclerTouchListener(getBaseActivity(), recyclerView);
        recyclerView.addOnItemTouchListener(touchListener);
        touchListener.setSwipeOptionViews(R.id.btnEdit, R.id.btnDelete);
        touchListener.setSwipeable(R.id.rowFG, R.id.rowBG, (viewId, position) -> {
            switch (viewId) {
                case R.id.btnEdit:
                    showEditorActivity(adapter.getItem(position));
                    break;
                case R.id.btnDelete:
                    askForDelete(adapter.getItem(position));
                    break;
            }
        });
        touchListener.setClickable(new RecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
                ListListenerFragment.this.onRowClicked(adapter.getItem(position));
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        });
    }

    protected void setListData(List<ItemType> listData) {
        if (adapter == null) {
            adapter = createNewAdapter(listData);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setListData(listData);
        }
    }

    protected abstract AdapterType createNewAdapter(List<ItemType> listData);

    protected abstract void onRowClicked(ItemType itemType);

    protected abstract void showEditorActivity(ItemType item);

    protected abstract ItemType getNewItem();

    protected void askForDelete(ItemType item) {
        subscribeInMainThread(
                DialogUtils.showAskDialog(
                        getBaseActivity(),
                        getDeleteMessage(),
                        ResUtils.getString(R.string.caption_ok),
                        ResUtils.getString(R.string.caption_cancel)),
                new SingleData<>(
                        doDelete -> {
                            if (doDelete) {
                                deleteItem(item);
                            }
                        },
                        throwable -> {
                            throwable.printStackTrace();
                            GuiUtils.showMessage(throwable.getLocalizedMessage());
                        }
                ));
    }

    protected void deleteItem(ItemType item) {
        getViewModel().deleteItem(item);
    }

    protected abstract String getDeleteMessage();

    @Override
    public int getContentViewId() {
        return R.layout.fragment_list;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        if (getViewModel() != null) {
            getViewModel().startDataListener();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        if (getViewModel() != null) {
            getViewModel().stopDataListener();
        }
    }

    @Override
    protected void bindView(View itemView) {
        recyclerView = itemView.findViewById(R.id.rvData);
        fabAddItem = itemView.findViewById(R.id.fabAddItem);
    }
}
