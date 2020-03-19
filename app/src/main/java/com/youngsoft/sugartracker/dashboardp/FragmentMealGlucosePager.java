package com.youngsoft.sugartracker.dashboardp;

import android.os.Bundle;
import android.text.format.DateFormat;
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

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;
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
    /*
    LineChart lineChart;
    ineDataSet set1;
    LineDataSet set2;
    LineDataSet set3;
    LineDataSet set4;
    ArrayList<Entry> valuesAfterSupper = new ArrayList<>();
    ArrayList<Entry> valuesAfterDinner = new ArrayList<>();
    ArrayList<Entry> valuesAfterBreakfast = new ArrayList<>();
    ArrayList<Entry> valuesBeforeBreakfast = new ArrayList<>();
    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    ValueFormatter xAxisFormatter;*/
    XAxis xAxis;
    Calendar c;
    Calendar cStart;
    Calendar earliestDisplayableDate;
    Calendar latestDisplayableDate;
    int childGraphFragmentCount;
    long initValue;
    int numDays;
    float min;
    float max;

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

        /*lineChart = pagerView.findViewById(R.id.chart1);
        lineChart.setTouchEnabled(false);
        lineChart.setPinchZoom(false);

        c = Calendar.getInstance();
        c.set(Calendar.MILLISECOND,999);
        c.set(Calendar.SECOND,60);
        c.set(Calendar.MINUTE,60);
        c.set(Calendar.HOUR_OF_DAY,23);

        cStart = Calendar.getInstance();
        cStart.set(Calendar.MILLISECOND,1);
        cStart.set(Calendar.SECOND,0);
        cStart.set(Calendar.MINUTE,0);
        cStart.set(Calendar.HOUR_OF_DAY,0);
        cStart.add(Calendar.DATE,-numDays+1);

        xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(-90f);
        xAxis.setDrawGridLines(true);

        if (numDays == 7) {
            xAxis.setLabelCount(8,true);
        } else if (numDays == 7*4) {
            xAxis.setLabelCount(7*4/2+1,true);
        } else if (numDays == 12*30) {
            xAxis.setLabelCount(12+1,true);
        } else {
        }

        xAxis.setValueFormatter(new DayAxisValueFormatter(lineChart));
        xAxis.setAxisMinimum((float) (cStart.getTimeInMillis()/1000));
        xAxis.setAxisMaximum((float) (c.getTimeInMillis()/1000));

        YAxis yaxis = lineChart.getAxisLeft();
        yaxis.setAxisMinimum(40f);
        yaxis.setAxisMaximum(160f);

        lineChart.getAxisRight().setEnabled(false);*/

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
        adapterMealGlucoseGraphPager = new AdapterMealGlucoseGraphPager(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
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
                /*valuesAfterBreakfast.clear();
                valuesAfterDinner.clear();
                valuesAfterSupper.clear();
                valuesBeforeBreakfast.clear();
                for (int i = 0; i < sugarMeasurements.size(); i++) {

                    if (i==0) {
                        initValue = sugarMeasurements.get(i).getDate();
                    }

                    if (sugarMeasurements.get(i).getAssociatedMealType() == 1 && sugarMeasurements.get(i).getMealSequence() == 1) {
                        valuesBeforeBreakfast.add(new Entry((float) sugarMeasurements.get(i).getDate()/1000,(float) sugarMeasurements.get(i).getMeasurement()));
                    } else {
                        if (sugarMeasurements.get(i).getAssociatedMealType() == 1) {
                            valuesAfterBreakfast.add(new Entry((float) sugarMeasurements.get(i).getDate()/1000,(float) sugarMeasurements.get(i).getMeasurement()));
                        } else if (sugarMeasurements.get(i).getAssociatedMealType() == 4) {
                            valuesAfterDinner.add(new Entry((float) sugarMeasurements.get(i).getDate()/1000,(float) sugarMeasurements.get(i).getMeasurement()));
                        } else if (sugarMeasurements.get(i).getAssociatedMealType() == 5) {
                            valuesAfterSupper.add(new Entry((float) sugarMeasurements.get(i).getDate()/1000,(float) sugarMeasurements.get(i).getMeasurement()));
                        }
                    }


                }

                lineChart.clear();
                dataSets.clear();

                //xAxisFormatter = new DayAxisValueFormatter(lineChart, initValue, 86400000);

                set1 = new LineDataSet(valuesAfterDinner, "After Dinner");
                set1.setDrawIcons(true);
                set1.setFillAlpha(0);
                set1.setDrawCircles(true);
                set1.setDrawValues(false);
                int colorAfterDinner = ContextCompat.getColor(getActivity(), R.color.colorAfterDinner);
                set1.setColor(colorAfterDinner);
                set1.setCircleColor(colorAfterDinner);
                set1.setLineWidth(2f);
                set1.setCircleRadius(3f);
                set1.setDrawCircleHole(false);
                set1.setDrawFilled(false);
                set1.setFormLineWidth(1f);
                set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                set1.setFormSize(15.f);

                set2 = new LineDataSet(valuesBeforeBreakfast, "Before Breakfast");
                set2.setDrawIcons(true);
                set2.setFillAlpha(0);
                set2.setDrawCircles(true);
                set2.setDrawValues(false);
                int colorBeforeBreakfast = ContextCompat.getColor(getActivity(), R.color.colorBeforeBreakfast);
                set2.setColor(colorBeforeBreakfast);
                set2.setCircleColor(colorBeforeBreakfast);
                set2.setLineWidth(2f);
                set2.setCircleRadius(3f);
                set2.setDrawCircleHole(false);
                set2.setDrawFilled(false);
                set2.setFormLineWidth(1f);
                set2.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                set2.setFormSize(15.f);

                set3 = new LineDataSet(valuesAfterBreakfast, "After Breakfast");
                set3.setDrawIcons(true);
                set3.setFillAlpha(0);
                set3.setDrawCircles(true);
                set3.setDrawValues(false);
                int colorAfterBreakfast = ContextCompat.getColor(getActivity(), R.color.colorAfterBreakfast);
                set3.setColor(colorAfterBreakfast);
                set3.setCircleColor(colorAfterBreakfast);
                set3.setLineWidth(2f);
                set3.setCircleRadius(3f);
                set3.setDrawCircleHole(false);
                set3.setDrawFilled(false);
                set3.setFormLineWidth(1f);
                set3.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                set3.setFormSize(15.f);

                set4 = new LineDataSet(valuesAfterSupper, "After Supper");
                set4.setDrawIcons(true);
                set4.setFillAlpha(0);
                set4.setDrawCircles(true);
                set4.setDrawValues(false);
                int colorAfterSupper = ContextCompat.getColor(getActivity(), R.color.colorAfterSupper);
                set4.setColor(colorAfterSupper);
                set4.setCircleColor(colorAfterSupper);
                set4.setLineWidth(2f);
                set4.setCircleRadius(3f);
                set4.setDrawCircleHole(false);
                set4.setDrawFilled(false);
                set4.setFormLineWidth(1f);
                set4.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                set4.setFormSize(15.f);

                dataSets.add(set1);
                dataSets.add(set2);
                dataSets.add(set3);
                dataSets.add(set4);
                LineData data = new LineData(dataSets);
                lineChart.setData(data);*/

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

    private class DayAxisValueFormatter extends ValueFormatter {

        private final BarLineChartBase<?> chart;

        public DayAxisValueFormatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }

        @Override
        public String getFormattedValue(float value) {

            long millis = (long) value;

            if (numDays == 12*30) {
                return DateFormat.format("MM/yyyy", millis*1000).toString();
            } else if (numDays == 7*4) {
                return DateFormat.format("dd/MM", millis*1000).toString();
            } else {
                return DateFormat.format("dd/MM", millis*1000).toString();
            }
        }
    }
}