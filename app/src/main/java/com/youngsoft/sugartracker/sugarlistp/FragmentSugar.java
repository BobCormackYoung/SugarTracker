package com.youngsoft.sugartracker.sugarlistp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.youngsoft.sugartracker.R;
import com.youngsoft.sugartracker.ViewModelMainActivity;
import com.youngsoft.sugartracker.data.SugarMeasurement;

import java.util.List;

public class FragmentSugar extends Fragment implements AdapterSugarList.OnDeleteClickListener, AdapterSugarList.OnEditClickListener{

    View view;
    FloatingActionButton floatingActionButton;
    BottomSheetDialogAddSugar bottomSheet;
    RecyclerView recyclerView;
    AdapterSugarList adapterSugarList;
    ViewModelMainActivity viewModelMainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sugar, container, false);

        floatingActionButton = view.findViewById(R.id.fab_add_sugar);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet = new BottomSheetDialogAddSugar();
                Bundle inputs = new Bundle();
                inputs.putInt("EntryId",-1);
                bottomSheet.setArguments(inputs);
                bottomSheet.show(getChildFragmentManager(), "sugarBottomSheet");
            }
        });

        recyclerView = view.findViewById(R.id.rv_sugar_list);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModelMainActivity = ViewModelProviders.of(getActivity()).get(ViewModelMainActivity.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        adapterSugarList = new AdapterSugarList(viewModelMainActivity.getDataRepository(), this, viewModelMainActivity, this, this);
        recyclerView.setAdapter(adapterSugarList);
        viewModelMainActivity.getAllSugarMeasurementsSortedByDate().observe(getViewLifecycleOwner(), new Observer<List<SugarMeasurement>>() {
            @Override
            public void onChanged(List<SugarMeasurement> sugarMeasurements) {
                    adapterSugarList.submitList(sugarMeasurements);
            }
        });
    }

    @Override
    public void onDeleteClick(final int index) {
        Log.i("FragmentSugar","onDeleteClick " + index);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_MaterialComponents_Light_Dialog_Alert)
                .setMessage("Are you sure you'd like to delete this record?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        viewModelMainActivity.deleteSugarMeasurement(index);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Do nothing
                    }
                });

        builder.create().show();
    }

    @Override
    public void onEditClick(int index) {
        Log.i("FragmentSugar","onEditClick " + index);
        bottomSheet = new BottomSheetDialogAddSugar();
        Bundle inputs = new Bundle();
        inputs.putInt("EntryId",index);
        bottomSheet.setArguments(inputs);
        bottomSheet.show(getChildFragmentManager(), "sugarBottomSheet");
    }
}
