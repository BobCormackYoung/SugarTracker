package com.youngsoft.sugartracker;

import android.text.format.DateFormat;

import java.util.Calendar;

public class UtilMethods {

    public static String getMealType(int input) {
        String output = null;

        switch (input) {
            case -1:
                output = "None";
                break;
            case 1:
                output = "Breakfast";
                break;
            case 2:
                output = "Brunch";
                break;
            case 3:
                output = "Lunch";
                break;
            case 4:
                output = "Dinner";
                break;
            case 5:
                output = "Supper";
                break;
            case 6:
                output = "Snack";
                break;
            case 7:
                output = "Other";
                break;
        }

        return output;
    }

    public static String convertDate(long dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, dateInMilliseconds).toString();
    }

    public static Calendar setCalendarToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,1);
        return calendar;
    }

    public static Calendar setCalendarToEndOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,999);
        return calendar;
    }

}
