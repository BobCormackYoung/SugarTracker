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
        final MealRecord[] output = new MealRecord[1];

        new AsyncTask<Integer, Void, MealRecord>() {
            @Override
            protected MealRecord doInBackground(Integer... integers) {
                return dataDao.getMealRecordById(integers[0]);
            }

            @Override
            protected void onPostExecute(MealRecord mealRecord) {
                output[0] = mealRecord;
            }
        }.execute();
        return output[0];
    }

    public LiveData<List<SugarMeasurement>> getAllSugarMeasurementsSortedByDate() {
        return allSugarMeasurementsSortedByDate;
    }

    public LiveData<List<MealRecord>> getAllMealRecordsSortedByDate() {
        return allMealRecordsSortedByDate;
    }
}
