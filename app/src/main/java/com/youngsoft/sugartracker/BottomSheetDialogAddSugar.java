package com.youngsoft.sugartracker;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class BottomSheetDialogAddSugar extends BottomSheetDialogFragment {

    ViewModelAddSugarMeasurement viewModelAddSugarMeasurement;

    LinearLayout llSugarBottomSheet;
    EditText etSugarDate;
    EditText etSugarTime;
    EditText etSugarAssociatedMeal;
    EditText etSugarValue;
    CheckBox cbFirstMeal;
    Button btSugarCancel;
    Button btSugarSave;
    RadioGroup rgMealTiming;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_sugarmeaurement, container, false);

        mapViews(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModelAddSugarMeasurement = ViewModelProviders.of(this).get(ViewModelAddSugarMeasurement.class);

        initNewData();
        setOnClickListeners();
        setObservers();

    }

    private void mapViews(View view) {
        llSugarBottomSheet = view.findViewById(R.id.ll_sugar_bottom_sheet);
        etSugarDate = view.findViewById(R.id.et_date_sugar);
        etSugarTime = view.findViewById(R.id.et_time_sugar);
        etSugarAssociatedMeal = view.findViewById(R.id.et_associated_meal);
        cbFirstMeal = view.findViewById(R.id.cb_first_measurement);
        btSugarCancel = view.findViewById(R.id.bt_cancel);
        btSugarSave = view.findViewById(R.id.bt_save);
        etSugarValue = view.findViewById(R.id.et_sugar_measurement);
        rgMealTiming= view.findViewById(R.id.rg_meal_timing);
    }

    private void initNewData() {

        // Set Date and Time to now
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.set(Calendar.HOUR, 1);
        calendarDate.set(Calendar.MINUTE, 0);
        calendarDate.set(Calendar.SECOND, 0);
        calendarDate.set(Calendar.MILLISECOND, 0);

        viewModelAddSugarMeasurement.setDateMutableLiveData(calendarDate.getTimeInMillis());

        Calendar calendarTime = Calendar.getInstance();
        calendarTime.set(Calendar.YEAR, 1970);
        calendarTime.set(Calendar.MONTH, 0);
        calendarTime.set(Calendar.DAY_OF_MONTH, 1);

        Log.i("BSF","Date" + calendarDate.getTimeInMillis());
        Log.i("BSF","Time" + calendarTime.getTimeInMillis());

        viewModelAddSugarMeasurement.setTimeMutableLiveData(calendarTime.getTimeInMillis());

        viewModelAddSugarMeasurement.setIsFirstMeasurementMutableLiveData(false);

        viewModelAddSugarMeasurement.setAssociatedMealMutableLiveData(-1);

        viewModelAddSugarMeasurement.setMealTimingMutableLiveData(0);

    }

    private void setOnClickListeners() {

        etSugarDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment datePickerFragment = new FragmentDatePickerSugar();
                    datePickerFragment.show(getChildFragmentManager(), "datePickerFragment");
                }
                //TODO: 2. Save picked date in the viewmodel
            }
        });

        etSugarDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragment = new FragmentDatePickerSugar();
                datePickerFragment.show(getChildFragmentManager(), "datePickerFragment");
                //TODO: 2. Save picked date in the viewmodel
            }
        });

        etSugarTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment timePickerFragment = new FragmentTimePickerSugar();
                    timePickerFragment.show(getChildFragmentManager(), "timePickerFragment");
                }
                //TODO: 2. Save picked time in the viewModel
            }
        });
        etSugarTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePickerFragment = new FragmentTimePickerSugar();
                timePickerFragment.show(getChildFragmentManager(), "timePickerFragment");
                //TODO: 2. Save picked time in the viewModel
            }
        });

        etSugarValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("BSDAS","onTextChanged = " + etSugarValue.getText().toString());
                viewModelAddSugarMeasurement.setSugarMutableLiveData(Double.valueOf(etSugarValue.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {
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

        // Listen for change to the checkbox, update viewModel if changed
        cbFirstMeal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewModelAddSugarMeasurement.setIsFirstMeasurementMutableLiveData(isChecked);
            }
        });

        // Listen for changes to the RadioGroup
        rgMealTiming.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_no_timing) {
                    viewModelAddSugarMeasurement.setMealTimingMutableLiveData(0);
                } else if (checkedId == R.id.rb_before_meal) {
                    viewModelAddSugarMeasurement.setMealTimingMutableLiveData(1);
                } else if (checkedId == R.id.rb_after_meal) {
                    viewModelAddSugarMeasurement.setMealTimingMutableLiveData(2);
                }
            }
        });

        btSugarSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModelAddSugarMeasurement.saveData();
                dismiss();
            }
        });
        btSugarCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: clear data from viewModel
                dismiss();
            }
        });

    }

    private void setObservers() {

        // Setting observer for the date object & updating the UI when changed
        viewModelAddSugarMeasurement.getDateMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                etSugarDate.setText(DateFormat.format("yyyy-MM-dd", aLong).toString());
                Log.i("BSF","Date" + aLong);
            }
        });

        // Setting observer for the time & updating the UI when changed
        viewModelAddSugarMeasurement.getTimeMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                etSugarTime.setText(DateFormat.format("HH:mm", aLong).toString());
                Log.i("BSF","Time" + aLong);
            }
        });

        // Setting observer for the sugar measurement & updating the UI when changed
        viewModelAddSugarMeasurement.getSugarMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                //Check if null. If "yes" then clear text view. If "no" then display the value
                if (aDouble != null) {
                    Log.i("BSDAS","aDouble = " + aDouble);
                    //Check if value in livedata is the same as in the textview
                    //if no, then update the view. if yes, then do nothing
                    if (aDouble == Double.valueOf(etSugarValue.getText().toString())) /*{
                        Log.i("BSDAS","aDouble not equal to text");
                        etSugarValue.setText(Double.toString(aDouble));
                    } else {
                        Log.i("BSDAS","aDouble equal to text");
                    }*/
                    etSugarValue.setText(Double.toString(aDouble));
                } else {
                    etSugarValue.getText().clear();
                }

            }
        });

        // Setting observer for whether is first meal measurement of the day & updating the UI when changed
        viewModelAddSugarMeasurement.getIsFirstMeasurementMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                cbFirstMeal.setChecked(aBoolean);
            }
        });

        // Setting observer for the associated meal, calling the ROOM to get the data & updating the UI when changed
        viewModelAddSugarMeasurement.getAssociatedMealMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == -1) {
                    etSugarAssociatedMeal.setText("- NONE -");
                } else {
                    //TODO: create call to ROOM for the respective
                }
            }
        });

        // Setting observer for the meal timing & updating the UI when changed
        viewModelAddSugarMeasurement.getMealTimingMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 0) {
                    rgMealTiming.check(R.id.rb_no_timing);
                } else if (integer == 1) {
                    rgMealTiming.check(R.id.rb_before_meal);
                } else if (integer == 2) {
                    rgMealTiming.check(R.id.rb_after_meal);
                }

            }
        });

    }

}
