package com.youngsoft.sugartracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.youngsoft.sugartracker.data.MealRecord;

import java.util.Calendar;

public class BottomSheetDialogAddMeal extends BottomSheetDialogFragment {

    ViewModelAddMealRecord viewModelAddMealRecord;

    LinearLayout llmealBottomSheet;
    EditText etMealDate;
    EditText etMealTime;
    EditText etMealDetails;
    EditText etMealType;
    Button btMealCancel;
    Button btMealSave;

    int mealRecordId;

    long date;
    long time;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_mealrecord, container, false);

        //Get the entryId - required if editing an existing entry
        // if a there is no value passed, default value is -1, triggering data fields for a new entry
        mealRecordId = getArguments().getInt("EntryId",-1);

        mapViews(view);
        
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModelAddMealRecord = ViewModelProviders.of(this).get(ViewModelAddMealRecord.class);

        if (mealRecordId == -1) {
            //new data entry, initialise the data as a clean entry
            initNewData();
        } else {
            //existing data entry, initialise the data as an existing entry
            initNewData(); // first init clean entry values... to wait for asyn-task to finish on calling existing data
            //TODO: avoid initialising clean data first
            initExistingData();
        }
        
        setOnClickListeners();
        setObservers();

    }

    private void initExistingData() {
        GetMealRecordEntry getSugarMeasurementEntry = new GetMealRecordEntry();
        getSugarMeasurementEntry.execute(mealRecordId);
    }

    private void setObservers() {

        // Setting observer for the date object & updating the UI when changed
        viewModelAddMealRecord.getDateMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                etMealDate.setText(DateFormat.format("yyyy-MM-dd", aLong).toString());
                date = aLong;
                Log.i("BSF","Date" + aLong);
            }
        });

        // Setting observer for the time & updating the UI when changed
        viewModelAddMealRecord.getTimeMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                etMealTime.setText(DateFormat.format("HH:mm", aLong).toString());
                time = aLong;
                Log.i("BSF","Time" + aLong);
            }
        });

        // Setting observer for the sugar measurement & updating the UI when changed
        viewModelAddMealRecord.getMealMutableLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String aString) {
                //Check if null. If "yes" then clear text view. If "no" then display the value
                if (!aString.isEmpty()) {
                    //Check if livedata string is the same as already in the view
                    //if no, then update. if yes, then do nothing
                    if (!aString.equals(etMealDetails.getText().toString())) {
                        etMealDetails.setText(aString);
                    }
                } else {
                    etMealDetails.getText().clear();
                }

            }
        });

        // Setting observer for the associated meal, calling the ROOM to get the data & updating the UI when changed
        viewModelAddMealRecord.getMealTypeMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                etMealType.setText(UtilMethods.getMealType(integer));
            }
        });

    }

    private void setOnClickListeners() {

        etMealDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment datePickerFragment = new FragmentDatePickerMeal();
                    Bundle inputs = new Bundle();
                    inputs.putLong("inputDate",date);
                    datePickerFragment.setArguments(inputs);
                    datePickerFragment.show(getChildFragmentManager(), "datePickerFragment");
                }
            }
        });

        etMealDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragment = new FragmentDatePickerMeal();
                Bundle inputs = new Bundle();
                inputs.putLong("inputDate",date);
                datePickerFragment.setArguments(inputs);
                datePickerFragment.show(getChildFragmentManager(), "datePickerFragment");
            }
        });

        etMealTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment timePickerFragment = new FragmentTimePickerMeal();
                    Bundle inputs = new Bundle();
                    inputs.putLong("inputTime",time);
                    timePickerFragment.setArguments(inputs);
                    timePickerFragment.show(getChildFragmentManager(), "timePickerFragment");
                }
            }
        });
        etMealTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePickerFragment = new FragmentTimePickerMeal();
                Bundle inputs = new Bundle();
                inputs.putLong("inputTime",time);
                timePickerFragment.setArguments(inputs);
                timePickerFragment.show(getChildFragmentManager(), "timePickerFragment");
            }
        });

        etMealDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModelAddMealRecord.setMealMutableLiveData(etMealDetails.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etMealType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment mealTypePickerFragment = new FragmentMealTypePicker();
                    mealTypePickerFragment.show(getChildFragmentManager(), "mealTypePickerFragment");
                }
            }
        });

        etMealType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment mealTypePickerFragment = new FragmentMealTypePicker();
                mealTypePickerFragment.show(getChildFragmentManager(), "mealTypePickerFragment");
            }
        });

        btMealSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mealRecordId == -1) {
                    //new entry, save as a new row in the database
                    viewModelAddMealRecord.saveData();
                } else {
                    //existing entry, update the row in the database with new data
                    viewModelAddMealRecord.updateData();
                }
                dismiss();
            }
        });

        btMealCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private void initNewData() {

        // Set Date and Time to now
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.set(Calendar.HOUR, 1);
        calendarDate.set(Calendar.MINUTE, 0);
        calendarDate.set(Calendar.SECOND, 0);
        calendarDate.set(Calendar.MILLISECOND, 0);

        viewModelAddMealRecord.setDateMutableLiveData(calendarDate.getTimeInMillis());

        Calendar calendarTime = Calendar.getInstance();
        calendarTime.set(Calendar.YEAR, 1970);
        calendarTime.set(Calendar.MONTH, 0);
        calendarTime.set(Calendar.DAY_OF_MONTH, 1);

        Log.i("BSF","Date" + calendarDate.getTimeInMillis());
        Log.i("BSF","Time" + calendarTime.getTimeInMillis());

        viewModelAddMealRecord.setTimeMutableLiveData(calendarTime.getTimeInMillis());

        //Init the meal type as "other" and allow to be changed later
        viewModelAddMealRecord.setMealTypeMutableLiveData(7);

    }

    private void mapViews(View view) {
        etMealDate = view.findViewById(R.id.et_date_meal);
        etMealTime = view.findViewById(R.id.et_time_meal);
        etMealDetails = view.findViewById(R.id.et_meal_details);
        etMealType = view.findViewById(R.id.et_meal_type);
        btMealSave = view.findViewById(R.id.bt_save);
        btMealCancel = view.findViewById(R.id.bt_cancel);
    }

    private class GetMealRecordEntry extends AsyncTask<Integer, Void, Void> {

        int index;
        MealRecord inputMealRecord;

        @Override
        protected Void doInBackground(Integer... inputIntegers) {
            index = inputIntegers[0];
            inputMealRecord = viewModelAddMealRecord.getDataRepository().getMealRecordById(index);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //TODO: move this calendar modification work to utils
            Calendar calendarDate = Calendar.getInstance();
            calendarDate.setTimeInMillis(inputMealRecord.getDate());
            calendarDate.set(Calendar.HOUR, 1);
            calendarDate.set(Calendar.MINUTE, 0);
            calendarDate.set(Calendar.SECOND, 0);
            calendarDate.set(Calendar.MILLISECOND, 0);

            Calendar calendarTime = Calendar.getInstance();
            calendarTime.setTimeInMillis(inputMealRecord.getDate());
            calendarTime.set(Calendar.YEAR, 1970);
            calendarTime.set(Calendar.MONTH, 0);
            calendarTime.set(Calendar.DAY_OF_MONTH, 1);

            viewModelAddMealRecord.setIndexMutableLiveData(index);
            viewModelAddMealRecord.setDateMutableLiveData(calendarDate.getTimeInMillis());
            viewModelAddMealRecord.setTimeMutableLiveData(calendarTime.getTimeInMillis());
            viewModelAddMealRecord.setMealMutableLiveData(inputMealRecord.getDescription());
            viewModelAddMealRecord.setMealTypeMutableLiveData(inputMealRecord.getType());
        }
    }
}
