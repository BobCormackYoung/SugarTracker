package com.youngsoft.sugartracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.youngsoft.sugartracker.dashboardp.FragmentDashboard;
import com.youngsoft.sugartracker.mealslistp.FragmentMeals;
import com.youngsoft.sugartracker.preferencesp.SettingsActivity;
import com.youngsoft.sugartracker.sugarlistp.FragmentSugar;
import com.youngsoft.sugartracker.weekviewp.FragmentWeekView;

public class MainActivity extends AppCompatActivity {

    Fragment fragmentDashboard = new FragmentDashboard();
    Fragment fragmentSugar = new FragmentSugar();
    Fragment fragmentMeal = new FragmentMeals();
    Fragment fragmentWeek = new FragmentWeekView();
    Fragment selectedFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = fragmentDashboard;
                    break;
                case R.id.navigation_week:
                    selectedFragment = fragmentWeek;
                    break;
                case R.id.navigation_sugar:
                    selectedFragment = fragmentSugar;
                    break;
                case R.id.navigation_meals:
                    selectedFragment = fragmentMeal;
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();


            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragmentDashboard).commit();
        Toolbar toolbarMainActivity = findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(toolbarMainActivity);

        /*
        // Sets default values only once, first time this is called.
        // The third argument is a boolean that indicates whether
        // the default values should be set more than once. When false,
        // the system sets the default values only if this method has never
        // been called in the past.
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        */
        setDefaultSharedPreferences();

        /*
        // Read the settings from the shared preferences and display a toast.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean switchPref = sharedPref.getBoolean(SettingsActivity.KEY_PREF_SWITCH, false);
        Toast.makeText(this, switchPref.toString(),Toast.LENGTH_SHORT).show();
        */
    }

    private void setDefaultSharedPreferences() {

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        if (preferences.getLong(SettingsActivity.KEY_PREF_LIMIT1, -1) == -1) {
            editor.putLong(SettingsActivity.KEY_PREF_LIMIT1, Double.doubleToRawLongBits(100.0));
            editor.commit();
        }

        if (preferences.getLong(SettingsActivity.KEY_PREF_LIMIT2, -1) == -1) {
            editor.putLong(SettingsActivity.KEY_PREF_LIMIT2, Double.doubleToRawLongBits(200.0));
            editor.commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID  was selected
            case R.id.action_licences:
                Toast.makeText(this, "Licences selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                //TODO: https://codelabs.developers.google.com/codelabs/android-training-adding-settings-to-app/index.html?index=..%2F..android-training#2
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            // action with ID  was selected
            case R.id.action_bug_report:
                Toast.makeText(this, "Bug report selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }

        return true;
    }
}
