package com.example.fitnessfactory.utils;

import androidx.core.os.LocaleListCompat;

import com.example.fitnessfactory.FFApp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    public static Date getStartDate(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime());
            int startDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, startDay);
            return calendar.getTime();
        }

        return null;
    }

    public static Date getEndDate(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime());
            int endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, endDay);
            return calendar.getTime();
        }

        return null;
    }

    public static Date getStartOfDayDate(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int firstHourOfDay = calendar.getActualMinimum(Calendar.HOUR_OF_DAY);
            calendar.set(Calendar.HOUR_OF_DAY, firstHourOfDay);
            return calendar.getTime();
        }

        return null;
    }

    public static Date getCurrentMoment() {
        return new Date();
    }

    public static String dateToLocaleStr(Date date) {
        if (date == null) {
            return "";
        }

        try {
            LocaleListCompat list = LocaleListCompat.getDefault();
            Locale current = list.get(0);
            return DateFormat.getDateInstance(DateFormat.MEDIUM, current).format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(FFApp.get()
                .getApplicationContext());

        return dateFormat.format(date);
    }

    public static String dateTo24HoursTime(Date date) {
        if (date == null) {
            return "";
        }

        return new SimpleDateFormat("HH:mm", Locale.UK).format(date);
    }
}
