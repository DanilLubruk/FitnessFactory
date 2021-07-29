package com.example.fitnessfactory.ui.activities.editors;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.fitnessfactory.R;
import com.example.fitnessfactory.ui.activities.BaseActivity;

public class EditorActivity extends BaseActivity {

    private final int MENU_SAVE = 21;
    private final int MENU_DELETE = 22;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:

                return false;
            case MENU_SAVE:

                return false;
            case MENU_DELETE:

                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
