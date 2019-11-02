package com.youngsoft.sugartracker.weekviewp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.youngsoft.sugartracker.data.DataRepository;

public class ViewModelWeekView extends AndroidViewModel {

    private DataRepository dataRepository;

    public ViewModelWeekView(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);
    }
}
