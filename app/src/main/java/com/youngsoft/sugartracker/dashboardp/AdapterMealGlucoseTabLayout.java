package com.youngsoft.sugartracker.dashboardp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AdapterMealGlucoseTabLayout extends FragmentPagerAdapter {

    static final int NUM_ITEMS = 3;

    public AdapterMealGlucoseTabLayout(@NonNull FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new FragmentMealGlucosePager();
        Bundle args = new Bundle();
        if (position == 0) {
            args.putInt("numDays", 7);
        } else if (position == 1) {
            args.putInt("numDays", 7*4);
        } else if (position == 2) {
            args.putInt("numDays", 12*30);
        } else {
            args.putInt("numDays", 1);
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title;
        if (position == 0) {
            title = "7D";
        } else if (position == 1) {
            title = "4W";
        } else if (position == 2) {
            title = "1Y";
        } else {
            title = "error";
        }
        return title;
    }
}