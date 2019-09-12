package com.youngsoft.sugartracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FragmentDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new DatePickerDialog(getParentFragment().getActivity());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //TODO: Do something with the date picked
    }
}
