package com.youngsoft.sugartracker.preferencesp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.youngsoft.sugartracker.R;

public class FragmentPreferencesNumberPicker extends DialogFragment {

    View view;
    NumberPicker numberPickerHundreds;
    NumberPicker numberPickerTens;
    NumberPicker numberPickerOnes;
    NumberPicker numberPickerDecimals;
    double inputValue;
    int inputHundreds;
    int inputTens;
    int inputOnes;
    int inputDecimals;
    int inputKey;

    private OnSaveListener onSaveListener;

    public FragmentPreferencesNumberPicker(OnSaveListener onSaveListener) {
        this.onSaveListener = onSaveListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentFragment().getActivity());

        inputValue = getArguments().getDouble("inputValue",100);
        inputHundreds = (int) inputValue/100;
        inputTens = (int) (inputValue-inputHundreds*100)/10;
        inputOnes = (int) (inputValue-inputHundreds*100-inputTens*10)/1;
        inputDecimals = ((int) (inputValue*10))-inputHundreds*1000-inputTens*100-inputOnes*10;

        inputKey = getArguments().getInt("inputKey",1); // 1 = before, 2 = after


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

        numberPickerHundreds.setValue(inputHundreds);
        numberPickerTens.setValue(inputTens);
        numberPickerOnes.setValue(inputOnes);
        numberPickerDecimals.setValue(inputDecimals);

        return view;
    }

    @Override
    public void onDestroyView() {
        view = null;
        super.onDestroyView();
    }

    private void saveData() {
        double outputValue = (double) numberPickerHundreds.getValue()*100+
                (double) numberPickerTens.getValue()*10+
                (double) numberPickerOnes.getValue()+
                (double) numberPickerDecimals.getValue()/10;

        onSaveListener.onSave(outputValue, inputKey);

        dismiss();
    }

    public interface OnSaveListener {
        void onSave(double outputValue, int inputKey);
    }

}
