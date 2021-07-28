package com.example.fitnessfactory.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessfactory.FFApp;
import com.example.fitnessfactory.R;

public class GuiUtils {

    public static void showMessage(String message) {
        Toast toast = Toast.makeText(FFApp.get(), message, Toast.LENGTH_LONG);
        setToastSettings(toast);
        toast.show();
    }

    private static void setToastSettings(Toast toast) {
        try {
            View view = toast.getView();
            if (view != null) {
                //doesn't work for now ??where is to be fixed
                int margin = ResUtils.getDimen(R.dimen.activity_horizontal_margin);
                TextView text = view.findViewById(android.R.id.message);
                view.setBackgroundColor(ResUtils.getColor(R.color.colorMessageBackground));
                text.setTextColor(ResUtils.getColor(R.color.black));
                text.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                text.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) text.getLayoutParams();
                lp.setMargins(margin, margin, margin, margin);
                text.setLayoutParams(lp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initListView(Context context, RecyclerView recyclerView, boolean useDivider) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        if (useDivider) {
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,
                    layoutManager.getOrientation());
            dividerItemDecoration.setDrawable(ResUtils.getDrawable(R.drawable.divider));
            recyclerView.addItemDecoration(dividerItemDecoration);
        }
    }
}
