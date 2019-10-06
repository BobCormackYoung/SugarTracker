package com.youngsoft.sugartracker;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.youngsoft.sugartracker.data.DataRepository;
import com.youngsoft.sugartracker.data.MealRecord;
import com.youngsoft.sugartracker.data.SugarMeasurement;

import java.util.List;

public class FragmentMeals extends Fragment implements AdapterMealList.OnDeleteClickListener {

    View view;
    FloatingActionButton floatingActionButton;
    BottomSheetDialogAddMeal bottomSheet;
    RecyclerView recyclerView;
    AdapterMealList adapterMealList;
    ViewModelMainActivity viewModelMainActivity;
    DataRepository dataRepository;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_meals, container, false);

        floatingActionButton = view.findViewById(R.id.fab_add_meal);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet = new BottomSheetDialogAddMeal();
                bottomSheet.show(getChildFragmentManager(), "mealBottomSheet");
            }
        });

        recyclerView = view.findViewById(R.id.rv_meals_list);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModelMainActivity = ViewModelProviders.of(getActivity()).get(ViewModelMainActivity.class);
        dataRepository = viewModelMainActivity.getDataRepository();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        adapterMealList = new AdapterMealList(viewModelMainActivity.getDataRepository(), this, viewModelMainActivity, this);
        recyclerView.setAdapter(adapterMealList);
        viewModelMainActivity.getAllMealRecordsSortedByDate().observe(getViewLifecycleOwner(), new Observer<List<MealRecord>>() {
            @Override
            public void onChanged(List<MealRecord> mealRecords) {
                adapterMealList.submitList(mealRecords);
            }
        });

    }

    @Override
    public void onDeleteClick(final int index) {
        Log.i("FragmentMeal","onDeleteClick " + index);
        DeleteMealRecordAsync deleteMealRecordAsync = new DeleteMealRecordAsync();
        deleteMealRecordAsync.execute(index);
    }

    private class DeleteMealRecordAsync extends AsyncTask<Integer, Void, Void>{

        int index;
        List<SugarMeasurement> associatedSugarRecords;

        @Override
        protected Void doInBackground(Integer... integers) {
            index = integers[0];
            //TODO: simplify to a simple method to get count instead of returning a list
            associatedSugarRecords = dataRepository.getAssociatedSugarMeasurements(index);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (associatedSugarRecords == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setTitle("Confirm deletion")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                viewModelMainActivity.deleteMealRecord(index);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Do nothing
                            }
                        });
                builder.create().show();
            } else if (associatedSugarRecords.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setTitle("Confirm deletion")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                viewModelMainActivity.deleteMealRecord(index);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Do nothing
                            }
                        });
                builder.create().show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setTitle("Meal Record Deletion")
                        .setMessage("This meal record is referenced by " +
                                associatedSugarRecords.size() +
                                " sugar measurements, and cannot be deleted.")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Do nothing
                            }
                        });
                builder.create().show();
            }
        }
    }
}
