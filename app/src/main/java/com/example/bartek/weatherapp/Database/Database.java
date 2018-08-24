package com.example.bartek.weatherapp.Database;


import android.app.Application;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.example.bartek.weatherapp.Database.Dao.CityHoursWeatherDao;
import com.example.bartek.weatherapp.Database.Dao.CurrentWeatherDao;
import com.example.bartek.weatherapp.Database.Dao.SingleWeatherDao;
import com.example.bartek.weatherapp.Database.Model.CityHoursWeather;
import com.example.bartek.weatherapp.Database.Model.CurrentWeather;
import com.example.bartek.weatherapp.Database.Model.SingleWeather;

@android.arch.persistence.room.Database(entities = {CurrentWeather.class, CityHoursWeather.class, SingleWeather.class}, version = 11)
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

    public abstract CurrentWeatherDao currentWeatherDao();
    public abstract CityHoursWeatherDao cityHoursWeatherDao();
    public abstract SingleWeatherDao singleWeatherDao();
}

