package com.youngsoft.sugartracker;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.youngsoft.sugartracker.dashboardp.FragmentDashboard;
import com.youngsoft.sugartracker.mealslistp.FragmentMeals;
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
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();
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
