package com.youngsoft.sugartracker;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.youngsoft.sugartracker.dashboardp.FragmentDashboard;
import com.youngsoft.sugartracker.mealslistp.FragmentMeals;
import com.youngsoft.sugartracker.sugarlistp.FragmentSugar;

public class MainActivity extends AppCompatActivity {

    Fragment fragmentDashboard = new FragmentDashboard();
    Fragment fragmentSugar = new FragmentSugar();
    Fragment fragmentMeal = new FragmentMeals();
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
    }

}
