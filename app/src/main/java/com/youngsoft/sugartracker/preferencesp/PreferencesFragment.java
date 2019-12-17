package com.youngsoft.sugartracker.preferencesp;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.youngsoft.sugartracker.R;

public class PreferencesFragment extends PreferenceFragmentCompat {

    Preference versionName;
    Preference versionCode;

    public PreferencesFragment() {
        //Required empty constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }


}
