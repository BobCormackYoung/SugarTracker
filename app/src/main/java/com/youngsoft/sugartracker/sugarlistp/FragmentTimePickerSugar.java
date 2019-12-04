package com.youngsoft.sugartracker.sugarlistp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.Calendar;

public class FragmentTimePickerSugar extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    ViewModelAddSugarMeasurement viewModelAddSugarMeasurement;

    //TODO: read time data from the viewModel & set that as the initialised value instead of it being passed as a bundle
    long inputTime;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        viewModelAddSugarMeasurement = ViewModelProviders.of(getParentFragment()).get(ViewModelAddSugarMeasurement.class);

        inputTime = getArguments().getLong("inputTime");

        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(inputTime);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getParentFragment().getActivity(), this, hour, minute,true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Calendar c = Calendar.getInstance();
        c.set(1970, 0, 1, hourOfDay, minute, 0);

        viewModelAddSugarMeasurement.setTimeMutableLiveData(c.getTimeInMillis());
    }
}
