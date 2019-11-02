package com.youngsoft.sugartracker.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DataDao {

    @Insert
    void insertMultipleSugarMeasurementRecords(SugarMeasurement... sugarMeasurements);

    @Insert
    void insertMultipleMealRecords(MealRecord... mealRecords);

    @Insert
    void insertSingleSugarMeasurementRecord(SugarMeasurement... sugarMeasurement);

    @Insert
    void insertSingleMealRecord(MealRecord... mealRecord);

    @Query("SELECT * FROM MealRecord_Table ORDER BY id DESC")
    LiveData<List<MealRecord>> getAllMealRecordsSortById();

    @Query("SELECT * FROM MealRecord_Table ORDER BY date DESC")
    LiveData<List<MealRecord>> getAllMealRecordsSortByDate();

    @Query("SELECT * FROM SugarMeasurement_Table ORDER BY date DESC")
    LiveData<List<SugarMeasurement>> getAllSugarMeasurementsSortByDate();

    @Query("SELECT * FROM MealRecord_Table WHERE id=:index")
    MealRecord getMealRecordById(int index);

    @Query("SELECT * FROM MealRecord_Table ORDER BY date DESC")
    List<MealRecord> getAllMealRecordsNonLiveData();

    @Query("SELECT COUNT(id) FROM MealRecord_Table")
    int getMealCount();

    @Query("SELECT * FROM SugarMeasurement_Table WHERE mealSequence=1 AND associatedMeal=:index")
    List<SugarMeasurement> getBeforeMealSugarMeasurement(int index);

    @Query("SELECT * FROM SugarMeasurement_Table WHERE mealSequence=2 AND associatedMeal=:index")
    List<SugarMeasurement> getAfterMealSugarMeasurement(int index);

    @Query("DELETE FROM SugarMeasurement_Table WHERE id = :index")
    int deleteSugarMeasurement(int index);

    @Query("SELECT * FROM SugarMeasurement_Table WHERE associatedMeal=:index")
    List<SugarMeasurement> getAssociatedSugarMeasurements(int index);

    @Query("DELETE FROM MealRecord_Table WHERE id = :index")
    int deleteMealRecord(int index);

    @Query("SELECT * FROM SugarMeasurement_Table ORDER BY date")
    LiveData<List<SugarMeasurement>> getAllSugarMeasurementsSortByDateInc();

    @Query("SELECT * FROM SugarMeasurement_Table WHERE id=:index")
    SugarMeasurement getSugarMeasurementById(int index);

    @Update
    void updateSugarMeasurementEntry(SugarMeasurement... outputSugarMeasurements);

    @Update
    void updateMealRecordEntry(MealRecord... outputMealRecords);

    @Query("SELECT * FROM SugarMeasurement_Table WHERE date BETWEEN :startDate AND :endDate ORDER BY date")
    LiveData<List<SugarMeasurement>> getSugarMeasurementsBetweenDates(long startDate, long endDate);
}
