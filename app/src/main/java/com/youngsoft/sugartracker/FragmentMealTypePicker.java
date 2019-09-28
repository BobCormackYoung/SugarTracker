package com.youngsoft.sugartracker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FragmentMealTypePicker extends DialogFragment implements AdapterMealTypePickerList.OnMealTypeClickListener{

    ViewModelAddMealRecord viewModelAddMealRecord;
    View view;
    RecyclerView recyclerView;
    ArrayList<Integer> mealTypeArrayList;
    AdapterMealTypePickerList adapterMealTypePickerList;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getParentFragment().getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        view = inflater.inflate(R.layout.fragment_meal_type_picker_dialog, null);
        builder.setView(view)
                .setNegativeButton("Negative", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //LoginDialogFragment.this.getDialog().cancel();
                    }
                });
        recyclerView = view.findViewById(R.id.rv_meal_type_picker);
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModelAddMealRecord = ViewModelProviders.of(getParentFragment()).get(ViewModelAddMealRecord.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        adapterMealTypePickerList = new AdapterMealTypePickerList(this);
        recyclerView.setAdapter(adapterMealTypePickerList);

        mealTypeArrayList = new ArrayList<>();
        mealTypeArrayList.add(1);
        mealTypeArrayList.add(2);
        mealTypeArrayList.add(3);
        mealTypeArrayList.add(4);
        mealTypeArrayList.add(5);
        mealTypeArrayList.add(6);
        mealTypeArrayList.add(7);

        adapterMealTypePickerList.submitList(mealTypeArrayList);

    }

    @Override
    public void onDestroyView() {
        view = null;
        super.onDestroyView();
    }

    @Override
    public void onMealTypeClick(int index) {
        Log.i("OnMealClickListener", "Clicked index " + index);
        viewModelAddMealRecord.setMealTypeMutableLiveData(index);
        dismiss();
    }
}
