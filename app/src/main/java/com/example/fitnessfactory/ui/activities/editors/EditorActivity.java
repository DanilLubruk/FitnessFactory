package com.example.fitnessfactory.ui.activities.editors;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.callbacks.EditorCallback;
import com.example.fitnessfactory.data.observers.SingleData;
import com.example.fitnessfactory.ui.activities.BaseActivity;
import com.example.fitnessfactory.ui.viewmodels.editors.EditorViewModel;
import com.example.fitnessfactory.utils.GuiUtils;
import com.example.fitnessfactory.utils.ResUtils;
import com.example.fitnessfactory.utils.dialogs.DialogUtils;

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
        menuItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menuItem = menu.add(0, MENU_DELETE, 0, R.string.caption_delete);
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
        setTitle(isNewEntity() ? R.string.title_add_item : R.string.title_edit_item);
    }

    @Override
    public void onBackPressed() {
        cancelAndClose();
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

    private void cancelAndClose() {
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
                        ResUtils.getString(R.string.caption_save),
                        ResUtils.getString(R.string.caption_close)),
                new SingleData<>(
                        doSave -> {
                            if (doSave) {
                                save();
                            } else {
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
            GuiUtils.showMessage(ResUtils.getString(R.string.caption_blank_fields));
            return false;
        }
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
