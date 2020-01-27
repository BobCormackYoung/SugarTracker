package com.youngsoft.sugartracker.dashboardp;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.youngsoft.sugartracker.ViewModelMainActivity;
import com.youngsoft.sugartracker.data.SugarMeasurement;

import java.util.ArrayList;
import java.util.Calendar;
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

    AdapterDashboardTabLayout2 adapterDashboardTabLayout;
    ViewPager viewPagerDashboard;
    TextView tvDataBeforeBreakfast;
    TextView tvDataAfterBreakfast;
    TextView tvDataAfterLunch;
    TextView tvDataAfterDinner;
    TextView tvDataAfterSupper;
    HashMap<String, Double> dailySummaryMap = new HashMap<>();

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
        viewModelDashboard = ViewModelProviders.of(this).get(ViewModelDashboard.class);

        final DecimalFormat decimalFormat = new DecimalFormat("#");
        dailySummaryMap.put("beforeBreakfast", -1.0);
        dailySummaryMap.put("afterBreakfast", -1.0);
        dailySummaryMap.put("afterLunch", -1.0);
        dailySummaryMap.put("afterDinner", -1.0);
        dailySummaryMap.put("afterSupper", -1.0);

        viewModelDashboard.getSugarMeasurementLiveData().observe(getViewLifecycleOwner(), new Observer<List<SugarMeasurement>>() {
            @Override
            public void onChanged(List<SugarMeasurement> sugarMeasurements) {
                //check if the list has data or not

                Log.i("FD","Num records: " + sugarMeasurements.size());

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

                    for (int i = 0; i < sugarMeasurements.size(); i++) {
                        Log.i("FD","record " + i + " meal type " + sugarMeasurements.get(i).getAssociatedMealType());
                        switch (sugarMeasurements.get(i).getAssociatedMealType()) {
                            case 1:
                                //breakfast
                                if (sugarMeasurements.get(i).getIsFirstMeasurementOfDay()) {
                                    dailySummaryMap.replace("beforeBreakfast", sugarMeasurements.get(i).getMeasurement());
                                } else if (sugarMeasurements.get(i).getMealSequence() == 2) {
                                    dailySummaryMap.replace("afterBreakfast", sugarMeasurements.get(i).getMeasurement());
                                }
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
        });

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
        tvDataBeforeBreakfast = view.findViewById(R.id.tv_dashboard_before_breakfast);
        tvDataAfterBreakfast = view.findViewById(R.id.tv_dashboard_after_breakfast);
        tvDataAfterLunch = view.findViewById(R.id.tv_dashboard_after_lunch);
        tvDataAfterDinner = view.findViewById(R.id.tv_dashboard_after_dinner);
        tvDataAfterSupper = view.findViewById(R.id.tv_dashboard_after_supper);
    }

    public static class FragmentDashboardPager extends Fragment {

        View pagerView;
        LineChart lineChart;
        private ViewModelMainActivity viewModelMainActivity;
        LineDataSet set1;
        LineDataSet set2;
        ArrayList<Entry> values = new ArrayList<>();
        ArrayList<Entry> valuesBreakfast = new ArrayList<>();
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
                    valuesBreakfast.clear();
                    for (int i = 0; i < sugarMeasurements.size(); i++) {

                        if (i==0) {
                            initValue = sugarMeasurements.get(i).getDate();
                        }

                        if (sugarMeasurements.get(i).getIsFirstMeasurementOfDay() == true) {
                            valuesBreakfast.add(new Entry((float) sugarMeasurements.get(i).getDate()/1000,(float) sugarMeasurements.get(i).getMeasurement()));
                        } else {
                            values.add(new Entry((float) sugarMeasurements.get(i).getDate()/1000,(float) sugarMeasurements.get(i).getMeasurement()));
                        }


                    }

                    lineChart.clear();
                    dataSets.clear();

                    //xAxisFormatter = new DayAxisValueFormatter(lineChart, initValue, 86400000);

                    set1 = new LineDataSet(values, "Meal Data");
                    set1.setDrawIcons(true);
                    set1.setFillAlpha(110);
                    set1.setDrawCircles(true);
                    set1.setDrawValues(false);
                    set1.setColor(Color.BLUE);
                    set1.setCircleColor(Color.BLUE);
                    set1.setLineWidth(3f);
                    set1.setCircleRadius(1.5f);
                    set1.setDrawCircleHole(false);
                    set1.setDrawFilled(false);
                    set1.setFormLineWidth(1f);
                    set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                    set1.setFormSize(15.f);

                    set2 = new LineDataSet(valuesBreakfast, "Breakfast Data");
                    set2.setDrawIcons(true);
                    set2.setFillAlpha(110);
                    set2.setDrawCircles(true);
                    set2.setDrawValues(false);
                    set2.setColor(Color.GREEN);
                    set2.setCircleColor(Color.GREEN);
                    set2.setLineWidth(3f);
                    set2.setCircleRadius(1.5f);
                    set2.setDrawCircleHole(false);
                    set2.setDrawFilled(false);
                    set2.setFormLineWidth(1f);
                    set2.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                    set2.setFormSize(15.f);

                    dataSets.add(set1);
                    dataSets.add(set2);
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
