package com.example.bartek.weatherapp.Database.AsyncTask;

import android.os.AsyncTask;

import com.example.bartek.weatherapp.Database.Dao.SingleWeatherDao;
import com.example.bartek.weatherapp.Database.Model.SingleWeather;

public class InsertSingeAsyncTask extends AsyncTask<SingleWeather, Void, Void> {
    private SingleWeatherDao singleWeatherDao;
    public InsertSingeAsyncTask(SingleWeatherDao singleWeatherDao) {
        this.singleWeatherDao = singleWeatherDao;
    }

    @Override
    protected Void doInBackground(SingleWeather... params) {
        SingleWeather singleWeather = params[0];
        singleWeatherDao.insert(singleWeather);
        return null;
    }
}
