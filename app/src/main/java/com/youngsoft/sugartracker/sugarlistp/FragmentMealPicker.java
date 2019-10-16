package com.youngsoft.sugartracker.sugarlistp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.youngsoft.sugartracker.R;
import com.youngsoft.sugartracker.data.MealRecord;

import java.util.List;

public class FragmentMealPicker extends DialogFragment implements AdapterMealPickerList.OnMealClickListener {

    ViewModelAddSugarMeasurement viewModelAddSugarMeasurement;
    View view;
    RecyclerView recyclerView;
    AdapterMealPickerList adapterMealPickerList;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getParentFragment().getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        view = inflater.inflate(R.layout.fragment_meal_picker_dialog, null);
        builder.setView(view)
                .setNegativeButton("Negative", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //dismiss();
                        //onDestroy();
                    }
                });
        recyclerView = view.findViewById(R.id.rv_meal_picker);
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModelAddSugarMeasurement = ViewModelProviders.of(getParentFragment()).get(ViewModelAddSugarMeasurement.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        adapterMealPickerList = new AdapterMealPickerList(viewModelAddSugarMeasurement.getDataRepository(), this, viewModelAddSugarMeasurement, this);
        recyclerView.setAdapter(adapterMealPickerList);

        viewModelAddSugarMeasurement.getMealRecordLiveData().observe(getViewLifecycleOwner(), new Observer<List<MealRecord>>() {
            @Override
            public void onChanged(List<MealRecord> mealRecords) {
                //If the 1st item in the arrayList is not "-1" i.e. no meal, the add "-1" at the top
                //If the 1st item in the arrayList is "-1", then leave the list as-is
                    if (mealRecords.get(0).getId() != -1) {
                        MealRecord tempMealRecord = new MealRecord(0,"None",7);
                        tempMealRecord.setId(-1);
                        mealRecords.add(0,tempMealRecord);
                        adapterMealPickerList.submitList(mealRecords);
                    } else {
                        adapterMealPickerList.submitList(mealRecords);
                    }

            }
        });
    }

    @Override
    public void onDestroyView() {
        view = null;
        super.onDestroyView();
    }

    @Override
    public void onMealClick(int index) {
        //Log.i("OnMealClickListener", "Clicked index " + index);
        viewModelAddSugarMeasurement.setAssociatedMealMutableLiveData(index);
        dismiss();
    }
}
