package com.youngsoft.sugartracker.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DataRepository {

    private final String TAG = "DataRepository";
    private DataDao dataDao;
    private LiveData<List<SugarMeasurement>> allSugarMeasurementsSortedByDate;
    private LiveData<List<SugarMeasurement>> allSugarMeasurementsSortedByDateInc;
    private LiveData<List<MealRecord>> allMealRecordsSortedByDate;

    public DataRepository(Application application) {
        DataDatabase dataDatabase = DataDatabase.getInstance(application);
        dataDao = dataDatabase.dataDao();
        allSugarMeasurementsSortedByDate = dataDao.getAllSugarMeasurementsSortByDate();
        allSugarMeasurementsSortedByDateInc = dataDao.getAllSugarMeasurementsSortByDateInc();
        allMealRecordsSortedByDate = dataDao.getAllMealRecordsSortByDate();
    }

    public void addDebugData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                dataDao.insertMultipleMealRecords(MealRecord.populateMealRecordData());
                dataDao.insertMultipleSugarMeasurementRecords(SugarMeasurement.populateSugarMeasurementData());
                //Log.i(TAG, "Adding Debug Data");
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

    public List<SugarMeasurement> getBeforeMealSugarMeasurement(int index) {
        return dataDao.getBeforeMealSugarMeasurement(index);
    }

    public List<SugarMeasurement> getAfterMealSugarMeasurement(int index) {
        return dataDao.getAfterMealSugarMeasurement(index);
    }

    public void deleteSugarMeasurement(final int index) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                //Log.i("DataRepository","deleteSugarMeasurement " + index);
                int affectRows = dataDao.deleteSugarMeasurement(index);
                //Log.i("DataRepository","deleteSugarMeasurement affects rows " + affectRows);
                return null;
            }
        }.execute();
    }

    public List<SugarMeasurement> getAssociatedSugarMeasurements(int index) {
        return dataDao.getAssociatedSugarMeasurements(index);
    }

    public void deleteMealRecord(final int index) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                //Log.i("DataRepository","deleteMealMeasurement " + index);
                int affectRows = dataDao.deleteMealRecord(index);
                //Log.i("DataRepository","deleteMealMeasurement affects rows " + affectRows);
                return null;
            }
        }.execute();
    }

    public LiveData<List<SugarMeasurement>> getAllSugarMeasurementsSortedByDateInc() {
        return allSugarMeasurementsSortedByDateInc;
    }

    public SugarMeasurement getSugarMeasurementById(int index) {
        return dataDao.getSugarMeasurementById(index);
    }

    public void updateSugarMeasurementEntry(final SugarMeasurement outputSugarMeasurement) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                //Log.i("DataRepository","UpdateSugarMeasurement " + outputSugarMeasurement.getId());
                dataDao.updateSugarMeasurementEntry(outputSugarMeasurement);
                return null;
            }
        }.execute();
    }

    public void updateMealRecord(final MealRecord outputMealRecord) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                //Log.i("DataRepository","UpdateMealRecord " + outputMealRecord.getId());
                dataDao.updateMealRecordEntry(outputMealRecord);
                return null;
            }
        }.execute();
    }
}
