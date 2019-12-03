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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.youngsoft.sugartracker.R;
import com.youngsoft.sugartracker.UtilMethods;
import com.youngsoft.sugartracker.ViewModelMainActivity;
import com.youngsoft.sugartracker.data.SugarMeasurement;
import com.youngsoft.sugartracker.sugarlistp.BottomSheetDialogAddSugar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class FragmentWeekView extends Fragment implements AdapterWeekViewItem.OnItemClickListener {

    //private View view;
    private RecyclerView recyclerView;
    private AdapterWeekView adapterWeekView;
    private ViewModelMainActivity viewModelMainActivity;
    private long newestDate; //newest date with a sugar measurement recorded
    private long oldestDate; //oldest date with a sugar measurement recorded
    private long weekCount; //number of weeks stored in the database
    private ArrayList<WeekDatesItem> weekDatesArrayList = new ArrayList<>();
    FloatingActionButton floatingActionButton;
    BottomSheetDialogAddSugar bottomSheet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week_view, container, false);

        floatingActionButton = view.findViewById(R.id.fab_add_sugar_weekly);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet = new BottomSheetDialogAddSugar();
                Bundle inputs = new Bundle();
                inputs.putInt("EntryId",-1);
                bottomSheet.setArguments(inputs);
                bottomSheet.show(getChildFragmentManager(), "weekBottomSheet");
            }
        });

        recyclerView = view.findViewById(R.id.rv_week_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModelMainActivity = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(ViewModelMainActivity.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        adapterWeekView = new AdapterWeekView(this, viewModelMainActivity.getDataRepository(), this);
        recyclerView.setAdapter(adapterWeekView);

        viewModelMainActivity.getAllSugarMeasurementsSortedByDate().observe(getViewLifecycleOwner(), new Observer<List<SugarMeasurement>>() {
            @Override
            public void onChanged(List<SugarMeasurement> sugarMeasurements) {
                Log.i("FWV","observer changed");
                //TODO: handle if the observed item is null
                Calendar tempCalendar = Calendar.getInstance();
                tempCalendar.set(Calendar.HOUR_OF_DAY,0);
                tempCalendar.set(Calendar.MINUTE,0);
                tempCalendar.set(Calendar.SECOND,0);
                newestDate = tempCalendar.getTimeInMillis(); //get the newest date from the existing data
                oldestDate = sugarMeasurements.get(sugarMeasurements.size()-1).getDate(); //get the oldest date from the existing data
                weekCount = 1+(newestDate-oldestDate)/(1000*60*60*24*7);
                weekDatesArrayList.clear();

                tempCalendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
                long startDateOfFirstWeek = tempCalendar.getTimeInMillis();
                for (long item = 0; item < weekCount+1; item++) {
                    Log.i("FWV","item " + item + ", date " + UtilMethods.convertDate(startDateOfFirstWeek-item*1000*60*60*24*7,"dd-MM-yyyy HH:mm:ss"));
                    //Log.i("FWV","startDateOfFirstWeek " + startDateOfFirstWeek + ", item " + item*1000*60*60*24*7);
                    tempCalendar.setTimeInMillis(startDateOfFirstWeek-item*1000*60*60*24*7);
                    weekDatesArrayList.add(new WeekDatesItem(tempCalendar.getTimeInMillis(),tempCalendar.getTimeInMillis()+1000*60*60*24*7));
                }
                adapterWeekView.submitList(weekDatesArrayList);
                adapterWeekView.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(WeekViewItem weekViewItem) {
        if(weekViewItem.getId() == -1) {
            //TODO: if a comment value, then need the date and meal type, then pre-populate the bottomSheet
            bottomSheet = new BottomSheetDialogAddSugar();
            Bundle inputs = new Bundle();
            inputs.putInt("MealType",weekViewItem.getAssociatedMealType());
            inputs.putInt("MealSequence",weekViewItem.getMealSequence());
            inputs.putBoolean("FirstMeal", weekViewItem.isFirstMeasurementOfDay());
            inputs.putLong("Date", weekViewItem.getDate());
            Log.i("FWV","onClick: " + UtilMethods.convertDate(weekViewItem.getDate(),"dd-MM-yyyy HH-mm-ss"));
            bottomSheet.setArguments(inputs);
            bottomSheet.show(getChildFragmentManager(), "sugarBottomSheet");
        } else {
            //if an existing measurement, load the data and show in the bottomSheet
            bottomSheet = new BottomSheetDialogAddSugar();
            Bundle inputs = new Bundle();
            inputs.putInt("EntryId",weekViewItem.getId());
            bottomSheet.setArguments(inputs);
            bottomSheet.show(getChildFragmentManager(), "sugarBottomSheet");
        }
    }
}
