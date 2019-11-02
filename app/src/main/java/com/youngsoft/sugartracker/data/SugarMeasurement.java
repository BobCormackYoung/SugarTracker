package com.youngsoft.sugartracker.data;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "SugarMeasurement_Table")
public class SugarMeasurement {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private long date;

    private double measurement;

    private int mealSequence;
    //1 = before, 2 = after

    private int associatedMealType;
    //if no specific associated meal picked then assign one of the default meal types
    //"Breakfast" = 1
    //"Brunch" = 2
    //"Lunch" = 3
    //"Dinner" = 4
    //"Supper" = 5
    //"Snack" = 6
    //"Other" = 7
    //default = -1;

    //TODO: Change to foreignKey relationship
    private int associatedMeal;
    //-1 = no associated meal

    private boolean isFirstMeasurementOfDay;

    public SugarMeasurement(long date, double measurement, int mealSequence, int associatedMeal, int associatedMealType, boolean isFirstMeasurementOfDay) {
        this.date = date;
        this.measurement = measurement;
        this.mealSequence = mealSequence;
        this.associatedMeal = associatedMeal;
        this.associatedMealType = associatedMealType;
        this.isFirstMeasurementOfDay = isFirstMeasurementOfDay;
    }

    public static SugarMeasurement[] populateSugarMeasurementData() {
        long tempDate = Calendar.getInstance().getTimeInMillis();
        return new SugarMeasurement[]{
                new SugarMeasurement(tempDate, 100, 1, 1, -1, true),
                new SugarMeasurement(tempDate + 1000 * 60, 105, 1, -1, -1, true),
                new SugarMeasurement(tempDate + 1000 * 60 * 60, 105, 2, 1, -1, false)
        };

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public double getMeasurement() {
        return measurement;
    }

    public int getMealSequence() {
        return mealSequence;
    }

    public int getAssociatedMealType() {
        return associatedMealType;
    }

    public int getAssociatedMeal() {
        return associatedMeal;
    }

    public boolean getIsFirstMeasurementOfDay() {
        return isFirstMeasurementOfDay;
    }

}
