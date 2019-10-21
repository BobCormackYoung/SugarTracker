package com.youngsoft.sugartracker.sugarlistp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.youngsoft.sugartracker.R;
import com.youngsoft.sugartracker.UtilMethods;
import com.youngsoft.sugartracker.data.MealRecord;
import com.youngsoft.sugartracker.data.SugarMeasurement;

import java.util.Calendar;

public class BottomSheetDialogAddSugar extends BottomSheetDialogFragment {

    ViewModelAddSugarMeasurement viewModelAddSugarMeasurement;

    LinearLayout llSugarBottomSheet;
    EditText etSugarDate;
    EditText etSugarTime;
    EditText etSugarAssociatedMeal;
    EditText etSugarValue;
    EditText etSugarAssociatedMealType;
    TextInputLayout tilSugarAssociatedMeal;
    TextInputLayout tilSugarAssociatedMealType;
    CheckBox cbFirstMeal;
    Button btSugarCancel;
    Button btSugarSave;
    RadioGroup rgMealTiming;

    int sugarMeasurementEntryId;

    long date;
    long time;

    Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_sugarmeaurement, container, false);
        mapViews(view);
        mContext = this.getContext();

        //Get the entryId - required if editing an existing entry
        // if a there is no value passed, default value is -1, triggering data fields for a new entry
        sugarMeasurementEntryId = getArguments().getInt("EntryId",-1);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModelAddSugarMeasurement = ViewModelProviders.of(this).get(ViewModelAddSugarMeasurement.class);

        if (sugarMeasurementEntryId == -1) {
            //new data entry, initialise the data as a clean entry
            initNewData();
            setOnClickListeners();
            setObservers();
        } else {
            //existing data entry, initialise the data as an existing entry
            initNewData(); // first init clean entry values... to wait for asyn-task to finish on calling existing data
            setOnClickListeners();
            setObservers();
            //TODO: avoid initialising clean data first
            initExistingData();
        }


    }

    private void initExistingData() {
        GetSugarMeasurementEntry getSugarMeasurementEntry = new GetSugarMeasurementEntry();
        getSugarMeasurementEntry.execute(sugarMeasurementEntryId);
    }

    private void mapViews(View view) {
        llSugarBottomSheet = view.findViewById(R.id.ll_sugar_bottom_sheet);
        etSugarDate = view.findViewById(R.id.et_date_sugar);
        etSugarTime = view.findViewById(R.id.et_time_sugar);
        etSugarAssociatedMeal = view.findViewById(R.id.et_associated_meal);
        etSugarAssociatedMealType = view.findViewById(R.id.et_associated_meal_type);
        cbFirstMeal = view.findViewById(R.id.cb_first_measurement);
        btSugarCancel = view.findViewById(R.id.bt_cancel);
        btSugarSave = view.findViewById(R.id.bt_save);
        etSugarValue = view.findViewById(R.id.et_sugar_measurement);
        rgMealTiming= view.findViewById(R.id.rg_meal_timing);
        tilSugarAssociatedMeal = view.findViewById(R.id.til_associated_meal);
        tilSugarAssociatedMealType = view.findViewById(R.id.til_associated_meal_type);
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

        //Log.i("BSF","Date" + calendarDate.getTimeInMillis());
        //Log.i("BSF","Time" + calendarTime.getTimeInMillis());

        viewModelAddSugarMeasurement.setSugarMutableLiveData(0);

        viewModelAddSugarMeasurement.setTimeMutableLiveData(calendarTime.getTimeInMillis());

        viewModelAddSugarMeasurement.setIsFirstMeasurementMutableLiveData(false);

        viewModelAddSugarMeasurement.setAssociatedMealTypeMutableLiveData(-1);

        viewModelAddSugarMeasurement.setAssociatedMealMutableLiveData(-1);

        viewModelAddSugarMeasurement.setMealTimingMutableLiveData(0);

    }

    private void setOnClickListeners() {

        etSugarDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment datePickerFragment = new FragmentDatePickerSugar();
                    Bundle inputs = new Bundle();
                    inputs.putLong("inputDate",date);
                    datePickerFragment.setArguments(inputs);
                    datePickerFragment.show(getChildFragmentManager(), "datePickerFragment");
                }
            }
        });

        etSugarDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragment = new FragmentDatePickerSugar();
                Bundle inputs = new Bundle();
                inputs.putLong("inputDate",date);
                datePickerFragment.setArguments(inputs);
                datePickerFragment.show(getChildFragmentManager(), "datePickerFragment");
            }
        });

        etSugarTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment timePickerFragment = new FragmentTimePickerSugar();
                    Bundle inputs = new Bundle();
                    inputs.putLong("inputTime",time);
                    timePickerFragment.setArguments(inputs);
                    timePickerFragment.show(getChildFragmentManager(), "timePickerFragment");
                }
            }
        });
        etSugarTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePickerFragment = new FragmentTimePickerSugar();
                Bundle inputs = new Bundle();
                inputs.putLong("inputTime",time);
                timePickerFragment.setArguments(inputs);
                timePickerFragment.show(getChildFragmentManager(), "timePickerFragment");
            }
        });

        etSugarValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment numberPickerFragment = new FragmentNumberPicker();
                numberPickerFragment.show(getChildFragmentManager(), "numberPickerFragment");
            }
        });

        etSugarValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment numberPickerFragment = new FragmentNumberPicker();
                    numberPickerFragment.show(getChildFragmentManager(), "numberPickerFragment");
                }
            }
        });

        etSugarAssociatedMeal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    GetMealCountAsync getMealCountAsync= new GetMealCountAsync();
                    getMealCountAsync.execute();
                }
            }
        });

        etSugarAssociatedMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMealCountAsync getMealCountAsync= new GetMealCountAsync();
                getMealCountAsync.execute();
            }
        });

        etSugarAssociatedMealType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //If no specific associated meal selected, allow picking meal type
                    if (viewModelAddSugarMeasurement.getCurrentAssociatedMeal() == -1) {
                        DialogFragment mealTypePickerFragment = new FragmentMealTypePickerSugar();
                        mealTypePickerFragment.show(getChildFragmentManager(), "mealTypePickerFragment");
                    }
                }
            }
        });

        etSugarAssociatedMealType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If no specific associated meal selected, allow picking meal type
                if (viewModelAddSugarMeasurement.getCurrentAssociatedMeal() == -1) {
                    DialogFragment mealTypePickerFragment = new FragmentMealTypePickerSugar();
                    mealTypePickerFragment.show(getChildFragmentManager(), "mealTypePickerFragment");
                }
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
                if (sugarMeasurementEntryId == -1) {
                    //new entry, save as a new row in the database
                    viewModelAddSugarMeasurement.saveData();
                } else {
                    //existing entry, update the row in the database with new data
                    viewModelAddSugarMeasurement.updateData();
                }

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
                date = aLong;
                //Log.i("BSF","Date" + aLong);
            }
        });

        // Setting observer for the time & updating the UI when changed
        viewModelAddSugarMeasurement.getTimeMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                etSugarTime.setText(DateFormat.format("HH:mm", aLong).toString());
                time = aLong;
                //Log.i("BSF","Time" + aLong);
            }
        });

        // Setting observer for the sugar measurement & updating the UI when changed
        viewModelAddSugarMeasurement.getSugarMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                //Check if null. If "yes" then clear text view. If "no" then display the value
                if (aDouble != null) {
                    //Log.i("BSDAS","aDouble = " + aDouble);
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

        // Setting observer for the associated meal type & updating the UI when changed
        viewModelAddSugarMeasurement.getAssociatedMealTypeMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer index) {
                if (index == -1) {
                    etSugarAssociatedMealType.setText("None");
                } else {
                    etSugarAssociatedMealType.setText(UtilMethods.getMealType(index));
                }
            }
        });

        // Setting observer for the associated meal, calling the ROOM to get the data & updating the UI when changed
        viewModelAddSugarMeasurement.getAssociatedMealMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer index) {
                if (index == -1) {
                    viewModelAddSugarMeasurement.setAssociatedMealTypeMutableLiveData(-1);
                    etSugarAssociatedMeal.setText("None");
                } else {
                    GetMealDataAsync getMealDataAsync = new GetMealDataAsync();
                    getMealDataAsync.execute(index);
                }
            }
        });

        // Setting observer for the meal timing & updating the UI when changed
        viewModelAddSugarMeasurement.getMealTimingMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 0) {
                    rgMealTiming.check(R.id.rb_no_timing);
                    //If changing to no associated meal, hide views for picking a meal & meal type
                    tilSugarAssociatedMeal.setVisibility(View.GONE);
                    tilSugarAssociatedMealType.setVisibility(View.GONE);
                    //If changing to no associated meal, delete data related to meal & meal type
                    viewModelAddSugarMeasurement.setAssociatedMealMutableLiveData(-1);
                    viewModelAddSugarMeasurement.setAssociatedMealTypeMutableLiveData(-1);
                } else if (integer == 1) {
                    rgMealTiming.check(R.id.rb_before_meal);
                    //If there is an associated meal, then show allow picking options
                    tilSugarAssociatedMeal.setVisibility(View.VISIBLE);
                    tilSugarAssociatedMealType.setVisibility(View.VISIBLE);
                } else if (integer == 2) {
                    rgMealTiming.check(R.id.rb_after_meal);
                    //If there is an associated meal, then show allow picking options
                    tilSugarAssociatedMeal.setVisibility(View.VISIBLE);
                    tilSugarAssociatedMealType.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    private class GetMealDataAsync extends AsyncTask<Integer, Void, Void> {

        int index;
        String outputDate;
        String outputMealType;
        MealRecord outputMealRecord;

        @Override
        protected Void doInBackground(Integer... inputIntegers) {

            index = inputIntegers[0];
            outputMealRecord = viewModelAddSugarMeasurement.getDataRepository().getMealRecordById(index);
            outputDate = DateFormat.format("yyyy-MM-dd HH:mm", outputMealRecord.getDate()).toString();
            //Log.i("BSDAS","type " + outputMealRecord.getType());
            outputMealType = UtilMethods.getMealType(outputMealRecord.getType());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Set the text views to match the stored date
            etSugarAssociatedMealType.setText("" + outputMealType);
            etSugarAssociatedMeal.setText("" + outputDate);
        }
    }

    private class GetMealCountAsync extends AsyncTask<Void, Void, Void> {
        int mealCount;

        @Override
        protected Void doInBackground(Void... aVoid) {
            mealCount=viewModelAddSugarMeasurement.getDataRepository().getMealCount();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mealCount != 0) {
                DialogFragment mealPickerFragment = new FragmentMealPicker();
                mealPickerFragment.show(getChildFragmentManager(), "mealPickerFragment");
            } else {
                Toast.makeText(mContext, "No meals in database!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetSugarMeasurementEntry extends AsyncTask<Integer, Void, Void> {

        int index;
        SugarMeasurement inputSugarMeasurement;

        @Override
        protected Void doInBackground(Integer... inputIntegers) {
            index = inputIntegers[0];
            inputSugarMeasurement = viewModelAddSugarMeasurement.getDataRepository().getSugarMeasurementById(index);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //TODO: move this calendar modification work to utils
            Calendar calendarDate = Calendar.getInstance();
            calendarDate.setTimeInMillis(inputSugarMeasurement.getDate());
            calendarDate.set(Calendar.HOUR, 1);
            calendarDate.set(Calendar.MINUTE, 0);
            calendarDate.set(Calendar.SECOND, 0);
            calendarDate.set(Calendar.MILLISECOND, 0);

            Calendar calendarTime = Calendar.getInstance();
            calendarTime.setTimeInMillis(inputSugarMeasurement.getDate());
            calendarTime.set(Calendar.YEAR, 1970);
            calendarTime.set(Calendar.MONTH, 0);
            calendarTime.set(Calendar.DAY_OF_MONTH, 1);

            viewModelAddSugarMeasurement.setIndexMutableLiveData(index);
            viewModelAddSugarMeasurement.setDateMutableLiveData(calendarDate.getTimeInMillis());
            viewModelAddSugarMeasurement.setSugarMutableLiveData(inputSugarMeasurement.getMeasurement());
            viewModelAddSugarMeasurement.setTimeMutableLiveData(calendarTime.getTimeInMillis());
            viewModelAddSugarMeasurement.setIsFirstMeasurementMutableLiveData(inputSugarMeasurement.getIsFirstMeasurementOfDay());
            viewModelAddSugarMeasurement.setAssociatedMealMutableLiveData(inputSugarMeasurement.getAssociatedMeal());
            viewModelAddSugarMeasurement.setAssociatedMealTypeMutableLiveData(inputSugarMeasurement.getAssociatedMealType());
            viewModelAddSugarMeasurement.setMealTimingMutableLiveData(inputSugarMeasurement.getMealSequence());
        }
    }
}
