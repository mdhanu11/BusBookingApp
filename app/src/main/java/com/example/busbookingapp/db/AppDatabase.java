package com.example.busbookingapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.busbookingapp.db.dao.BusDao;
import com.example.busbookingapp.db.entities.Bus;
import com.example.busbookingapp.db.entities.SeatsAvailabiliy;

@Database(entities = {
        Bus.class, SeatsAvailabiliy.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract BusDao getBusDao();

    private static AppDatabase busDB;

    public static AppDatabase getInstance(Context context) {
        if (null == busDB) {
            busDB = buildDatabaseInstance(context);
        }
        return busDB;
    }

    private static AppDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class,
                "bus_database")
                .allowMainThreadQueries().build();
    }

    public void cleanUp(){
        busDB = null;
    }

}
