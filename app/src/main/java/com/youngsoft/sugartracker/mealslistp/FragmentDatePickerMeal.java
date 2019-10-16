package com.youngsoft.sugartracker.mealslistp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.Calendar;

public class FragmentDatePickerMeal extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    ViewModelAddMealRecord viewModelAddMealRecord;

    //TODO: read date data from the viewModel & set that as the initialised value instead of it being passed as a bundle
    long inputDate;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        viewModelAddMealRecord = ViewModelProviders.of(getParentFragment()).get(ViewModelAddMealRecord.class);

        inputDate = getArguments().getLong("inputDate");

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
        //Log.i("DatePicker","Date = " + c.getTimeInMillis());
        //Log.i("DatePicker","" + year);
        //Log.i("DatePicker","" + month);
        //Log.i("DatePicker","" + dayOfMonth);

        viewModelAddMealRecord.setDateMutableLiveData(c.getTimeInMillis());

        Calendar b = Calendar.getInstance();
        b.setTimeInMillis(0);
        //Log.i("DatePicker","" + b.get(Calendar.YEAR));
        //Log.i("DatePicker","" + b.get(Calendar.MONTH));
        //Log.i("DatePicker","" + b.get(Calendar.DAY_OF_MONTH));
        //Log.i("DatePicker","" + b.get(Calendar.HOUR));
        //Log.i("DatePicker","" + b.get(Calendar.MINUTE));
        //Log.i("DatePicker","" + b.get(Calendar.SECOND));
        //Log.i("DatePicker","" + b.get(Calendar.MILLISECOND));

    }
}
