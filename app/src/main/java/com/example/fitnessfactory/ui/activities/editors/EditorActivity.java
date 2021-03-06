package com.example.fitnessfactory.ui.activities.editors;

import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.callbacks.EditorCallback;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.ui.activities.BaseActivity;
import com.example.fitnessfactory.ui.fragments.lists.ListListenerSelectFragment;
import com.example.fitnessfactory.ui.fragments.lists.personnelList.PersonnelListFragment;
import com.example.fitnessfactory.ui.viewmodels.editors.EditorViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;

import java.util.List;

public abstract class EditorActivity extends BaseActivity {

    protected final int MENU_SAVE = 21;
    protected final int MENU_DELETE = 22;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem;

        menuItem = menu.add(0, MENU_SAVE, 0, R.string.caption_save);
        menuItem.setIcon(ResUtils.getDrawable(getBaseContext(), R.drawable.ic_baseline_save_24));
        menuItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menuItem = menu.add(0, MENU_DELETE, 0, R.string.caption_delete);
        menuItem.setIcon(ResUtils.getDrawable(getBaseContext(), R.drawable.ic_baseline_delete_24));
        menuItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    protected abstract EditorViewModel getViewModel();

    protected abstract boolean isNewEntity();

    protected abstract void initEntityKey();

    @Override
    protected void initActivity() {
        super.initActivity();
        initEntityKey();
        setTitle(getTitleCaption());
    }

    protected abstract String getTitleCaption();

    @Override
    public void onBackPressed() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

        if (fragmentList.isEmpty()) {
            cancelAndClose();
            return;
        }

        Fragment topFragment = fragmentList.get(fragmentList.size() - 1);

        if (topFragment instanceof ListListenerSelectFragment) {
            ListListenerSelectFragment listFragment = (ListListenerSelectFragment) topFragment;
            listFragment.closeFragment();
        } else {
            cancelAndClose();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                cancelAndClose();
                return false;
            case MENU_SAVE:
                save();
                return false;
            case MENU_DELETE:
                askForDelete();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void askForDelete() {
        subscribeInMainThread(
                DialogUtils.showAskDialog(
                        this,
                        getDeleteMessage(),
                        ResUtils.getString(R.string.caption_ok),
                        ResUtils.getString(R.string.caption_cancel)),
                new SingleData<>(
                        doDelete -> {
                            if (doDelete) {
                                delete();
                            }
                        },
                        throwable -> {
                            throwable.printStackTrace();
                            GuiUtils.showMessage(throwable.getLocalizedMessage());
                        }
                ));
    }

    private void delete() {
        getViewModel().delete()
                .observe(this, isDeleted -> {
                    if (isDeleted) {
                        close();
                    }
                });
    }

    protected abstract String getDeleteMessage();

    protected void cancelAndClose() {
        isModified(isModified -> {
            if (isModified) {
                askForClose();
            } else {
                close();
            }
        });
    }

    private void isModified(EditorCallback callback) {
        getViewModel().isModified().observe(this, callback::callback);
    }

    private void askForClose() {
        subscribeInMainThread(
                DialogUtils.showAskDialog(
                        this,
                        ResUtils.getString(R.string.message_data_modified),
                        ResUtils.getString(R.string.caption_ok),
                        ResUtils.getString(R.string.caption_cancel)),
                new SingleData<>(
                        doClose -> {
                            if (doClose) {
                                close();
                            }
                        },
                        throwable -> {
                            throwable.printStackTrace();
                            GuiUtils.showMessage(throwable.getLocalizedMessage());
                        }));
    }

    private void save() {
        save(isSaved -> {
            if (isSaved) {
                close();
            }
        });
    }

    protected abstract boolean isDataValid();

    protected boolean checkScreenDataValidity() {
        if (isDataValid()) {
            return true;
        } else {
            GuiUtils.showMessage(getInvalidDataMessage());
            return false;
        }
    }

    protected String getInvalidDataMessage() {
        return ResUtils.getString(R.string.caption_blank_fields);
    }

    public void save(EditorCallback callback) {
        if (checkScreenDataValidity()) {
            getViewModel().save().observe(this, callback::callback);
        } else {
            callback.callback(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        getViewModel().saveState(savedState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        getViewModel().restoreState(savedState);
    }
}
