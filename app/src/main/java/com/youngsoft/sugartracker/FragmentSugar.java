package com.youngsoft.sugartracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.youngsoft.sugartracker.data.SugarMeasurement;

import java.util.List;

public class FragmentSugar extends Fragment {

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

        adapterSugarList = new AdapterSugarList(viewModelMainActivity.getDataRepository(), this, viewModelMainActivity);
        recyclerView.setAdapter(adapterSugarList);
        viewModelMainActivity.getAllSugarMeasurementsSortedByDate().observe(getViewLifecycleOwner(), new Observer<List<SugarMeasurement>>() {
            @Override
            public void onChanged(List<SugarMeasurement> sugarMeasurements) {
                    adapterSugarList.submitList(sugarMeasurements);
            }
        });


    }
}
