package com.example.fitnessfactory.ui.fragments.lists.helpers;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.managers.RxManager;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.ui.activities.BaseActivity;
import com.example.fitnessfactory.ui.adapters.ListAdapter;
import com.example.fitnessfactory.ui.fragments.lists.listListeners.ListListener;
import com.example.fitnessfactory.ui.viewholders.BaseRecyclerViewHolder;
import com.example.fitnessfactory.ui.viewmodels.lists.ListViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;
import com.github.clans.fab.FloatingActionMenu;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import java.util.List;

public class ListListenerFragmentHelper<ItemType,
        ViewHolderType extends BaseRecyclerViewHolder<ItemType>,
        AdapterType extends ListAdapter<ItemType, ViewHolderType>> {

    private BaseActivity context;
    private FloatingActionMenu fab;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvEmptyData;
    private RecyclerTouchListener touchListener;
    private ListListener<ItemType, ViewHolderType, AdapterType> fragment;
    private AdapterType adapter;
    private RxManager rxManager;
    private ListViewModel<ItemType> viewModel;

    public ListListenerFragmentHelper(BaseActivity context,
                                      FloatingActionMenu fab,
                                      RecyclerView recyclerView,
                                      ProgressBar progressBar,
                                      TextView tvEmptyData,
                                      ListListener<ItemType, ViewHolderType, AdapterType> fragment,
                                      ListViewModel<ItemType> viewModel) {
        this.context = context;
        this.fab = fab;
        this.recyclerView = recyclerView;
        this.progressBar = progressBar;
        this.tvEmptyData = tvEmptyData;
        this.fragment = fragment;
        this.viewModel = viewModel;
        rxManager = new RxManager();
    }

    public void initComponents() {
        fab.setOnMenuButtonClickListener(view -> fragment.openItemsScreen(fragment.getNewItem()));
        GuiUtils.initListView(context, recyclerView, false);
        GuiUtils.setListViewAnimation(recyclerView, fab);
        touchListener = new RecyclerTouchListener(context, recyclerView);
        recyclerView.addOnItemTouchListener(touchListener);
        touchListener.setSwipeOptionViews(R.id.btnEdit, R.id.btnDelete);
        touchListener.setSwipeable(R.id.rowFG, R.id.rowBG, (viewId, position) -> {
            switch (viewId) {
                case R.id.btnEdit:
                    fragment.openItemsScreen(adapter.getItem(position));
                    break;
                case R.id.btnDelete:
                    askForDelete(adapter.getItem(position));
                    break;
            }
        });
        touchListener.setClickable(new RecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
                fragment.onRowClicked(adapter.getItem(position));
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        });
    }

    public void askForDelete(ItemType item) {
        rxManager.subscribeInMainThread(
                DialogUtils.showAskDialog(
                        context,
                        fragment.getDeleteMessage(),
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

    private void deleteItem(ItemType item) {
        viewModel.deleteItem(item);
    }

    protected void setListData(List<ItemType> listData) {
        if (adapter == null) {
            adapter = fragment.createNewAdapter(listData);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setListData(listData);
        }
        closeProgress();
    }

    public void closeProgress() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
        if (adapter != null) {
            tvEmptyData.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);

        }
    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        fab.setVisibility(View.GONE);
    }
}
