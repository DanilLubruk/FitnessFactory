package com.example.fitnessfactory.utils;

import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
}
