package com.example.bartek.weatherapp.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.util.Log;
import com.example.bartek.weatherapp.Model.WeatherResult;

import java.util.List;

public class DatabaseRepo {
    private CurrentWeatherModelDao dao;
    private LiveData<CurrentWeatherModel> currentWeatherModel;
    private LiveData<List<CurrentWeatherModel>> listLiveData;
    private static DatabaseRepo instance = null;

    public static DatabaseRepo getInstance(Application application){
        if(instance == null){
            instance = new DatabaseRepo(application);
        }
        Log.i("REPO", "getInstance: " + instance);
        return instance;
    }

    public DatabaseRepo(Application application) {
        Database db = Database.getDatabase(application);
        dao = db.currentWeatherModelDao();
        currentWeatherModel = dao.getCurrent();
        listLiveData = dao.getAll();
    }

    public LiveData<CurrentWeatherModel> getCurrentWeatherModel() {
        return currentWeatherModel;
    }

    public LiveData<List<CurrentWeatherModel>> getListLiveData() {
        return listLiveData;
    }


    public void insert (WeatherResult weatherResult){
        CurrentWeatherModel currentWeatherModel = new CurrentWeatherModel(
                weatherResult.getCoord().getLon(),
                weatherResult.getCoord().getLon(),
                weatherResult.getName(),
                weatherResult.getWeather().get(0).getDescription(),
                weatherResult.getWeather().get(0).getIcon(),
                weatherResult.getMain().getTemp(),
                weatherResult.getMain().getTemp_min(),
                weatherResult.getMain().getTemp_max(),
                weatherResult.getMain().getPressure(),
                weatherResult.getMain().getHumidity(),
                weatherResult.getVisibility(),
                weatherResult.getWind().getSpeed(),
                weatherResult.getWind().getDeg(),
                weatherResult.getClouds().getAll(),
                weatherResult.getDt(),
                weatherResult.getSys().getSunrise(),
                weatherResult.getSys().getSunset(),
                weatherResult.getSys().getCountry(),
                weatherResult.getName());
        new InsertAsynctask(dao).execute(currentWeatherModel);
    }
}
