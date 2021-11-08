package com.example.fitnessfactory.ui.fragments.lists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.databinding.FragmentListBinding;
import com.example.fitnessfactory.ui.adapters.ListAdapter;
import com.example.fitnessfactory.ui.fragments.BaseFragment;
import com.example.fitnessfactory.ui.fragments.lists.helpers.ListListenerFragmentHelper;
import com.example.fitnessfactory.ui.fragments.lists.listListeners.ListListener;
import com.example.fitnessfactory.ui.viewholders.BaseRecyclerViewHolder;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.ui.viewmodels.lists.ListViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public abstract class

ListListenerFragment<
        ItemType,
        ViewHolderType extends BaseRecyclerViewHolder<ItemType>,
        AdapterType extends ListAdapter<ItemType, ViewHolderType>> extends BaseFragment
        implements ListListener<ItemType, ViewHolderType, AdapterType> {

    protected ListListenerFragmentHelper<ItemType, ViewHolderType, AdapterType> helper;

    private FragmentListBinding binding;

    protected abstract ListViewModel<ItemType> getViewModel();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        helper = new ListListenerFragmentHelper<>(
                getBaseActivity(),
                binding.fabAddItem,
                binding.rvData,
                binding.pkProgress,
                binding.tvEmptyData,
                this);
        getBaseActivity().setTitle(getTitle());
        showProgress();
        defineViewModel();
        getViewModel().doInterruptProgress.observe(getBaseActivity(), doInterrupt -> {
            if (doInterrupt) {
                closeProgress();
            }
        });
        super.onActivityCreated(savedInstanceState);
        initComponents();
    }

    @Override
    protected void initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentListBinding.inflate(inflater, container, false);
    }

    protected void initComponents() {
        helper.initComponents();
    }

    protected abstract String getTitle();

    protected abstract void defineViewModel();

    @Override
    public void deleteItem(ItemType item) {
        getViewModel().deleteItem(item);
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
}
