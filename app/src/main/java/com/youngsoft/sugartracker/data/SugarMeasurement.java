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

    public SugarMeasurement(long date, double measurement, int mealSequence, int associatedMeal, int associatedMealType) {
        this.date = date;
        this.measurement = measurement;
        this.mealSequence = mealSequence;
        this.associatedMeal = associatedMeal;
        this.associatedMealType = associatedMealType;
    }

    //Method for bulk inserting debug data as required
    public static SugarMeasurement[] populateSugarMeasurementData() {
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(Calendar.HOUR_OF_DAY,0);
        tempCalendar.set(Calendar.MINUTE,0);
        tempCalendar.set(Calendar.SECOND,0);
        tempCalendar.set(Calendar.MILLISECOND,1);
        long tempDate = tempCalendar.getTimeInMillis();
        return new SugarMeasurement[]{
                new SugarMeasurement(tempDate-(172*1000*60*60*24)+(8*60*60*1000)  , 91, 1, -1, 1),
                new SugarMeasurement(tempDate-(171*1000*60*60*24)+(8*60*60*1000)  , 94, 1, -1, 1)
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

}
