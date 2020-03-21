package com.youngsoft.sugartracker.dashboardp;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.youngsoft.sugartracker.UtilMethods;

import java.util.Calendar;

/**
 * Adapter for horizontal scrolling viewPager
 * Shows the individual view for the view pager
 */
public class AdapterMealGlucoseGraphPager extends FragmentStatePagerAdapter {

    int num_items = 5;
    long startDate;
    long endDate;
    int numDays;

    public AdapterMealGlucoseGraphPager(@NonNull FragmentManager fm, int behavior, int numDays) {
        super(fm, behavior);
        this.numDays = numDays;
        Log.i("AMGGP const","numDays: " + numDays);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Log.i("AMGGP getItem","numDays: " + numDays);

        //set the latest date based on the position in the scrollView & number of items
        Calendar latestDate = Calendar.getInstance();
        latestDate = UtilMethods.setCalendarToEndOfDay(latestDate);
        latestDate.add(Calendar.DATE, (position+1-num_items)*numDays);
        Log.i("AMGGP getItem", "LD: " + UtilMethods.convertDate(latestDate.getTimeInMillis(),"yyyy-MM-dd HH:mm:ss"));

        //set the earliest date of the range to be displayed
        Calendar earliestDate = Calendar.getInstance();
        earliestDate.setTimeInMillis(latestDate.getTimeInMillis());
        earliestDate.add(Calendar.DATE,-numDays-1);
        earliestDate.add(Calendar.MILLISECOND,2);
        Log.i("AMGGP getItem", "ED: " + UtilMethods.convertDate(earliestDate.getTimeInMillis(),"yyyy-MM-dd HH:mm:ss"));

        //instantiate the new fragment
        Fragment fragment = new FragmentMealGlucoseGraphPager();

        //send data to the fragment as arguments
        Bundle args = new Bundle();
        args.putInt("position" , position);
        args.putInt("numDays", numDays);
        args.putLong("earliestDate", earliestDate.getTimeInMillis());
        args.putLong("latestDate", latestDate.getTimeInMillis());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getCount() {
        return num_items;
    }

    public void setCount(int num_items) {
        this.num_items = num_items;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }
}
