package com.youngsoft.sugartracker.data;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

@Database(entities = {SugarMeasurement.class, MealRecord.class}, version = 1, exportSchema = false)
public abstract class DataDatabase extends RoomDatabase {

    private static DataDatabase instance;
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    //instance.dataDao().insertMultipleMealRecords(MealRecord.populateMealRecordData());
                    //instance.dataDao().insertMultipleSugarMeasurementRecords(SugarMeasurement.populateSugarMeasurementData());
                }
            });


        }
    };

    public static synchronized DataDatabase getInstance(Context context) {
        if (instance == null) {
            Log.i("DataDatabase", "In constructor, instance = null");
            //Populate database on first creation
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    DataDatabase.class, "dataDatabase")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    public abstract DataDao dataDao();

}
