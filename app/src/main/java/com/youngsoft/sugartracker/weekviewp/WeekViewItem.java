package com.youngsoft.sugartracker.weekviewp;

public class WeekViewItem {

    private int id;
    private long date;
    private double measurement;
    private int mealSequence;
    //1 = before, 2 = after
    private int associatedMealType;
    //if no specific associated meal picked then assign one of the default meal types
    //"Breakfast" = 1
    //"Brunch" = 2
    //"Lunch"; = 3
    //"Dinner" = 4
    //"Supper" = 5
    //"Snack" = 6
    //"Other" = 7
    //default = -1;
    private int associatedMeal;
    //-1 = no associated meal
    private boolean isFirstMeasurementOfDay;
    private String comment;

    public WeekViewItem(int id, long date, double measurement, int mealSequence, int associatedMealType, int associatedMeal, boolean isFirstMeasurementOfDay) {
        this.id = id;
        this.date = date;
        this.measurement = measurement;
        this.mealSequence = mealSequence;
        this.associatedMealType = associatedMealType;
        this.associatedMeal = associatedMeal;
        this.isFirstMeasurementOfDay = isFirstMeasurementOfDay;
        this.comment = "";
    }

    public WeekViewItem(String comment) {
        this.comment = comment;
        this.id = -1;
        this.date = -1;
        this.measurement = -1;
        this.mealSequence = -1;
        this.associatedMealType = -1;
        this.associatedMeal = -1;
        this.isFirstMeasurementOfDay = false;
    }

    public WeekViewItem(String comment, boolean isFirstMeasurementOfDay, int associatedMealType, int mealSequence, long date) {
        this.comment = comment;
        this.id = -1;
        this.date = -1;
        this.measurement = -1;
        this.mealSequence = mealSequence;
        this.associatedMealType = associatedMealType;
        this.associatedMeal = -1;
        this.isFirstMeasurementOfDay = isFirstMeasurementOfDay;
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

    public void setDate(long date) {
        this.date = date;
    }

    public double getMeasurement() {
        return measurement;
    }

    public void setMeasurement(double measurement) {
        this.measurement = measurement;
    }

    public int getMealSequence() {
        return mealSequence;
    }

    public void setMealSequence(int mealSequence) {
        this.mealSequence = mealSequence;
    }

    public int getAssociatedMealType() {
        return associatedMealType;
    }

    public void setAssociatedMealType(int associatedMealType) {
        this.associatedMealType = associatedMealType;
    }

    public int getAssociatedMeal() {
        return associatedMeal;
    }

    public void setAssociatedMeal(int associatedMeal) {
        this.associatedMeal = associatedMeal;
    }

    public boolean isFirstMeasurementOfDay() {
        return isFirstMeasurementOfDay;
    }

    public void setFirstMeasurementOfDay(boolean firstMeasurementOfDay) {
        isFirstMeasurementOfDay = firstMeasurementOfDay;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
