package com.youngsoft.sugartracker.data;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "MealRecord_Table")
public class MealRecord {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private long date;

    private String description;

    private int type;
    //1 = breakfast, 2 = brunch, 3 = lunch, 4 = dinner, 5 = supper, 6 = snack, 7 = other

    public MealRecord(long date, String description, int type) {
        this.date = date;
        this.description = description;
        this.type = type;
    }

    public static MealRecord[] populateMealRecordData() {
        long tempDate = Calendar.getInstance().getTimeInMillis();
        return new MealRecord[]{
                new MealRecord(tempDate, "chicken", 1),
                new MealRecord(tempDate + 1000 * 60 * 60, "chicken2", 2),
                new MealRecord(tempDate + 2000 * 60 * 60, "chicken3", 3),
                new MealRecord(tempDate + 3000 * 60 * 60, "chicken4", 4),
                new MealRecord(tempDate + 4000 * 60 * 60, "chicken5", 5),
                new MealRecord(tempDate + 5000 * 60 * 60, "chicken6", 6),
                new MealRecord(tempDate + 6000 * 60 * 60, "chicken7", 7)
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

    public String getDescription() {
        return description;
    }

    public int getType() {
        return type;
    }
}
