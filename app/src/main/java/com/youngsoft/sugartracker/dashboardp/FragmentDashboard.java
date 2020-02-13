package com.youngsoft.sugartracker.dashboardp;

import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.util.Log;
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

        adapterMealGlucoseTabLayout = new AdapterMealGlucoseTabLayout(getChildFragmentManager());
        viewPagerDashboard.setAdapter(adapterMealGlucoseTabLayout);

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
                        switch (sugarMeasurements.get(i).getAssociatedMealType()) {
                            case 1:
                                //breakfast
                                if (sugarMeasurements.get(i).getIsFirstMeasurementOfDay()) {
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

    /*public class AdapterDashboardTabLayout2 extends FragmentPagerAdapter {


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
                args.putInt("numDays", 12*30);
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
    }*/

    private void mapViews() {
        viewPagerDashboard = view.findViewById(R.id.vp_dashboard);
        tvDataBeforeBreakfast = view.findViewById(R.id.tv_dashboard_before_breakfast);
        tvDataAfterBreakfast = view.findViewById(R.id.tv_dashboard_after_breakfast);
        tvDataAfterLunch = view.findViewById(R.id.tv_dashboard_after_lunch);
        tvDataAfterDinner = view.findViewById(R.id.tv_dashboard_after_dinner);
        tvDataAfterSupper = view.findViewById(R.id.tv_dashboard_after_supper);
    }

    /*public static class FragmentDashboardPager extends Fragment {

        View pagerView;
        LineChart lineChart;
        private ViewModelMainActivity viewModelMainActivity;
        LineDataSet set1;
        LineDataSet set2;
        LineDataSet set3;
        LineDataSet set4;
        ArrayList<Entry> valuesAfterSupper = new ArrayList<>();
        ArrayList<Entry> valuesAfterDinner = new ArrayList<>();
        ArrayList<Entry> valuesAfterBreakfast = new ArrayList<>();
        ArrayList<Entry> valuesBeforeBreakfast = new ArrayList<>();
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
            //xAxis.setTypeface(tfLight);

            if (numDays == 7) {
                xAxis.setLabelCount(8,true);
                //xAxis.setGranularity(86400f); // only intervals of 1 day
            } else if (numDays == 7*4) {
                xAxis.setLabelCount(7*4/2+1,true);
                //xAxis.setGranularity(2*86400f); // only intervals of 1 day
            } else if (numDays == 12*30) {
                xAxis.setLabelCount(12+1,true);
                //xAxis.setGranularity(30*86400f); // only intervals of 1 day
            } else {
                //xAxis.setLabelCount(7);
                //xAxis.setGranularity(86400000f); // only intervals of 1 day
            }

            xAxis.setValueFormatter(new DayAxisValueFormatter(lineChart));
            xAxis.setAxisMinimum((float) (cStart.getTimeInMillis()/1000));
            xAxis.setAxisMaximum((float) (c.getTimeInMillis()/1000));

            YAxis yaxis = lineChart.getAxisLeft();
            yaxis.setAxisMinimum(40f);
            yaxis.setAxisMaximum(160f);

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
                    valuesAfterBreakfast.clear();
                    valuesAfterDinner.clear();
                    valuesAfterSupper.clear();
                    valuesBeforeBreakfast.clear();
                    for (int i = 0; i < sugarMeasurements.size(); i++) {

                        if (i==0) {
                            initValue = sugarMeasurements.get(i).getDate();
                        }

                        if (sugarMeasurements.get(i).getIsFirstMeasurementOfDay() == true) {
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
                    set1.setColor(Color.TRANSPARENT);
                    int colorAfterDinner = ContextCompat.getColor(getActivity(), R.color.colorAfterDinner);
                    set1.setCircleColor(colorAfterDinner);
                    set1.setLineWidth(0f);
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
                    set2.setColor(Color.TRANSPARENT);
                    int colorBeforeBreakfast = ContextCompat.getColor(getActivity(), R.color.colorBeforeBreakfast);
                    set2.setCircleColor(colorBeforeBreakfast);
                    set2.setLineWidth(0f);
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
                    set3.setColor(Color.TRANSPARENT);
                    int colorAfterBreakfast = ContextCompat.getColor(getActivity(), R.color.colorAfterBreakfast);
                    set3.setCircleColor(colorAfterBreakfast);
                    set3.setLineWidth(0f);
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
                    set4.setColor(Color.TRANSPARENT);
                    int colorAfterSupper = ContextCompat.getColor(getActivity(), R.color.colorAfterSupper);
                    set4.setCircleColor(colorAfterSupper);
                    set4.setLineWidth(0f);
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

                if (numDays == 12*30) {
                    return DateFormat.format("MM/yyyy", millis*1000).toString();
                } else if (numDays == 7*4) {
                    return DateFormat.format("dd/MM", millis*1000).toString();
                } else {
                    return DateFormat.format("dd/MM", millis*1000).toString();
                }
            }
        }
    }*/
}
