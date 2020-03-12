package com.youngsoft.sugartracker.dashboardp;

import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.youngsoft.sugartracker.R;
import com.youngsoft.sugartracker.ViewModelMainActivity;
import com.youngsoft.sugartracker.data.SugarMeasurement;

import java.util.HashMap;
import java.util.List;

public class FragmentDashboard extends Fragment {

    //TODO: https://developer.android.com/guide/navigation/navigation-swipe-view
    //TODO: https://developer.android.com/reference/androidx/fragment/app/FragmentPagerAdapter

    static final int NUM_ITEMS = 3;
    View view;


    private ViewModelMainActivity viewModelMainActivity;
    private ViewModelDashboard viewModelDashboard;
    int viewCase;

    AdapterMealGlucoseTabLayout adapterMealGlucoseTabLayout;
    ViewPager viewPagerDashboard;
    TabLayout tabLayout;
    TextView tvDataBeforeBreakfast;
    TextView tvDataAfterBreakfast;
    TextView tvDataAfterLunch;
    TextView tvDataAfterDinner;
    TextView tvDataAfterSupper;
    HashMap<String, Double> dailySummaryMap = new HashMap<>();
    DecimalFormat decimalFormat;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the fragment for the entire dashboard view
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //Map all views for data display
        mapViews();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Create adapter for the glucose measurement tab layout
        //this adapter will handle switching between tabs for the different date ranges
        adapterMealGlucoseTabLayout = new AdapterMealGlucoseTabLayout(getChildFragmentManager());

        //assign adapter to the tab viewPager
        viewPagerDashboard.setAdapter(adapterMealGlucoseTabLayout);

        //assign the viewPager to the tabLayout
        tabLayout.setupWithViewPager(viewPagerDashboard);

        //set up viewModels for the data
        //TODO: can this all go in the viewModelDashboard? remove call back to the mainActivity viewModel
        viewModelMainActivity = ViewModelProviders.of(getActivity()).get(ViewModelMainActivity.class);
        viewModelDashboard = ViewModelProviders.of(this).get(ViewModelDashboard.class);

        //set up a decimal format for displaying glucose measurements
        decimalFormat = new DecimalFormat("#");

        //initialise daily summary hashMap to indicate that nothing is stored
        initialiseDailySummaryMap();

        //observe for changes to the set of recorded values for today
        viewModelDashboard.getSugarMeasurementTodayLiveData().observe(getViewLifecycleOwner(), new Observer<List<SugarMeasurement>>() {
            @Override
            public void onChanged(List<SugarMeasurement> sugarMeasurements) {
                getDailySummary(sugarMeasurements);
            }
        });

    }

    /**
     * Set all daily summary hashMap values to -1, which means that no data is stored for this particular value.
     * If values are != -1 then the actual value will be displayed.
     */
    private void initialiseDailySummaryMap() {
        dailySummaryMap.put("beforeBreakfast", -1.0);
        dailySummaryMap.put("afterBreakfast", -1.0);
        dailySummaryMap.put("afterLunch", -1.0);
        dailySummaryMap.put("afterDinner", -1.0);
        dailySummaryMap.put("afterSupper", -1.0);
    }

    /**
     * loop through a list of sugarMeasurements and assign them to the hashMap of
     * daily measurements for displaying later
     * @param sugarMeasurements
     */
    private void getDailySummary(List<SugarMeasurement> sugarMeasurements) {

        //check if the input array is null,
        //and set all values to default -1,
        // which means nothing is stored for that hash
        if (sugarMeasurements == null) {
            dailySummaryMap.replace("beforeBreakfast", -1.0);
            dailySummaryMap.replace("afterBreakfast", -1.0);
            dailySummaryMap.replace("afterLunch", -1.0);
            dailySummaryMap.replace("afterDinner", -1.0);
            dailySummaryMap.replace("afterSupper", -1.0);
        } else {
            //"Breakfast" = 1
            //"Brunch" = 2
            //"Lunch" = 3
            //"Dinner" = 4
            //"Supper" = 5
            //"Snack" = 6
            //"Other" = 7
            //default = -1;

            //loop through all items in the input arrayList and assign to the appropriate
            //hash in the hashMap if meets specific criteroa
            for (int i = 0; i < sugarMeasurements.size(); i++) {
                switch (sugarMeasurements.get(i).getAssociatedMealType()) {
                    case 1:
                        //breakfast
                        if (sugarMeasurements.get(i).getMealSequence()== 1) {
                            dailySummaryMap.replace("beforeBreakfast", sugarMeasurements.get(i).getMeasurement());
                        } else if (sugarMeasurements.get(i).getMealSequence() == 2) {
                            dailySummaryMap.replace("afterBreakfast", sugarMeasurements.get(i).getMeasurement());
                        }
                        break;
                    case 3:
                        if (sugarMeasurements.get(i).getMealSequence() == 2) {
                            dailySummaryMap.replace("afterLunch", sugarMeasurements.get(i).getMeasurement());
                        }
                        break;
                    case 4:
                        if (sugarMeasurements.get(i).getMealSequence() == 2) {
                            dailySummaryMap.replace("afterDinner", sugarMeasurements.get(i).getMeasurement());
                        }
                        break;
                    case 5:
                        if (sugarMeasurements.get(i).getMealSequence() == 2) {
                            dailySummaryMap.replace("afterSupper", sugarMeasurements.get(i).getMeasurement());
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        //update the appropriate views to display the data
        updateDailySummaryViews(dailySummaryMap);
    }

    /**
     * update the UI views for the daily summary to show the
     * stored sugarmeasurement values
     * @param dailySummaryMap
     */
    private void updateDailySummaryViews(HashMap<String, Double> dailySummaryMap) {
        //check all positions in the hashMap, and update the appropriate views
        if (dailySummaryMap.get("beforeBreakfast") == -1) {
            tvDataBeforeBreakfast.setText(" - ");
        } else {
            tvDataBeforeBreakfast.setText(decimalFormat.format(dailySummaryMap.get("beforeBreakfast")));
        }
        if (dailySummaryMap.get("afterBreakfast") == -1) {
            tvDataAfterBreakfast.setText(" - ");
        } else {
            tvDataAfterBreakfast.setText(decimalFormat.format(dailySummaryMap.get("afterBreakfast")));
        }
        if (dailySummaryMap.get("afterLunch") == -1) {
            tvDataAfterLunch.setText(" - ");
        } else {
            tvDataAfterLunch.setText(decimalFormat.format(dailySummaryMap.get("afterLunch")));
        }
        if (dailySummaryMap.get("afterDinner") == -1) {
            tvDataAfterDinner.setText(" - ");
        } else {
            tvDataAfterDinner.setText(decimalFormat.format(dailySummaryMap.get("afterDinner")));
        }
        if (dailySummaryMap.get("afterSupper") == -1) {
            tvDataAfterSupper.setText(" - ");
        } else {
            tvDataAfterSupper.setText(decimalFormat.format(dailySummaryMap.get("afterSupper")));
        }
    }

    private void mapViews() {
        viewPagerDashboard = view.findViewById(R.id.vp_dashboard_meal_glucose);
        tvDataBeforeBreakfast = view.findViewById(R.id.tv_dashboard_before_breakfast);
        tvDataAfterBreakfast = view.findViewById(R.id.tv_dashboard_after_breakfast);
        tvDataAfterLunch = view.findViewById(R.id.tv_dashboard_after_lunch);
        tvDataAfterDinner = view.findViewById(R.id.tv_dashboard_after_dinner);
        tvDataAfterSupper = view.findViewById(R.id.tv_dashboard_after_supper);
        tabLayout = view.findViewById(R.id.tl_vp_dashboard_meal_glucose);
    }
}
