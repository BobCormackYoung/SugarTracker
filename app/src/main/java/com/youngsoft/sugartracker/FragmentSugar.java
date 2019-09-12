package com.youngsoft.sugartracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FragmentSugar extends Fragment {

    View view;
    FloatingActionButton floatingActionButton;
    BottomSheetDialogAddSugar bottomSheet;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sugar, container, false);

        bottomSheet = new BottomSheetDialogAddSugar();

        floatingActionButton = view.findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.show(getChildFragmentManager(), "sugarBottomSheet");
            }
        });

        return view;
    }
}
