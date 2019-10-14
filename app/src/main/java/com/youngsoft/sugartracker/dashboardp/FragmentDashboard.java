package com.youngsoft.sugartracker.dashboardp;

import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.youngsoft.sugartracker.R;
import com.youngsoft.sugartracker.ViewModelMainActivity;
import com.youngsoft.sugartracker.data.SugarMeasurement;

import java.util.ArrayList;
import java.util.List;

public class FragmentDashboard extends Fragment {

    View view;
    LineChart lineChart;
    LineDataSet set1;
    ArrayList<Entry> values = new ArrayList<>();
    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    private ViewModelMainActivity viewModelMainActivity;
    long initValue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        lineChart = view.findViewById(R.id.chart1);
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(true);

        /*view.findViewById(R.id.bt_debug_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModelMainActivity.addDebugData();
            }
        });*/

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModelMainActivity = ViewModelProviders.of(getActivity()).get(ViewModelMainActivity.class);

        viewModelMainActivity.getAllSugarMeasurementsSortedByDateInc().observe(getViewLifecycleOwner(), new Observer<List<SugarMeasurement>>() {
            @Override
            public void onChanged(List<SugarMeasurement> sugarMeasurements) {
                Log.i("FragDashboard","sugarMeasurements.size() " + sugarMeasurements.size());
                values.clear();
                for (int i = 0; i < sugarMeasurements.size(); i++) { //sugarMeasurements.size()
                    Log.i("FragDashboard","i = " + i);
                    Log.i("FragDashboard","" + sugarMeasurements.get(i).getDate() + " " + (long) sugarMeasurements.get(i).getMeasurement());
                    Log.i("FragDashboard","" + (float) sugarMeasurements.get(i).getDate() + " " + sugarMeasurements.get(i).getDate());

                    if (i==0) {
                        initValue = sugarMeasurements.get(i).getDate();
                        Log.i("FragDashboard", "initValue = " + initValue);
                    }

                    //values.add(new Entry((float) ((sugarMeasurements.get(i).getDate()-initValue)/86400000),(float) sugarMeasurements.get(i).getMeasurement()));
                    values.add(new Entry((float) sugarMeasurements.get(i).getDate(),(float) sugarMeasurements.get(i).getMeasurement()));

                    //values.add(new Entry(i,(long) sugarMeasurements.get(i).getMeasurement()));
                }
                Log.i("FragDashboard","values.size() " + values.size());

                lineChart.clear();
                dataSets.clear();

                ValueFormatter xAxisFormatter = new DayAxisValueFormatter(lineChart, initValue, 86400000);
                XAxis xAxis = lineChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                //xAxis.setTypeface(tfLight);
                xAxis.setDrawGridLines(false);
                xAxis.setGranularity(1f); // only intervals of 1 day
                xAxis.setLabelCount(7);
                xAxis.setValueFormatter(xAxisFormatter);

                set1 = new LineDataSet(values, "Sample Data");
                set1.setDrawIcons(false);
                set1.setColor(R.color.colorAccent);
                set1.setCircleColor(R.color.colorAccent);
                set1.setLineWidth(3f);
                set1.setCircleRadius(6f);
                set1.setDrawCircleHole(true);
                set1.setValueTextSize(9f);
                set1.setDrawFilled(false);
                set1.setFormLineWidth(1f);
                set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                set1.setFormSize(15.f);

                dataSets.add(set1);
                Log.i("FragDashboard","dataSets.size() " + dataSets.size());
                LineData data = new LineData(dataSets);
                Log.i("FragDashboard","data count " + data.getDataSetCount());
                lineChart.setData(data);

            }
        });

        //ArrayList<Entry> values = new ArrayList<>();
        //values.add(new Entry(1, 50));
        //values.add(new Entry(2, 100));

        //LineDataSet set1;





    }

    private class DayAxisValueFormatter extends ValueFormatter {

        private final BarLineChartBase<?> chart;
        private final float initValue;
        private final long multiplier;

        public DayAxisValueFormatter(BarLineChartBase<?> chart, float initValue, long multiplier) {
            this.chart = chart;
            this.initValue = initValue;
            this.multiplier = multiplier;
        }

        @Override
        public String getFormattedValue(float value) {

            //long millis = (long) (value+initValue)*multiplier;
            long millis = (long) value;

            Log.i("DayAxisValueFormatter","" + value + " "
                + initValue + " " + multiplier + " "
                + (value+initValue)*multiplier + " " + millis);


            //TODO: adjust ranges in graph
            if (chart.getVisibleXRange() > 30 * 6) {
                return DateFormat.format("yyyy-MM", millis).toString();
            } else if (chart.getVisibleXRange() < 2 ) {
                return DateFormat.format("HH:mm", millis).toString();
            } else {
                return DateFormat.format("yyyy-MM-dd", millis).toString();
            }
        }
    }
}
