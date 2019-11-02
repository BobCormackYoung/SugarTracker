package com.youngsoft.sugartracker;

import android.text.format.DateFormat;

public class UtilMethods {

    public static String getMealType(int input) {
        String output = null;

        switch (input) {
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

}
