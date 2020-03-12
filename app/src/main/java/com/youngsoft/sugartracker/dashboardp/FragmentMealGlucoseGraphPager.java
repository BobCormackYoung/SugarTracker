package com.youngsoft.sugartracker.dashboardp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.youngsoft.sugartracker.R;

/**
 * FragmentMealGlucoseGraphPager
 * Fragment for each individual view of the horizontal scrolling viewPager
 * Used by {@link AdapterMealGlucoseGraphPager}
 */
public class FragmentMealGlucoseGraphPager extends Fragment {

    View view;
    int position;
    TextView tvDebug;

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
        mapViews();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvDebug.setText("" + position);
    }

    private void mapViews() {
        tvDebug = view.findViewById(R.id.tv_debug);
    }
}
