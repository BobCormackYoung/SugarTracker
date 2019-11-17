package com.youngsoft.sugartracker.weekviewp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.youngsoft.sugartracker.R;
import com.youngsoft.sugartracker.ViewModelMainActivity;
import com.youngsoft.sugartracker.data.SugarMeasurement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentWeekView extends Fragment {

    View view;
    RecyclerView recyclerView;
    AdapterWeekView adapterWeekView;
    ViewModelMainActivity viewModelMainActivity;
    long newestDate; //newest date with a sugar measurement recorded
    long oldestDate; //oldest date with a sugar measurement recorded
    long weekCount; //number of weeks stored in the database
    ArrayList<WeekDatesItem> weekDatesArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_week_view, container, false);
        recyclerView = view.findViewById(R.id.rv_week_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModelMainActivity = ViewModelProviders.of(getActivity()).get(ViewModelMainActivity.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        //adapterWeekView = new AdapterWeekView(this, viewModelWeekView, viewModelMainActivity.getDataRepository());
        adapterWeekView = new AdapterWeekView(this, viewModelMainActivity.getDataRepository());
        recyclerView.setAdapter(adapterWeekView);

        viewModelMainActivity.getAllSugarMeasurementsSortedByDate().observe(getViewLifecycleOwner(), new Observer<List<SugarMeasurement>>() {
            @Override
            public void onChanged(List<SugarMeasurement> sugarMeasurements) {
                //TODO: handle if the observed item is null
                Calendar tempCalendar = Calendar.getInstance();
                newestDate = tempCalendar.getTimeInMillis(); //get the newest date from the existing data
                oldestDate = sugarMeasurements.get(sugarMeasurements.size()-1).getDate(); //get the oldest date from the existing data
                weekCount = 1+(newestDate-oldestDate)/(1000*60*60*24*7);
                weekDatesArrayList.clear();

                tempCalendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
                long startDateOfFirstWeek = tempCalendar.getTimeInMillis();
                for (int item = 0; item < weekCount+1; item++) {
                    tempCalendar.setTimeInMillis(startDateOfFirstWeek-item*1000*60*60*24*7);
                    weekDatesArrayList.add(new WeekDatesItem(tempCalendar.getTimeInMillis(),tempCalendar.getTimeInMillis()+1000*60*60*24*7));
                }
                adapterWeekView.submitList(weekDatesArrayList);
            }
        });
    }
}
