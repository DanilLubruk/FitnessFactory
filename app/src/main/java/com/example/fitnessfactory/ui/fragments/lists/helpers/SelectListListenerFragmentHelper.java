package com.example.fitnessfactory.ui.fragments.lists.helpers;

import android.app.Activity;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.ui.activities.BaseActivity;
import com.example.fitnessfactory.ui.adapters.ListAdapter;
import com.example.fitnessfactory.ui.fragments.lists.listListeners.ListListener;
import com.example.fitnessfactory.ui.viewholders.BaseRecyclerViewHolder;
import com.example.fitnessfactory.ui.viewmodels.lists.ListViewModel;
import com.github.clans.fab.FloatingActionMenu;

public class SelectListListenerFragmentHelper<ItemType,
        ViewHolderType extends BaseRecyclerViewHolder<ItemType>,
        AdapterType extends ListAdapter<ItemType, ViewHolderType>> extends ListListenerFragmentHelper<ItemType, ViewHolderType, AdapterType> {

    private boolean selectMode = false;

    public SelectListListenerFragmentHelper(BaseActivity context,
                                            FloatingActionMenu fab,
                                            RecyclerView recyclerView,
                                            ProgressBar progressBar,
                                            TextView tvEmptyData,
                                            ListListener<ItemType, ViewHolderType, AdapterType> fragment,
                                            ListViewModel<ItemType> viewModel,
                                            FragmentComponentInitiator initiator) {
        super(context, fab, recyclerView, progressBar, tvEmptyData, fragment, viewModel, initiator);
    }


    public void setSelectMode(boolean selectMode) {
        this.selectMode = selectMode;
    }

    protected void onRowClicked(ItemType item) {
        if (selectMode) {
            sendSelectResult(item);
        } else {
            fragment.openItemsScreen(item);
        }
    }

    private void sendSelectResult(ItemType item) {
        context.setResult(Activity.RESULT_OK, fragment.getResultIntent(item));
        context.finish();
    }
}
