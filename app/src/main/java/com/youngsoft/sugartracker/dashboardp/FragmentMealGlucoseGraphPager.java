package com.youngsoft.sugartracker.dashboardp;

import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.youngsoft.sugartracker.R;
import com.youngsoft.sugartracker.UtilMethods;
import com.youngsoft.sugartracker.ViewModelMainActivity;
import com.youngsoft.sugartracker.data.SugarMeasurement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * FragmentMealGlucoseGraphPager
 * Fragment for each individual view of the horizontal scrolling viewPager
 * Used by {@link AdapterMealGlucoseGraphPager}
 */
public class FragmentMealGlucoseGraphPager extends Fragment {

    private ViewModelMainActivity viewModelMainActivity;

    View view;
    int position;
    TextView tvDebug;
    int numDays;
    Long earliestDate;
    Long latestDate;

    Calendar earliestCalendar;
    Calendar latestCalendar;

    LineChart lineChart;
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
    float min;
    float max;
    long initValue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the view and get required arguments
        //required arguments:
        // 1) start date for the graph
        // 2) end date for the grah
        // 3) graph view range (i.e. week, month, year)
        view = inflater.inflate(R.layout.fragment_dashboard_meal_glucose_graph,container,false);
        Bundle args = getArguments();
        position = args.getInt("position",-1);
        numDays = args.getInt("numDays",1);
        earliestDate = args.getLong("earliestDate", 1);
        latestDate = args.getLong("latestDate", 1);
        mapViews();

        lineChart.setTouchEnabled(false);
        lineChart.setPinchZoom(false);

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
        xAxis.setAxisMinimum((float) (earliestDate/1000));
        xAxis.setAxisMaximum((float) (latestDate/1000));

        YAxis yaxis = lineChart.getAxisLeft();
        yaxis.setAxisMinimum(40f);
        yaxis.setAxisMaximum(160f);

        lineChart.getAxisRight().setEnabled(false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModelMainActivity = ViewModelProviders.of(getActivity()).get(ViewModelMainActivity.class);

        tvDebug.setText("Pos: " + position + " NumDays: " + numDays +
                " ED: " + UtilMethods.convertDate(earliestDate,"yyyy-MM-dd HH:mm:ss") +
                " LD:" + UtilMethods.convertDate(latestDate,"yyyy-MM-dd HH:mm:ss"));

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
                lineChart.setData(data);

            }
        });
    }

    private void mapViews() {
        lineChart = view.findViewById(R.id.chart1);
        tvDebug = view.findViewById(R.id.tv_debug);
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
