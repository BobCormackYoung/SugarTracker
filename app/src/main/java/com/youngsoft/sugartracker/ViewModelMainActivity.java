package com.youngsoft.sugartracker;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.youngsoft.sugartracker.data.DataRepository;
import com.youngsoft.sugartracker.data.MealRecord;
import com.youngsoft.sugartracker.data.SugarMeasurement;

import java.util.List;

public class ViewModelMainActivity extends AndroidViewModel {

    private DataRepository dataRepository;
    private LiveData<List<SugarMeasurement>> allSugarMeasurementsSortedByDate;
    private LiveData<List<SugarMeasurement>> allSugarMeasurementsSortedByDateInc;
    private LiveData<List<MealRecord>> allMealRecordsSortedByDate;
    private LiveData<List<SugarMeasurement>> sugarMeasurementsBetweenDates;

    public ViewModelMainActivity(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);
        allSugarMeasurementsSortedByDate = dataRepository.getAllSugarMeasurementsSortedByDate();
        allSugarMeasurementsSortedByDateInc = dataRepository.getAllSugarMeasurementsSortedByDateInc();
        allMealRecordsSortedByDate = dataRepository.getAllMealRecordsSortedByDate();
    }

    public void addDebugData() {
        dataRepository.addDebugData();
    }

    public LiveData<List<SugarMeasurement>> getAllSugarMeasurementsSortedByDate() {
        return allSugarMeasurementsSortedByDate;
    }

    public LiveData<List<SugarMeasurement>> getAllSugarMeasurementsSortedByDateInc() {
        return allSugarMeasurementsSortedByDateInc;
    }

    public LiveData<List<MealRecord>> getAllMealRecordsSortedByDate() {
        return allMealRecordsSortedByDate;
    }

    public DataRepository getDataRepository() {
        return dataRepository;
    }

    public void deleteSugarMeasurement(int index) {
        //Log.i("VMMainActivity","deleteSugarMeasurement " + index);
        dataRepository.deleteSugarMeasurement(index);
    }

    public void deleteMealRecord(int index) {
        //Log.i("VMMainActivity","deleteMealMeasurement " + index);
        dataRepository.deleteMealRecord(index);
    }

    public LiveData<List<SugarMeasurement>> getSugarMeasurementsBetweenDates() {
        return sugarMeasurementsBetweenDates;
    }

    public void setSugarMeasurementsBetweenDates(long startDate, long endDate) {
        Log.i("VMMA", "Start Date: " + UtilMethods.convertDate(startDate, "yyyy-MM-dd  HH:mm:ss") + ", End Date: " +
                UtilMethods.convertDate(endDate, "yyyy-MM-dd  HH:mm:ss"));
        sugarMeasurementsBetweenDates = dataRepository.getSugarMeasurementsBetweenDates(startDate, endDate);
    }
}
