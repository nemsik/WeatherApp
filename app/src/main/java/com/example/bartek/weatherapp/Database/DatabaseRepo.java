package com.example.bartek.weatherapp.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.location.Location;
import android.util.Log;

import com.example.bartek.weatherapp.MainActivity;
import com.example.bartek.weatherapp.Model.WeatherResult;
import com.example.bartek.weatherapp.Model.Weatherresult5Days;
import com.example.bartek.weatherapp.Retrofit.IOpenWeatherMap;
import com.example.bartek.weatherapp.Retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DatabaseRepo {
    private String TAG = "DatabaseRepo";

    private CurrentWeatherModelDao dao;
    private FiveHoursWeatherDao daofive;
    private LiveData<CurrentWeatherModel> currentWeatherModel;
    private LiveData<List<CurrentWeatherModel>> listLiveData;
    private LiveData<FiveHoursWeather> fiveHoursWeatherLiveData;
    private static DatabaseRepo instance = null;
    private Retrofit retrofit;
    private IOpenWeatherMap iOpenWeatherMap;

    public static DatabaseRepo getInstance(Application application){
        if(instance == null){
            instance = new DatabaseRepo(application);
        }
        Log.i("REPO", "getInstance: " + instance);
        return instance;
    }

    public DatabaseRepo(Application application) {
        retrofit = RetrofitClient.getInstance();
        iOpenWeatherMap = retrofit.create(IOpenWeatherMap.class);
        Database db = Database.getDatabase(application);
        dao = db.currentWeatherModelDao();
        daofive = db.fiveHoursWeatherDao();
        currentWeatherModel = dao.getCurrent();
        fiveHoursWeatherLiveData = daofive.getAllFive();
        listLiveData = dao.getAll();
    }

    public LiveData<CurrentWeatherModel> getCurrentWeatherModel() {
        return currentWeatherModel;
    }

    public LiveData<List<CurrentWeatherModel>> getListLiveData() {
        return listLiveData;
    }

    public LiveData<FiveHoursWeather> getFiveHoursWeatherLiveData() {
        return fiveHoursWeatherLiveData;
    }

    public void insertFromLocation(Location location){
        try {
            Log.d(TAG, "loadData: ");
            iOpenWeatherMap.getWeatherByLatLng(String.valueOf(location.getLatitude()),
                    String.valueOf(location.getLongitude())
                    , MainActivity.api_key, "metric").enqueue(new Callback<WeatherResult>() {
                @Override
                public void onResponse(Call<WeatherResult> call, Response<WeatherResult> response) {
                    Log.d(TAG, "onResponse: " + response.body().getName());
                    insert(response.body());
                }

                @Override
                public void onFailure(Call<WeatherResult> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.toString());
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "loadData: Error " + e.toString());
        }
    }

    public void insert5days(Location location){
        try {
            Log.d(TAG, "loadData: 5 days ");
            iOpenWeatherMap.get5DaysWeatherByLatLng(String.valueOf(location.getLatitude()),
                    String.valueOf(location.getLongitude())
                    , MainActivity.api_key, "metric").enqueue(new Callback<Weatherresult5Days>() {
                @Override
                public void onResponse(Call<Weatherresult5Days> call, Response<Weatherresult5Days> response) {
                    Log.d(TAG, "onResponse: " + response.body().getCity().getName());
                    Log.d(TAG, "onResponse: " + response.body().getList().get(1).getMain().getTemp());
                    Log.d(TAG, "onResponse: " + response.body().getList().get(1).getDt_txt());

                    insert5(response.body());
                }

                @Override
                public void onFailure(Call<Weatherresult5Days> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.toString());
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "loadData: Error " + e.toString());
        }
    }

    private void insert5(Weatherresult5Days weatherresult5Days){
        FiveHoursWeather fiveHoursWeather = new FiveHoursWeather(weatherresult5Days.getCity().getName()
                ,weatherresult5Days.getCity().getName());
        new Insert5AsyncTask(daofive).execute(fiveHoursWeather);
    }


    public void insert (WeatherResult weatherResult){
        try {
            CurrentWeatherModel currentWeatherModel = new CurrentWeatherModel(
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
        } catch (Exception e){
            Log.i(TAG, "insert err" + e.toString());
        }
    }
}
