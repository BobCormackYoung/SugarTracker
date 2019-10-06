package com.youngsoft.sugartracker;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
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

        viewModelMainActivity.getAllSugarMeasurementsSortedByDate().observe(getViewLifecycleOwner(), new Observer<List<SugarMeasurement>>() {
            @Override
            public void onChanged(List<SugarMeasurement> sugarMeasurements) {
                Log.i("FragDashboard","sugarMeasurements.size() " + sugarMeasurements.size());
                values.clear();
                for (int i = 0; i < sugarMeasurements.size(); i++) {
                    Log.i("FragDashboard","i = " + i);
                    //values.add(new Entry(sugarMeasurements.get(i).getDate(),(long) sugarMeasurements.get(i).getMeasurement()));
                    Log.i("FragDashboard","" + sugarMeasurements.get(i).getDate() + " " + (long) sugarMeasurements.get(i).getMeasurement());
                    values.add(new Entry(i,(long) sugarMeasurements.get(i).getMeasurement()));
                }
                Log.i("FragDashboard","values.size() " + values.size());

                if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
                    //data & chart already exist
                    Log.i("FragDashboard","LineChart case 1");
                    set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
                    set1.setValues(values);
                    lineChart.getData().notifyDataChanged();
                    lineChart.notifyDataSetChanged();
                } else {
                    Log.i("FragDashboard","LineChart case 2");
                    set1 = new LineDataSet(values, "Sample Data");
                    set1.setDrawIcons(false);
                    set1.enableDashedLine(10f, 5f, 0f);
                    set1.enableDashedHighlightLine(10f, 5f, 0f);
                    set1.setColor(Color.DKGRAY);
                    set1.setCircleColor(Color.DKGRAY);
                    set1.setLineWidth(1f);
                    set1.setCircleRadius(3f);
                    set1.setDrawCircleHole(false);
                    set1.setValueTextSize(9f);
                    set1.setDrawFilled(true);
                    set1.setFormLineWidth(1f);
                    set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                    set1.setFormSize(15.f);
                    if (Utils.getSDKInt() >= 18) {
                        //Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
                        //set1.setFillDrawable(drawable);
                        set1.setFillColor(Color.DKGRAY);
                    } else {
                        set1.setFillColor(Color.DKGRAY);
                    }
                    dataSets.add(set1);
                    Log.i("FragDashboard","dataSets.size() " + dataSets.size());
                    LineData data = new LineData(dataSets);
                    Log.i("FragDashboard","data count " + data.getDataSetCount());
                    lineChart.setData(data);
                }
            }
        });

        //ArrayList<Entry> values = new ArrayList<>();
        //values.add(new Entry(1, 50));
        //values.add(new Entry(2, 100));

        //LineDataSet set1;



    }
}
