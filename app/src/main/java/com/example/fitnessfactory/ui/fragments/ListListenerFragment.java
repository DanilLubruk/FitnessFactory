package com.example.fitnessfactory.ui.fragments;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.ui.viewmodels.DataListListener;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;

import org.greenrobot.eventbus.EventBus;

public abstract class ListListenerFragment<ItemType> extends BaseFragment {

    protected abstract DataListListener<ItemType> getViewModel();

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
}
