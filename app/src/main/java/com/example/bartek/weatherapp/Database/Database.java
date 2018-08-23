package com.example.bartek.weatherapp.Database;


import android.app.Application;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

@android.arch.persistence.room.Database(entities = CurrentWeatherModel.class, version = 5)
public abstract class Database extends RoomDatabase {

    private static Database INSTANCE;

    public static Database getDatabase(final Application application){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(application.getApplicationContext(), Database.class, "weather_db")
                    .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
    public static void destroyInstances(){
        INSTANCE = null;
    }
    public abstract CurrentWeatherModelDao currentWeatherModelDao();
}

