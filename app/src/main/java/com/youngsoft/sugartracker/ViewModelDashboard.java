package com.youngsoft.sugartracker;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.youngsoft.sugartracker.data.DataRepository;

public class ViewModelDashboard extends AndroidViewModel {

    private DataRepository dataRepository;

    public ViewModelDashboard(@NonNull Application application) {
        super(application);
    }
}
