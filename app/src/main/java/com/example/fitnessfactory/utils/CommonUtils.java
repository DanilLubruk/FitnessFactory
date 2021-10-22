package com.example.fitnessfactory.utils;

import java.util.Calendar;
import java.util.Date;

public class CommonUtils {

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
}
