package com.youngsoft.sugartracker.dashboardp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Adapter for horizontal scrolling viewPager
 * Shows the individual view for the view pager
 */
public class AdapterMealGlucoseGraphPager extends FragmentStatePagerAdapter {

    int num_items = 5;
    long startDate;
    long endDate;

    public AdapterMealGlucoseGraphPager(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new FragmentMealGlucoseGraphPager();
        Bundle args = new Bundle();
        args.putInt("position" , position);
        fragment.setArguments(args);

        //TODO: instantiate new fragment
        //TODO: provide start & end dates as argument
        //TODO: return fragment

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
