package com.youngsoft.sugartracker.dashboardp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.youngsoft.sugartracker.data.DataRepository;
import com.youngsoft.sugartracker.data.SugarMeasurement;

import java.util.Calendar;
import java.util.List;

public class ViewModelDashboard extends AndroidViewModel {

    private DataRepository dataRepository;
    private Calendar calendarStart;
    private Calendar calendarEnd;

    private LiveData<List<SugarMeasurement>> sugarMeasurementTodayLiveData;
    private LiveData<List<SugarMeasurement>> allSugarMeasurementsSortedByDate;
    private LiveData<List<SugarMeasurement>> allSugarMeasurementsSortedByDateInc;

    public ViewModelDashboard(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);
        calendarStart = Calendar.getInstance();
        calendarStart.set(Calendar.HOUR_OF_DAY,0);
        calendarStart.set(Calendar.MINUTE,0);
        calendarStart.set(Calendar.SECOND,0);
        calendarStart.set(Calendar.MILLISECOND,1);

        calendarEnd = Calendar.getInstance();
        calendarEnd.set(Calendar.HOUR_OF_DAY,24);
        calendarEnd.set(Calendar.MINUTE,59);
        calendarEnd.set(Calendar.SECOND,59);
        calendarEnd.set(Calendar.MILLISECOND,0);

        setSugarMeasurementsToday(calendarStart,calendarEnd);
        allSugarMeasurementsSortedByDate = dataRepository.getAllSugarMeasurementsSortedByDate();
        allSugarMeasurementsSortedByDateInc = dataRepository.getAllSugarMeasurementsSortedByDateInc();
    }

    private void setSugarMeasurementsToday(Calendar calendarStart, Calendar calendarEnd) {
        sugarMeasurementTodayLiveData = dataRepository.getSugarMeasurementsBetweenDates(calendarStart.getTimeInMillis(),calendarEnd.getTimeInMillis());
    }

    public LiveData<List<SugarMeasurement>> getSugarMeasurementTodayLiveData() {
        return sugarMeasurementTodayLiveData;
    }

    public LiveData<List<SugarMeasurement>> getAllSugarMeasurementsSortedByDate() {
        return allSugarMeasurementsSortedByDate;
    }

    public LiveData<List<SugarMeasurement>> getAllSugarMeasurementsSortedByDateInc() {
        return allSugarMeasurementsSortedByDateInc;
    }
}
