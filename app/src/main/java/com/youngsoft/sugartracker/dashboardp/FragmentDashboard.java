package com.youngsoft.sugartracker.dashboardp;

import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.tabs.TabLayout;
import com.youngsoft.sugartracker.R;
import com.youngsoft.sugartracker.UtilMethods;
import com.youngsoft.sugartracker.ViewModelMainActivity;
import com.youngsoft.sugartracker.data.SugarMeasurement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentDashboard extends Fragment {

    //TODO: https://developer.android.com/guide/navigation/navigation-swipe-view
    //TODO: https://developer.android.com/reference/androidx/fragment/app/FragmentPagerAdapter

    static final int NUM_ITEMS = 3;
    View view;


    private ViewModelMainActivity viewModelMainActivity;
    int viewCase;

    AdapterDashboardTabLayout2 adapterDashboardTabLayout;
    ViewPager viewPagerDashboard;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mapViews();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapterDashboardTabLayout = new AdapterDashboardTabLayout2(getChildFragmentManager());
        viewPagerDashboard.setAdapter(adapterDashboardTabLayout);

        TabLayout tabLayout = view.findViewById(R.id.tl_vp_dashboard);
        tabLayout.setupWithViewPager(viewPagerDashboard);

        viewModelMainActivity = ViewModelProviders.of(getActivity()).get(ViewModelMainActivity.class);

    }

    public class AdapterDashboardTabLayout2 extends FragmentPagerAdapter {


        public AdapterDashboardTabLayout2(@NonNull FragmentManager fm) {
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new FragmentDashboardPager();
            Bundle args = new Bundle();
            if (position == 0) {
                args.putInt("numDays", 7);
            } else if (position == 1) {
                args.putInt("numDays", 7*4);
            } else if (position == 2) {
                args.putInt("numDays", 365);
            } else {
                args.putInt("numDays", 1);
            }

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            String title;
            if (position == 0) {
                title = "7D";
            } else if (position == 1) {
                title = "4W";
            } else if (position == 2) {
                title = "1Y";
            } else {
                title = "error";
            }
            return title;
        }
    }

    private void mapViews() {
        viewPagerDashboard = view.findViewById(R.id.vp_dashboard);
    }



    public static class FragmentDashboardPager extends Fragment {

        View pagerView;
        LineChart lineChart;
        private ViewModelMainActivity viewModelMainActivity;
        LineDataSet set1;
        ArrayList<Entry> values = new ArrayList<>();
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        ValueFormatter xAxisFormatter;
        XAxis xAxis;
        Calendar c;
        Calendar cStart;
        long initValue;
        int numDays;
        float min;
        float max;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            pagerView = inflater.inflate(R.layout.fragment_dashboard_pager,container,false);

            Bundle args = getArguments();
            numDays = args.getInt("numDays",1);

            Log.i("FD","numDays " + numDays);

            lineChart = pagerView.findViewById(R.id.chart1);
            lineChart.setTouchEnabled(false);
            lineChart.setPinchZoom(false);


            c = Calendar.getInstance();
            cStart = Calendar.getInstance();
            cStart.add(Calendar.DATE,-numDays);
            xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelRotationAngle(-90f);
            xAxis.setDrawGridLines(false);
            //xAxis.setTypeface(tfLight);

            if (numDays == 7) {
                xAxis.setLabelCount(7);
                //xAxis.setGranularity(86400000f); // only intervals of 1 day
            } else if (numDays == 7*4) {
                xAxis.setLabelCount(7*4);
                //xAxis.setGranularity(86400000f); // only intervals of 1 day
            } else if (numDays == 365) {
                xAxis.setLabelCount(12);
                //xAxis.setGranularity(1209600000f); // only intervals of 1 day
            } else {
                xAxis.setLabelCount(7);
                //xAxis.setGranularity(86400000f); // only intervals of 1 day
            }

            xAxis.setValueFormatter(new DayAxisValueFormatter(lineChart));
            //xAxis.setAxisMinimum((float) (c.getTimeInMillis()-numDays*24*3600000)/1000);
            xAxis.setAxisMinimum((float) (cStart.getTimeInMillis()/1000));
            xAxis.setAxisMaximum((float) (c.getTimeInMillis()/1000));
            Log.i("FDrange","numDays " + numDays +
                    " start: " + UtilMethods.convertDate(c.getTimeInMillis()-numDays*24*3600000, "yyyy/MM/dd HH:mm:ss")
                    + " end: " + UtilMethods.convertDate(c.getTimeInMillis(), "yyyy/MM/dd HH:mm:ss"));
            Log.i("FDrangeC","numDays " + numDays +
                    " start: " + UtilMethods.convertDate(cStart.getTimeInMillis(), "yyyy/MM/dd HH:mm:ss")
                    + " end: " + UtilMethods.convertDate(c.getTimeInMillis(), "yyyy/MM/dd HH:mm:ss"));

            YAxis yaxis = lineChart.getAxisLeft();
            yaxis.setAxisMinimum(0f);
            yaxis.setAxisMaximum(200f);

            lineChart.getAxisRight().setEnabled(false);

            return pagerView;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            viewModelMainActivity = ViewModelProviders.of(getActivity()).get(ViewModelMainActivity.class);

            viewModelMainActivity.getAllSugarMeasurementsSortedByDateInc().observe(getViewLifecycleOwner(), new Observer<List<SugarMeasurement>>() {
                @Override
                public void onChanged(List<SugarMeasurement> sugarMeasurements) {
                    values.clear();
                    for (int i = 0; i < sugarMeasurements.size(); i++) {

                        if (i==0) {
                            initValue = sugarMeasurements.get(i).getDate();
                        }

                        values.add(new Entry((float) sugarMeasurements.get(i).getDate()/1000,(float) sugarMeasurements.get(i).getMeasurement()));

                    }

                    lineChart.clear();
                    dataSets.clear();

                    //xAxisFormatter = new DayAxisValueFormatter(lineChart, initValue, 86400000);

                    set1 = new LineDataSet(values, "Sample Data");
                    set1.setDrawIcons(false);
                    set1.setDrawCircles(false);
                    set1.setDrawValues(false);
                    set1.setColor(R.color.colorAccent);
                    //set1.setCircleColor(R.color.colorAccent);
                    set1.setLineWidth(3f);
                    //set1.setCircleRadius(6f);
                    //set1.setDrawCircleHole(true);
                    //set1.setValueTextSize(9f);
                    set1.setDrawFilled(false);
                    set1.setFormLineWidth(1f);
                    set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                    set1.setFormSize(15.f);

                    dataSets.add(set1);
                    LineData data = new LineData(dataSets);
                    lineChart.setData(data);

                }
            });

            //textView.setText(Integer.toString(args.getInt("Input",0)));
        }

        private class DayAxisValueFormatter extends ValueFormatter {

            private final BarLineChartBase<?> chart;

            public DayAxisValueFormatter(BarLineChartBase<?> chart) {
                this.chart = chart;
            }

            @Override
            public String getFormattedValue(float value) {

                long millis = (long) value;

                if (numDays == 365) {
                    return DateFormat.format("yyyy/MM", millis*1000).toString();
                } else if (numDays == 7*4) {
                    return DateFormat.format("MM/dd", millis*1000).toString();
                } else {
                    return DateFormat.format("MM/dd", millis*1000).toString();
                }
            }
        }
    }
}
