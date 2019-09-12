package com.youngsoft.sugartracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class FragmentDashboard extends Fragment {

    View view;
    private ViewModelMainActivity viewModelMainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        viewModelMainActivity = ViewModelProviders.of(getActivity()).get(ViewModelMainActivity.class);

        view.findViewById(R.id.bt_debug_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModelMainActivity.addDebugData();
            }
        });

        return view;
    }
}
