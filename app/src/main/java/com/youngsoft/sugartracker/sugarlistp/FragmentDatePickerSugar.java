package com.youngsoft.sugartracker.sugarlistp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.Calendar;

public class FragmentDatePickerSugar extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    ViewModelAddSugarMeasurement viewModelAddSugarMeasurement;

    //TODO: read date data from the viewModel & set that as the initialised value instead of it being passed as a bundle
    long inputDate;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        inputDate = getArguments().getLong("inputDate");

        viewModelAddSugarMeasurement = ViewModelProviders.of(getParentFragment()).get(ViewModelAddSugarMeasurement.class);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(inputDate);
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

        viewModelAddSugarMeasurement.setDateMutableLiveData(c.getTimeInMillis());

        Calendar b = Calendar.getInstance();
        b.setTimeInMillis(0);

    }
}
