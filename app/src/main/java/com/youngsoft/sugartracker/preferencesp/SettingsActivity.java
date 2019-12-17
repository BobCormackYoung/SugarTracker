package com.youngsoft.sugartracker.preferencesp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.youngsoft.sugartracker.R;

public class SettingsActivity extends AppCompatActivity {

    Fragment fragmentPreferences = new PreferencesFragment();
    public static final String KEY_PREF_EDITTEXT1 = "edit_text_preference_1";
    public static final String KEY_PREF_SWITCH = "switch_preference_1";
    public static final String KEY_PREF_EDITTEXT2 = "edit_text_preference_2";
    public static final String KEY_PREF_EDITTEXT3 = "edit_text_preference_3";

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
