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

//TODO: move view logic to viewModel

public class ViewModelAddSugarMeasurement extends AndroidViewModel {

    private DataRepository dataRepository;

    private LiveData<List<MealRecord>> mealRecordLiveData;

    private MutableLiveData<Long> dateMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Long> timeMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Double> sugarMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> mealTimingMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> associatedMealMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> indexMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> associatedMealTypeMutableLiveData = new MutableLiveData<>();

    public ViewModelAddSugarMeasurement(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);
        setMealRecordLiveData();
    }



    //Getters
    public DataRepository getDataRepository() {
        return dataRepository;
    }

    public LiveData<Long> getDateMutableLiveData() {
        return dateMutableLiveData;
    }

    public LiveData<Long> getTimeMutableLiveData() {
        return timeMutableLiveData;
    }

    public LiveData<Double> getSugarMutableLiveData() {
        return sugarMutableLiveData;
    }

    public LiveData<Integer> getMealTimingMutableLiveData() {
        return mealTimingMutableLiveData;
    }

    public LiveData<Integer> getAssociatedMealMutableLiveData() {
        return associatedMealMutableLiveData;
    }

    public int getCurrentAssociatedMeal() {
        return associatedMealMutableLiveData.getValue();
    }

    public LiveData<Integer> getAssociatedMealTypeMutableLiveData() {
        return associatedMealTypeMutableLiveData;
    }

    public LiveData<List<MealRecord>> getMealRecordLiveData() {
        return mealRecordLiveData;
    }

    //Setters
    private void setMealRecordLiveData() {
        mealRecordLiveData = dataRepository.getAllMealRecordsSortedByDate();
    }

    public void setDateMutableLiveData(long date) {
        dateMutableLiveData.setValue(date);
    }

    public void setTimeMutableLiveData(long time) {
        timeMutableLiveData.setValue(time);
    }

    public void setIndexMutableLiveData(int index) {
        indexMutableLiveData.setValue(index);
    }

    public void setSugarMutableLiveData(double sugarValue) {
        sugarMutableLiveData.setValue(sugarValue);
    }

    public void setMealTimingMutableLiveData(int mealTiming) {
        mealTimingMutableLiveData.setValue(mealTiming);
    }

    public void setAssociatedMealMutableLiveData(int associatedMeal) {
        associatedMealMutableLiveData.setValue(associatedMeal);
    }

    public void setAssociatedMealTypeMutableLiveData(int associatedMealType) {
        associatedMealTypeMutableLiveData.setValue(associatedMealType);
    }

    //Save and Update
    public void saveData() {

        dataRepository.addSingleSugarMeasurement(new SugarMeasurement(
                dateMutableLiveData.getValue()+timeMutableLiveData.getValue(),
                sugarMutableLiveData.getValue(),
                mealTimingMutableLiveData.getValue(),
                associatedMealMutableLiveData.getValue(),
                associatedMealTypeMutableLiveData.getValue()));
    }

    public void updateData() {
        SugarMeasurement outputSugarMeasurement = new SugarMeasurement(
                dateMutableLiveData.getValue()+timeMutableLiveData.getValue(),
                sugarMutableLiveData.getValue(),
                mealTimingMutableLiveData.getValue(),
                associatedMealMutableLiveData.getValue(),
                associatedMealTypeMutableLiveData.getValue());
        outputSugarMeasurement.setId(indexMutableLiveData.getValue());
        dataRepository.updateSugarMeasurementEntry(outputSugarMeasurement);
    }
}
