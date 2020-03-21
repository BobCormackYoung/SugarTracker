package com.youngsoft.sugartracker.dashboardp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.components.XAxis;
import com.youngsoft.sugartracker.R;
import com.youngsoft.sugartracker.UtilMethods;
import com.youngsoft.sugartracker.ViewModelMainActivity;
import com.youngsoft.sugartracker.data.SugarMeasurement;

import java.util.Calendar;
import java.util.List;

/**
 * fragment that contains the tabbed viewPager child scrolling viewPager
 */
public class FragmentMealGlucosePager extends Fragment {

    View view;
    ViewPager viewPagerMealGlucoseGraph;
    AdapterMealGlucoseGraphPager adapterMealGlucoseGraphPager;
    private ViewModelMainActivity viewModelMainActivity;

    XAxis xAxis;
    Calendar c;
    Calendar cStart;
    Calendar earliestDisplayableDate;
    Calendar latestDisplayableDate;
    int childGraphFragmentCount;
    int numDays;

    Button debugButton1;
    Button debugButton2;
    Button debugButton3;
    TextView debugTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the view that contains the scrolling viewPager that is used for the graphs
        view = inflater.inflate(R.layout.fragment_dashboard_meal_glucose_pager,container,false);

        //map all appropriate views
        mapViews();

        //get the number of days that will be displayed in each graph
        Bundle args = getArguments();
        numDays = args.getInt("numDays",1);

        Log.i("FD","numDays " + numDays);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //instantiate the viewModel
        //TODO: can this be moved to the dashboard viewModel?
        viewModelMainActivity = ViewModelProviders.of(getActivity()).get(ViewModelMainActivity.class);

        //instantiate the adapter that will scroll through the graphs
        //needs to be passed:
        // 1) number of days per graph,
        // 2) start date (earliest displayable date) - how... needs to be calculated later
        // 3) end date (latest displayable date i.e. end of today) - how... needs to be calculated later
        adapterMealGlucoseGraphPager = new AdapterMealGlucoseGraphPager(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, numDays);
        viewPagerMealGlucoseGraph.setAdapter(adapterMealGlucoseGraphPager);
        viewPagerMealGlucoseGraph.setCurrentItem(adapterMealGlucoseGraphPager.getCount()-1);

        //find the latest date for display
        latestDisplayableDate = Calendar.getInstance();
        latestDisplayableDate = UtilMethods.setCalendarToEndOfDay(latestDisplayableDate);

        //find the earliest date for display
        earliestDisplayableDate = Calendar.getInstance();
        viewModelMainActivity.getOldestSugarMeasurement().observe(getViewLifecycleOwner(), new Observer<SugarMeasurement>() {
            @Override
            public void onChanged(SugarMeasurement sugarMeasurement) {
                if (sugarMeasurement != null) {
                    //if the observed item is not null, then get the date
                    //else use the previously instantiated value
                    earliestDisplayableDate.setTimeInMillis(sugarMeasurement.getDate());
                }
                long earliestDate = earliestDisplayableDate.getTimeInMillis();
                long latestDate = latestDisplayableDate.getTimeInMillis();
                long millisInDay = 86400000;
                long dateRange = latestDate - earliestDate;
                childGraphFragmentCount = (int) (1+dateRange/(numDays*millisInDay));

                debugTextView.setText("ED: " + UtilMethods.convertDate(earliestDate,"yyyy-MM-dd") +
                        " LD: " + UtilMethods.convertDate(latestDate,"yyyy-MM-dd") +
                        " DR: " + dateRange/millisInDay +
                        " CFC: " + childGraphFragmentCount);

                //update the adapter size
                adapterMealGlucoseGraphPager.setCount(childGraphFragmentCount);
                adapterMealGlucoseGraphPager.notifyDataSetChanged();
                viewPagerMealGlucoseGraph.setCurrentItem(adapterMealGlucoseGraphPager.getCount()-1);
            }
        });

        debugButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterMealGlucoseGraphPager.setCount(2);
                adapterMealGlucoseGraphPager.notifyDataSetChanged();
                if (viewPagerMealGlucoseGraph.getCurrentItem()>=2) {
                    viewPagerMealGlucoseGraph.setCurrentItem(adapterMealGlucoseGraphPager.getCount()-1);
                }
            }
        });

        debugButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterMealGlucoseGraphPager.setCount(5);
                adapterMealGlucoseGraphPager.notifyDataSetChanged();
                if (viewPagerMealGlucoseGraph.getCurrentItem()>=5) {
                    viewPagerMealGlucoseGraph.setCurrentItem(adapterMealGlucoseGraphPager.getCount()-1);
                }
            }
        });

        debugButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterMealGlucoseGraphPager.setCount(10);
                adapterMealGlucoseGraphPager.notifyDataSetChanged();
                if (viewPagerMealGlucoseGraph.getCurrentItem()>=10) {
                    viewPagerMealGlucoseGraph.setCurrentItem(adapterMealGlucoseGraphPager.getCount()-1);
                }
            }
        });

        viewModelMainActivity.getAllSugarMeasurementsSortedByDateInc().observe(getViewLifecycleOwner(), new Observer<List<SugarMeasurement>>() {
            @Override
            public void onChanged(List<SugarMeasurement> sugarMeasurements) {


            }
        });

        viewModelMainActivity.getOldestSugarMeasurement().observe(getViewLifecycleOwner(), new Observer<SugarMeasurement>() {
            @Override
            public void onChanged(SugarMeasurement sugarMeasurement) {
                Log.i("FMGP","oldest on changed");
                Log.i("FMGP","Date oldest: " + sugarMeasurement.getDate());
            }
        });

    }

    /**
     * map views for the fragment
     */
    private void mapViews() {

        viewPagerMealGlucoseGraph = view.findViewById(R.id.vp_dashboard_meal_glucose_graph);
        debugButton1 = view.findViewById(R.id.bt_debug_1);
        debugButton2 = view.findViewById(R.id.bt_debug_5);
        debugButton3 = view.findViewById(R.id.bt_debug_10);
        debugTextView = view.findViewById(R.id.tv_debug);

    }


}