package com.example.bartek.weatherapp.Database.AsyncTask;

import android.os.AsyncTask;

import com.example.bartek.weatherapp.Database.Dao.CityHoursWeatherDao;
import com.example.bartek.weatherapp.Database.Model.CityHoursWeather;

public class InsertCityAsyncTask extends AsyncTask<CityHoursWeather, Void, Void> {

    private CityHoursWeatherDao dao;
    public InsertCityAsyncTask(CityHoursWeatherDao dao){
        this.dao = dao;
    }

    @Override
    protected Void doInBackground(CityHoursWeather... params) {
        CityHoursWeather cityHoursWeather = params[0];
        dao.insert(cityHoursWeather);
        return null;
    }
}
