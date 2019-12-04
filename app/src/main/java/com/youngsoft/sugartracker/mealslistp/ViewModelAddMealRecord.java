package com.youngsoft.sugartracker.mealslistp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.youngsoft.sugartracker.data.DataRepository;
import com.youngsoft.sugartracker.data.MealRecord;

public class ViewModelAddMealRecord extends AndroidViewModel {

    private DataRepository dataRepository;

    private MutableLiveData<Integer> indexMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Long> dateMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Long> timeMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> mealMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> mealTypeMutableLiveData = new MutableLiveData<>();

    public ViewModelAddMealRecord(@NonNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);
    }

    public DataRepository getDataRepository() {
        return dataRepository;
    }

    public void setDataRepository(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public LiveData<Long> getDateMutableLiveData() {
        return dateMutableLiveData;
    }

    public void setDateMutableLiveData(Long input) {
        dateMutableLiveData.setValue(input);
    }

    public LiveData<Long> getTimeMutableLiveData() {
        return timeMutableLiveData;
    }

    public void setTimeMutableLiveData(Long input) {
        timeMutableLiveData.setValue(input);
    }

    public LiveData<String> getMealMutableLiveData() {
        return mealMutableLiveData;
    }

    public void setMealMutableLiveData(String input) {
        mealMutableLiveData.setValue(input);
    }

    public LiveData<Integer> getMealTypeMutableLiveData() {
        return mealTypeMutableLiveData;
    }

    public void setMealTypeMutableLiveData(Integer input) {
        mealTypeMutableLiveData.setValue(input);
    }

    //Save and Update
    public void saveData() {
        dataRepository.addSingleMealRecord(new MealRecord(
                dateMutableLiveData.getValue()+timeMutableLiveData.getValue(),
                mealMutableLiveData.getValue(),
                mealTypeMutableLiveData.getValue()));
    }

    public void setIndexMutableLiveData(int index) {
        indexMutableLiveData.setValue(index);
    }

    public void updateData() {
        MealRecord outputMealRecord = new MealRecord(
                dateMutableLiveData.getValue()+timeMutableLiveData.getValue(),
                mealMutableLiveData.getValue(),
                mealTypeMutableLiveData.getValue());
        outputMealRecord.setId(indexMutableLiveData.getValue());
        dataRepository.updateMealRecord(outputMealRecord);
    }
}
