package com.youngsoft.sugartracker.sugarlistp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.youngsoft.sugartracker.data.DataRepository;
import com.youngsoft.sugartracker.data.MealRecord;
import com.youngsoft.sugartracker.data.SugarMeasurement;

import java.util.List;

public class ViewModelAddSugarMeasurement extends AndroidViewModel {

    private DataRepository dataRepository;

    private LiveData<List<MealRecord>> mealRecordLiveData;

    private MutableLiveData<Long> dateMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Long> timeMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Double> sugarMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isFirstMeasurementMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> mealTimingMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> associatedMealMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> indexMutableLiveData = new MutableLiveData<>();

    public ViewModelAddSugarMeasurement(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);
        setMealRecordLiveData();
    }

    private void setMealRecordLiveData() {
        mealRecordLiveData = dataRepository.getAllMealRecordsSortedByDate();
    }

    //Getters and Setters

    public DataRepository getDataRepository() {
        return dataRepository;
    }

    public LiveData<Long> getDateMutableLiveData() {
        return dateMutableLiveData;
    }

    public void setDateMutableLiveData(long date) {
        dateMutableLiveData.setValue(date);
    }

    public LiveData<Long> getTimeMutableLiveData() {
        return timeMutableLiveData;
    }

    public void setTimeMutableLiveData(long time) {
        timeMutableLiveData.setValue(time);
    }

    public LiveData<Double> getSugarMutableLiveData() {
        return sugarMutableLiveData;
    }

    public void setIndexMutableLiveData(int index) {
        indexMutableLiveData.setValue(index);
    }

    public void setSugarMutableLiveData(double sugarValue) {
        sugarMutableLiveData.setValue(sugarValue);
    }

    public LiveData<Boolean> getIsFirstMeasurementMutableLiveData() {
        return isFirstMeasurementMutableLiveData;
    }

    public void setIsFirstMeasurementMutableLiveData(boolean isFirstMeasurement) {
        isFirstMeasurementMutableLiveData.setValue(isFirstMeasurement);
    }

    public LiveData<Integer> getMealTimingMutableLiveData() {
        return mealTimingMutableLiveData;
    }

    public void setMealTimingMutableLiveData(int mealTiming) {
        mealTimingMutableLiveData.setValue(mealTiming);
    }

    public LiveData<Integer> getAssociatedMealMutableLiveData() {
        return associatedMealMutableLiveData;
    }

    public void setAssociatedMealMutableLiveData(int associatedMeal) {
        associatedMealMutableLiveData.setValue(associatedMeal);
    }

    public LiveData<List<MealRecord>> getMealRecordLiveData() {
        return mealRecordLiveData;
    }

    //Save and Update
    public void saveData() {

        //Log.i("VM","Date = " + DateFormat.format("yyyy-MM-dd HH:mm", dateMutableLiveData.getValue()));
        //Log.i("VM","Time = " + DateFormat.format("yyyy-MM-dd HH:mm", timeMutableLiveData.getValue()));
        //Log.i("VM","Time + Date = " + DateFormat.format("yyyy-MM-dd HH:mm", dateMutableLiveData.getValue() + timeMutableLiveData.getValue()));
        //Log.i("VM","Sugar = " + sugarMutableLiveData.getValue());
        //Log.i("VM","First Meal = " + isFirstMeasurementMutableLiveData.getValue());
        //Log.i("VM","Meal Timing = " + mealTimingMutableLiveData.getValue());
        //Log.i("VM","Associated Meal = " + associatedMealMutableLiveData.getValue());

        dataRepository.addSingleSugarMeasurement(new SugarMeasurement(
                dateMutableLiveData.getValue()+timeMutableLiveData.getValue(),
                sugarMutableLiveData.getValue(),
                mealTimingMutableLiveData.getValue(),
                associatedMealMutableLiveData.getValue(),
                isFirstMeasurementMutableLiveData.getValue()));
    }

    public void updateData() {
        SugarMeasurement outputSugarMeasurement = new SugarMeasurement(
                dateMutableLiveData.getValue()+timeMutableLiveData.getValue(),
                sugarMutableLiveData.getValue(),
                mealTimingMutableLiveData.getValue(),
                associatedMealMutableLiveData.getValue(),
                isFirstMeasurementMutableLiveData.getValue());
        outputSugarMeasurement.setId(indexMutableLiveData.getValue());
        dataRepository.updateSugarMeasurementEntry(outputSugarMeasurement);
    }
}
