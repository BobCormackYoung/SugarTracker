package com.youngsoft.sugartracker.data;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.youngsoft.sugartracker.R;
import com.youngsoft.sugartracker.ViewModelAddSugarMeasurement;

public class FragmentNumberPicker extends DialogFragment {

    ViewModelAddSugarMeasurement viewModelAddSugarMeasurement;
    View view;
    NumberPicker numberPickerHundreds;
    NumberPicker numberPickerTens;
    NumberPicker numberPickerOnes;
    NumberPicker numberPickerDecimals;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentFragment().getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        view = inflater.inflate(R.layout.fragment_number_picker_dialog, null);
        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //dismiss();
                        //onDestroy();
                    }
                })
        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveData();
            }
        });
        numberPickerHundreds = view.findViewById(R.id.np_hundreds_picker);
        numberPickerTens = view.findViewById(R.id.np_tens_picker);
        numberPickerOnes = view.findViewById(R.id.np_ones_picker);
        numberPickerDecimals = view.findViewById(R.id.np_decimals_picker);
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        numberPickerHundreds.setMinValue(0);
        numberPickerHundreds.setMaxValue(9);
        numberPickerTens.setMinValue(0);
        numberPickerTens.setMaxValue(9);
        numberPickerOnes.setMinValue(0);
        numberPickerOnes.setMaxValue(9);
        numberPickerDecimals.setMinValue(0);
        numberPickerDecimals.setMaxValue(9);

        //numberPickerHundreds.setValue(0);
        //numberPickerTens.setValue(0);
        //numberPickerOnes.setValue(0);
        //numberPickerDecimals.setValue(0);

        viewModelAddSugarMeasurement = ViewModelProviders.of(getParentFragment()).get(ViewModelAddSugarMeasurement.class);

        viewModelAddSugarMeasurement.getSugarMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                int hundreds = Integer.valueOf(Double.toString(aDouble/100));
                int tens = Integer.valueOf(Double.toString((aDouble-hundreds*100)/10));
                int ones = Integer.valueOf(Double.toString((aDouble-hundreds*100-tens*10)/1));
                int decimals = Integer.valueOf(Double.toString((aDouble-hundreds*100-tens*10-ones)*10));

                numberPickerHundreds.setValue(hundreds);
                numberPickerTens.setValue(tens);
                numberPickerOnes.setValue(ones);
                numberPickerDecimals.setValue(decimals);

                Log.i("FragmentNumberPicker","Observer results: " + hundreds + " " + tens + " " + ones + " " + decimals);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        view = null;
        super.onDestroyView();
    }

    private void saveData() {
        double outputValue = numberPickerHundreds.getValue()*100+
                numberPickerTens.getValue()*10+
                numberPickerOnes.getValue()+
                numberPickerDecimals.getValue()/10;
        Log.i("FragmentNumberPicker","" + outputValue);
        viewModelAddSugarMeasurement.setSugarMutableLiveData(outputValue);
        dismiss();
    }


}
