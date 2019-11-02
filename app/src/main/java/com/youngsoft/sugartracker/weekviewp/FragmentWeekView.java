package com.youngsoft.sugartracker.weekviewp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.youngsoft.sugartracker.R;
import com.youngsoft.sugartracker.UtilMethods;
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
    ArrayList<WeekViewItem> weekViewItemArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_week_view, container, false);
        recyclerView = view.findViewById(R.id.rv_week_view);
        return view;
    }


    //TODO: this - http://blog.sqisland.com/2014/12/recyclerview-grid-with-header.html
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Calendar temp = Calendar.getInstance();
        temp.set(Calendar.DAY_OF_WEEK,temp.getFirstDayOfWeek()); //Set to first day of week
        temp.set(Calendar.HOUR_OF_DAY,0);
        temp.set(Calendar.MINUTE,0);
        temp.set(Calendar.SECOND,0);
        final long startDate = temp.getTimeInMillis();
        long endDate = startDate+(1000*60*60*24*7);
        viewModelMainActivity = ViewModelProviders.of(getActivity()).get(ViewModelMainActivity.class);
        viewModelMainActivity.setSugarMeasurementsBetweenDates(startDate, endDate);

        recyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 5);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int size = 0;
                switch (position) {
                    case 0:
                        size = 1;
                        break;
                    case 1:
                        size = 2;
                        break;
                    case 2:
                        size = 1;
                        break;
                    case 3:
                        size = 1;
                        break;
                    default:
                        size = 1;
                        break;
                }
                return size;
            }
        });

        recyclerView.setLayoutManager(manager);

        adapterWeekView = new AdapterWeekView(viewModelMainActivity.getDataRepository(), this, viewModelMainActivity);
        recyclerView.setAdapter(adapterWeekView);
        viewModelMainActivity.getSugarMeasurementsBetweenDates().observe(getViewLifecycleOwner(), new Observer<List<SugarMeasurement>>() {
            @Override
            public void onChanged(List<SugarMeasurement> sugarMeasurements) {
                weekViewItemArrayList.clear();
                weekViewItemArrayList.add(new WeekViewItem(" "));
                weekViewItemArrayList.add(new WeekViewItem("Breakfast"));
                weekViewItemArrayList.add(new WeekViewItem("Dinner"));
                weekViewItemArrayList.add(new WeekViewItem("Supper"));
                weekViewItemArrayList.add(new WeekViewItem("Day"));
                weekViewItemArrayList.add(new WeekViewItem("B"));
                weekViewItemArrayList.add(new WeekViewItem("A"));
                weekViewItemArrayList.add(new WeekViewItem("A"));
                weekViewItemArrayList.add(new WeekViewItem("A"));

                //Cycle through all days and fill the data for each item
                for (int item = 0; item < 35; item++) {
                    //Define the start and end of the day
                    long dayStart = startDate+(item/5)*(1000*60*60*24);
                    long dayEnd = dayStart+(1000*60*60*24);
                    //Input the first column with day data
                    switch (item) {
                        case 0:
                            weekViewItemArrayList.add(new WeekViewItem("Mon"));
                            break;
                        case 5:
                            weekViewItemArrayList.add(new WeekViewItem("Tue"));
                            break;
                        case 10:
                            weekViewItemArrayList.add(new WeekViewItem("Wed"));
                            break;
                        case 15:
                            weekViewItemArrayList.add(new WeekViewItem("Thu"));
                            break;
                        case 20:
                            weekViewItemArrayList.add(new WeekViewItem("Fri"));
                            break;
                        case 25:
                            weekViewItemArrayList.add(new WeekViewItem("Sat"));
                            break;
                        case 30:
                            weekViewItemArrayList.add(new WeekViewItem("Sun"));
                            break;
                        case 1:
                        case 6:
                        case 11:
                        case 16:
                        case 21:
                        case 26:
                        case 31:
                            weekViewItemArrayList.add(getWeekViewItem(sugarMeasurements, dayStart, dayEnd, 1));
                            break;
                        case 2:
                        case 7:
                        case 12:
                        case 17:
                        case 22:
                        case 27:
                        case 32:
                            weekViewItemArrayList.add(getWeekViewItem(sugarMeasurements, dayStart, dayEnd, 2));
                            break;
                        case 3:
                        case 8:
                        case 13:
                        case 18:
                        case 23:
                        case 28:
                        case 33:
                            weekViewItemArrayList.add(getWeekViewItem(sugarMeasurements, dayStart, dayEnd, 3));
                            break;
                        case 4:
                        case 9:
                        case 14:
                        case 19:
                        case 24:
                        case 29:
                        case 34:
                            weekViewItemArrayList.add(getWeekViewItem(sugarMeasurements, dayStart, dayEnd, 4));
                            break;
                        default:
                            weekViewItemArrayList.add(new WeekViewItem("err"));//not one of the header or day cells, find relevant data for the specific cell
                            break;
                    }


                }

                /*for (int i = 0; i < sugarMeasurements.size(); i++) {
                    SugarMeasurement currentItem = sugarMeasurements.get(i);
                    weekViewItemArrayList.add(new WeekViewItem(currentItem.getId(),
                            currentItem.getDate(),
                            currentItem.getMeasurement(),
                            currentItem.getMealSequence(),
                            currentItem.getAssociatedMealType(),
                            currentItem.getAssociatedMeal(),
                            currentItem.getIsFirstMeasurementOfDay()));
                } */

                //adapterWeekView.submitList(sugarMeasurements);
                adapterWeekView.submitList(weekViewItemArrayList);
            }
        });

    }

    private WeekViewItem getWeekViewItem (List<SugarMeasurement> inputArray, long dayStart, long dayEnd, int criteria) {
        Log.i("getWeekViewItem", "Start Date: " + UtilMethods.convertDate(dayStart, "yyyy-MM-dd  HH:mm:ss") + ", End Date: " +
                UtilMethods.convertDate(dayEnd, "yyyy-MM-dd  HH:mm:ss"));
        for (int i = 0; i < inputArray.size(); i++) {
            SugarMeasurement currentItem = inputArray.get(i);
            //Check if the items is for the current day
            if (currentItem.getDate() < dayEnd && currentItem.getDate() > dayStart) {
                //Check whether meets data criteria
                // 1 = first meal of the day
                // 2 = after breakfast
                // 3 = after dinner
                // 4 = after supper
                if (criteria == 1 && currentItem.getIsFirstMeasurementOfDay()) {
                    return new WeekViewItem(currentItem.getId(),
                            currentItem.getDate(),
                            currentItem.getMeasurement(),
                            currentItem.getMealSequence(),
                            currentItem.getAssociatedMealType(),
                            currentItem.getAssociatedMeal(),
                            currentItem.getIsFirstMeasurementOfDay());
                } else if ((criteria == 2 && currentItem.getMealSequence() == 2 && currentItem.getAssociatedMealType() == 1)) {
                    return new WeekViewItem(currentItem.getId(),
                            currentItem.getDate(),
                            currentItem.getMeasurement(),
                            currentItem.getMealSequence(),
                            currentItem.getAssociatedMealType(),
                            currentItem.getAssociatedMeal(),
                            currentItem.getIsFirstMeasurementOfDay());
                } else if ((criteria == 3 && currentItem.getMealSequence() == 2 && currentItem.getAssociatedMealType() == 4)) {
                    return new WeekViewItem(currentItem.getId(),
                            currentItem.getDate(),
                            currentItem.getMeasurement(),
                            currentItem.getMealSequence(),
                            currentItem.getAssociatedMealType(),
                            currentItem.getAssociatedMeal(),
                            currentItem.getIsFirstMeasurementOfDay());
                } else if ((criteria == 4 && currentItem.getMealSequence() == 2 && currentItem.getAssociatedMealType() == 5)) {
                    return new WeekViewItem(currentItem.getId(),
                            currentItem.getDate(),
                            currentItem.getMeasurement(),
                            currentItem.getMealSequence(),
                            currentItem.getAssociatedMealType(),
                            currentItem.getAssociatedMeal(),
                            currentItem.getIsFirstMeasurementOfDay());
                }
            } else {
                if (i == inputArray.size()-1) {
                    //End of the loop, nothing found, insert empty data and break loop
                    return new WeekViewItem(" - ");
                }
            }
        }
        return new WeekViewItem(" - ");
    }

}
