package com.youngsoft.sugartracker.preferencesp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.youngsoft.sugartracker.R;

public class SettingsActivity extends AppCompatActivity {

    Fragment fragmentPreferences = new PreferencesFragment();
    public static final String KEY_PREF_LIMIT1 = "preference_limit_pre_breakfast";
    public static final String KEY_PREF_SWITCH1 = "preference_limit_highlight_pre_breakfast";
    public static final String KEY_PREF_LIMIT2 = "preference_limit_post_meal";
    public static final String KEY_PREF_SWITCH3 = "preference_limit_highlight_post_meal";
    public static final String KEY_PREF_BUILDNUMBER = "preference_build_number";
    public static final String KEY_PREF_VERSION = "preference_version";
    public static final String SHARED_PREFS_FILENAME = "com.youngsoft.sugartracker_preferences";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbarMainActivity = findViewById(R.id.toolbar_settings_activity);
        setSupportActionBar(toolbarMainActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_settings,
                fragmentPreferences).commit();
    }


}
