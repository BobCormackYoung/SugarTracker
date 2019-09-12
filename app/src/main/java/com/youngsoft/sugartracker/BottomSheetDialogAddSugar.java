package com.youngsoft.sugartracker;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class BottomSheetDialogAddSugar extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_sugarmeaurement, container, false);

        LinearLayout llSugarBottomSheet = view.findViewById(R.id.ll_sugar_bottom_sheet);
        EditText etSugarDate = view.findViewById(R.id.et_date_sugar);
        EditText etSugarTime = view.findViewById(R.id.et_time_sugar);
        EditText etSugarAssociatedMeal = view.findViewById(R.id.et_associated_meal);
        Button btSugarCancel = view.findViewById(R.id.bt_cancel);
        Button btSugarSave = view.findViewById(R.id.bt_save);

        Calendar calendarDate = Calendar.getInstance();
        calendarDate.set(Calendar.HOUR, 0);
        calendarDate.set(Calendar.MINUTE, 0);
        calendarDate.set(Calendar.SECOND, 0);
        calendarDate.set(Calendar.MILLISECOND, 0);

        etSugarDate.setText(DateFormat.format("yyyy-MM-dd", calendarDate.getTimeInMillis()).toString());

        Calendar calendarTime = Calendar.getInstance();
        calendarTime.set(Calendar.YEAR, 0);
        calendarTime.set(Calendar.MONTH, 0);
        calendarTime.set(Calendar.DAY_OF_MONTH, 0);

        etSugarTime.setText(DateFormat.format("HH:mm", calendarTime.getTimeInMillis()).toString());

        etSugarDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment datePickerFragment = new FragmentDatePicker();
                    datePickerFragment.show(getChildFragmentManager(), "datePickerFragment");
                }
                //TODO: 2. Save picked date in the viewmodel
            }
        });
        etSugarDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragment = new FragmentDatePicker();
                datePickerFragment.show(getChildFragmentManager(), "datePickerFragment");
                //TODO: 2. Save picked date in the viewmodel
            }
        });

        etSugarTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment timePickerFragment = new FragmentTimePicker();
                    timePickerFragment.show(getChildFragmentManager(), "timePickerFragment");
                }
                //TODO: 2. Save picked time in the viewModel
            }
        });
        etSugarTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePickerFragment = new FragmentTimePicker();
                timePickerFragment.show(getChildFragmentManager(), "timePickerFragment");
                //TODO: 2. Save picked time in the viewModel
            }
        });

        etSugarAssociatedMeal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment mealPickerFragment = new FragmentMealPicker();
                    mealPickerFragment.show(getChildFragmentManager(), "mealPickerFragment");
                }
                //TODO: 1. Create custom dialog
                //TODO: 2. Launch custom dialog to pick an associated meal
                //TODO: 3. Save data to viewModel
            }
        });
        etSugarAssociatedMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment mealPickerFragment = new FragmentMealPicker();
                mealPickerFragment.show(getChildFragmentManager(), "mealPickerFragment");
                //TODO: 1. Create custom dialog
                //TODO: 2. Launch custom dialog to pick an associated meal
                //TODO: 3. Save data to viewModel
            }
        });

        btSugarSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: clear data in viewModel (if going to be used/reused?)
                dismiss();
            }
        });
        btSugarCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: save data from viewModel to database
                dismiss();
            }
        });

        return view;
    }
}
