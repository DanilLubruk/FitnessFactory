package com.example.fitnessfactory.utils;

import androidx.core.os.LocaleListCompat;

import com.example.fitnessfactory.FFApp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Single;

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
            int firstMinute = calendar.getActualMinimum(Calendar.MINUTE);
            int firstSecond = calendar.getActualMinimum(Calendar.SECOND);
            int firstMillisecond = calendar.getActualMinimum(Calendar.MILLISECOND);
            calendar.set(Calendar.HOUR_OF_DAY, firstHourOfDay);
            calendar.set(Calendar.MINUTE, firstMinute);
            calendar.set(Calendar.SECOND, firstSecond);
            calendar.set(Calendar.MILLISECOND, firstMillisecond);
            return calendar.getTime();
        }

        return null;
    }

    public static Date getEndOfDayDate(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int firstHourOfDay = calendar.getActualMaximum(Calendar.HOUR_OF_DAY);
            int firstMinute = calendar.getActualMaximum(Calendar.MINUTE);
            int firstSecond = calendar.getActualMaximum(Calendar.SECOND);
            int firstMillisecond = calendar.getActualMinimum(Calendar.MILLISECOND);
            calendar.set(Calendar.HOUR_OF_DAY, firstHourOfDay);
            calendar.set(Calendar.MINUTE, firstMinute);
            calendar.set(Calendar.SECOND, firstSecond);
            calendar.set(Calendar.MILLISECOND, firstMillisecond);
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

        return get24HoursDateFormat().format(date);
    }

    private static SimpleDateFormat get24HoursDateFormat() {
        return new SimpleDateFormat("HH:mm", Locale.UK);
    }

    public static Date setDatesDay(Date example, Date subject) {
        Calendar exampleCalendar = Calendar.getInstance();
        exampleCalendar.setTime(example);
        Calendar subjectCalendar = Calendar.getInstance();
        subjectCalendar.setTime(subject);

        subjectCalendar.set(Calendar.YEAR, exampleCalendar.get(Calendar.YEAR));
        subjectCalendar.set(Calendar.MONTH, exampleCalendar.get(Calendar.MONTH));
        subjectCalendar.set(Calendar.DAY_OF_MONTH, exampleCalendar.get(Calendar.DAY_OF_MONTH));

        return subjectCalendar.getTime();
    }

    public static boolean isTheSameDay(Date comparableDate, Date comparedDate) {
        if (comparableDate == null) {
            comparableDate = new Date();
        }
        if (comparedDate == null) {
            comparedDate = new Date();
        }

        Calendar comparable = Calendar.getInstance();
        comparable.setTime(comparableDate);
        Calendar compared = Calendar.getInstance();
        compared.setTime(comparedDate);

        return isTheSameDay(comparable, compared);
    }

    public static boolean isTheSameDay(Calendar comparable, Calendar compared) {
        return comparable.get(Calendar.YEAR) == compared.get(Calendar.YEAR)
                && comparable.get(Calendar.MONTH) == comparable.get(Calendar.MONTH)
                && comparable.get(Calendar.DAY_OF_MONTH) == comparable.get(Calendar.DAY_OF_MONTH);
    }

    public static Single<Boolean> areDatesCorrectPeriodAsync(Date startDate, Date endDate) {
        return Single.create(emitter -> {
            if (!emitter.isDisposed()) {
                emitter.onSuccess(areDatesCorrectPeriod(startDate, endDate));
            }
        });
    }

    public static boolean areDatesCorrectPeriod(Date startDate, Date endDate) {
        return startDate.getTime() < endDate.getTime();
    }
}
