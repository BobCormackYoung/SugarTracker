package com.youngsoft.sugartracker.data;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DataRepository {

    private final String TAG = "DataRepository";
    private DataDao dataDao;
    private LiveData<List<SugarMeasurement>> allSugarMeasurementsSortedByDate;
    private LiveData<List<MealRecord>> allMealRecordsSortedByDate;

    public DataRepository(Application application) {
        DataDatabase dataDatabase = DataDatabase.getInstance(application);
        dataDao = dataDatabase.dataDao();
        allSugarMeasurementsSortedByDate = dataDao.getAllSugarMeasurementsSortByDate();
        allMealRecordsSortedByDate = dataDao.getAllMealRecordsSortByDate();
    }

    public void addDebugData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                dataDao.insertMultipleMealRecords(MealRecord.populateMealRecordData());
                dataDao.insertMultipleSugarMeasurementRecords(SugarMeasurement.populateSugarMeasurementData());
                Log.i(TAG, "Adding Debug Data");
                return null;
            }
        }.execute();
    }

    public void addSingleSugarMeasurement(final SugarMeasurement input) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                dataDao.insertSingleSugarMeasurementRecord(input);
                return null;
            }
        }.execute();
    }

    public MealRecord getMealRecordById(int index){
        return dataDao.getMealRecordById(index);
    }

    public LiveData<List<SugarMeasurement>> getAllSugarMeasurementsSortedByDate() {
        return allSugarMeasurementsSortedByDate;
    }

    public LiveData<List<MealRecord>> getAllMealRecordsSortedByDate() {
        return allMealRecordsSortedByDate;
    }

    public void addSingleMealRecord(final MealRecord mealRecord) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                dataDao.insertSingleMealRecord(mealRecord);
                return null;
            }
        }.execute();
    }

    public List<MealRecord> getMealList() {
        return dataDao.getAllMealRecordsNonLiveData();
    }

    public int getMealCount() {
        return dataDao.getMealCount();
    }
}
