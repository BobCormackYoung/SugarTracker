package com.youngsoft.sugartracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.Calendar;

public class FragmentDatePickerSugar extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    ViewModelAddSugarMeasurement viewModelAddSugarMeasurement;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        viewModelAddSugarMeasurement = ViewModelProviders.of(getParentFragment()).get(ViewModelAddSugarMeasurement.class);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day= c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getParentFragment().getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.HOUR_OF_DAY, 1);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Log.i("DatePicker","Date = " + c.getTimeInMillis());
        Log.i("DatePicker","" + year);
        Log.i("DatePicker","" + month);
        Log.i("DatePicker","" + dayOfMonth);

        viewModelAddSugarMeasurement.setDateMutableLiveData(c.getTimeInMillis());

        Calendar b = Calendar.getInstance();
        b.setTimeInMillis(0);
        Log.i("DatePicker","" + b.get(Calendar.YEAR));
        Log.i("DatePicker","" + b.get(Calendar.MONTH));
        Log.i("DatePicker","" + b.get(Calendar.DAY_OF_MONTH));
        Log.i("DatePicker","" + b.get(Calendar.HOUR));
        Log.i("DatePicker","" + b.get(Calendar.MINUTE));
        Log.i("DatePicker","" + b.get(Calendar.SECOND));
        Log.i("DatePicker","" + b.get(Calendar.MILLISECOND));

    }
}
