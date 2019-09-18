package com.youngsoft.sugartracker.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

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

}
