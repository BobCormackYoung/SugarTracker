package com.youngsoft.sugartracker;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.Calendar;

public class FragmentTimePickerSugar extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    ViewModelAddSugarMeasurement viewModelAddSugarMeasurement;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        viewModelAddSugarMeasurement = ViewModelProviders.of(getParentFragment()).get(ViewModelAddSugarMeasurement.class);

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getParentFragment().getActivity(), this, hour, minute,
                true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Calendar c = Calendar.getInstance();
        c.set(1970, 0, 1, hourOfDay, minute, 0);
        Log.i("DatePicker", "Date = " + c.getTimeInMillis());
        Log.i("DatePicker", "" + hourOfDay);
        Log.i("DatePicker", "" + minute);

        viewModelAddSugarMeasurement.setTimeMutableLiveData(c.getTimeInMillis());
    }
}
