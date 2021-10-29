package com.example.fitnessfactory.ui.fragments.lists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.databinding.FragmentListBinding;
import com.example.fitnessfactory.ui.adapters.ListAdapter;
import com.example.fitnessfactory.ui.fragments.BaseFragment;
import com.example.fitnessfactory.ui.viewholders.BaseRecyclerViewHolder;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public abstract class ListListenerFragment<
        ItemType,
        ViewHolderType extends BaseRecyclerViewHolder<ItemType>,
        AdapterType extends ListAdapter<ItemType, ViewHolderType>> extends BaseFragment {

    protected AdapterType adapter;
    protected RecyclerTouchListener touchListener;

    protected FragmentListBinding binding;

    protected abstract DataListListener<ItemType> getViewModel();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getBaseActivity().setTitle(getTitle());
        defineViewModel();
        super.onActivityCreated(savedInstanceState);
        initComponents();
    }

    @Override
    protected void initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentListBinding.inflate(inflater, container, false);
    }

    @Override
    protected View getRootView() {
        return binding.getRoot();
    }

    protected void initComponents() {
        binding.fabAddItem.setOnClickListener(view -> showEditorActivity(getNewItem()));
        GuiUtils.initListView(getBaseActivity(), binding.rvData, true);
        touchListener = new RecyclerTouchListener(getBaseActivity(), binding.rvData);
        binding.rvData.addOnItemTouchListener(touchListener);
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
                onListRowClicked(adapter.getItem(position));
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        });
    }

    protected void setListData(List<ItemType> listData) {
        if (adapter == null) {
            adapter = createNewAdapter(listData);
            binding.rvData.setAdapter(adapter);
        } else {
            adapter.setListData(listData);
        }
    }

    protected abstract String getTitle();

    protected abstract void defineViewModel();

    protected abstract AdapterType createNewAdapter(List<ItemType> listData);

    protected abstract void onListRowClicked(ItemType itemType);

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
}
