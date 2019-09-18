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
import com.youngsoft.sugartracker.data.MealRecord;

import java.util.List;

public class FragmentMeals extends Fragment {

    View view;
    FloatingActionButton floatingActionButton;
    BottomSheetDialogAddMeal bottomSheet;
    RecyclerView recyclerView;
    AdapterMealList adapterMealList;
    ViewModelMainActivity viewModelMainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_meals, container, false);

        floatingActionButton = view.findViewById(R.id.fab_add_meal);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet = new BottomSheetDialogAddMeal();
                bottomSheet.show(getChildFragmentManager(), "mealBottomSheet");
            }
        });

        recyclerView = view.findViewById(R.id.rv_meals_list);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModelMainActivity = ViewModelProviders.of(getActivity()).get(ViewModelMainActivity.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        adapterMealList = new AdapterMealList(viewModelMainActivity.getDataRepository(), this, viewModelMainActivity);
        recyclerView.setAdapter(adapterMealList);
        viewModelMainActivity.getAllMealRecordsSortedByDate().observe(getViewLifecycleOwner(), new Observer<List<MealRecord>>() {
            @Override
            public void onChanged(List<MealRecord> mealRecords) {
                adapterMealList.submitList(mealRecords);
            }
        });

    }
}
