package com.youngsoft.sugartracker.preferencesp;

import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.youngsoft.sugartracker.R;

public class PreferencesFragment extends PreferenceFragmentCompat implements FragmentPreferencesNumberPicker.OnSaveListener {

    Preference pfVersionName;
    Preference pfVersionCode;
    Preference pfPreBreakfastLimit;
    Preference pfPostMealLimit;

    public PreferencesFragment() {
        //Required empty constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        DecimalFormat decimalFormat = new DecimalFormat("#");

        Preference pfPreBreakfastLimit = findPreference(SettingsActivity.KEY_PREF_LIMIT1);
        pfPreBreakfastLimit.setSummary(decimalFormat.format(Double.longBitsToDouble(preferences.getLong(SettingsActivity.KEY_PREF_LIMIT1,-1))) + " mg/dL");

        Preference pfPostBreakfastLimit = findPreference(SettingsActivity.KEY_PREF_LIMIT2);
        pfPostBreakfastLimit.setSummary(decimalFormat.format(Double.longBitsToDouble(preferences.getLong(SettingsActivity.KEY_PREF_LIMIT2,-1))) + " mg/dL");

        pfPreBreakfastLimit.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                DialogFragment numberPickerFragment = new FragmentPreferencesNumberPicker(PreferencesFragment.this);
                Bundle inputs = new Bundle();
                inputs.putDouble("inputValue",Double.longBitsToDouble(preferences.getLong(SettingsActivity.KEY_PREF_LIMIT1,-1)));
                inputs.putInt("inputKey",1);
                numberPickerFragment.setArguments(inputs);
                numberPickerFragment.show(getChildFragmentManager(), "numberPickerFragment");
                return true;
            }
        });

        pfPostBreakfastLimit.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                DialogFragment numberPickerFragment = new FragmentPreferencesNumberPicker(PreferencesFragment.this);
                Bundle inputs = new Bundle();
                inputs.putDouble("inputValue",Double.longBitsToDouble(preferences.getLong(SettingsActivity.KEY_PREF_LIMIT2,-1)));
                inputs.putInt("inputKey",2);
                numberPickerFragment.setArguments(inputs);
                numberPickerFragment.show(getChildFragmentManager(), "numberPickerFragment");
                return true;
            }
        });
    }

    @Override
    public void onSave(double outputValue, int inputKey) {
        Log.i("onSave",outputValue + " " + inputKey);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();

        DecimalFormat decimalFormat = new DecimalFormat("#");

        if (inputKey == 1) {
            editor.putLong(SettingsActivity.KEY_PREF_LIMIT1, Double.doubleToRawLongBits(outputValue));
            editor.commit();
            Preference pfPreBreakfastLimit = findPreference(SettingsActivity.KEY_PREF_LIMIT1);
            pfPreBreakfastLimit.setSummary(decimalFormat.format(Double.longBitsToDouble(preferences.getLong(SettingsActivity.KEY_PREF_LIMIT1,-1))) + " mg/dL");
        } else if (inputKey == 2) {
            editor.putLong(SettingsActivity.KEY_PREF_LIMIT2, Double.doubleToRawLongBits(outputValue));
            editor.commit();
            Preference pfPostMealLimit = findPreference(SettingsActivity.KEY_PREF_LIMIT2);
            pfPostMealLimit.setSummary(decimalFormat.format(Double.longBitsToDouble(preferences.getLong(SettingsActivity.KEY_PREF_LIMIT2,-1))) + " mg/dL");
        }




    }

// TODO: https://stackoverflow.com/questions/16319237/cant-put-double-sharedpreferences

}
