package com.example.bartek.weatherapp.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.location.Location;
import android.util.Log;


import com.example.bartek.weatherapp.Database.AsyncTask.InsertCityAsyncTask;
import com.example.bartek.weatherapp.Database.AsyncTask.InsertCurrentAsynctask;
import com.example.bartek.weatherapp.Database.AsyncTask.InsertSingeAsyncTask;
import com.example.bartek.weatherapp.Database.Dao.CityHoursWeatherDao;
import com.example.bartek.weatherapp.Database.Dao.CurrentWeatherDao;
import com.example.bartek.weatherapp.Database.Dao.SingleWeatherDao;
import com.example.bartek.weatherapp.Database.Model.CityHoursWeather;
import com.example.bartek.weatherapp.Database.Model.CurrentWeather;
import com.example.bartek.weatherapp.Database.Model.SingleWeather;
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

    private CurrentWeatherDao currentWeatherDao;
    private CityHoursWeatherDao cityHoursWeatherDao;
    private SingleWeatherDao singleWeatherDao;
    private LiveData<CurrentWeather> currentWeatherLiveData;
    private LiveData<CityHoursWeather> cityHoursWeatherLiveData;
    private LiveData<List<SingleWeather>> singleWeatherLiveData;
    private static DatabaseRepo instance = null;
    private Retrofit retrofit;
    private IOpenWeatherMap iOpenWeatherMap;

    public static DatabaseRepo getInstance(Application application) {
        if (instance == null) {
            instance = new DatabaseRepo(application);
        }
        return instance;
    }

    public DatabaseRepo(Application application) {
        retrofit = RetrofitClient.getInstance();
        iOpenWeatherMap = retrofit.create(IOpenWeatherMap.class);

        Database db = Database.getDatabase(application);

        currentWeatherDao = db.currentWeatherDao();
        cityHoursWeatherDao = db.cityHoursWeatherDao();

        singleWeatherDao = db.singleWeatherDao();

        currentWeatherLiveData = currentWeatherDao.getCurrent();
        cityHoursWeatherLiveData = cityHoursWeatherDao.getCurrent();
        singleWeatherLiveData = singleWeatherDao.getAllSingleWeather(0);

    }

    public LiveData<CurrentWeather> getCurrentWeatherLiveData() {
        return currentWeatherLiveData;
    }

    public LiveData<CityHoursWeather> getCityHoursWeatherLiveData() {
        return cityHoursWeatherLiveData;
    }

    public LiveData<List<SingleWeather>> getSingleWeatherLiveData() {
        return singleWeatherLiveData;
    }

    public void insertFromLocation(Location location) {
        try {
            iOpenWeatherMap.getWeatherByLatLng(String.valueOf(location.getLatitude()),
                    String.valueOf(location.getLongitude())
                    , MainActivity.api_key, "metric").enqueue(new Callback<WeatherResult>() {
                @Override
                public void onResponse(Call<WeatherResult> call, Response<WeatherResult> response) {
                    //Log.d(TAG, "onResponse: " + response.body().getName());
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

    public void insert5days(Location location) {
        try {
            iOpenWeatherMap.get5DaysWeatherByLatLng(String.valueOf(location.getLatitude()),
                    String.valueOf(location.getLongitude())
                    , MainActivity.api_key, "metric").enqueue(new Callback<Weatherresult5Days>() {
                @Override
                public void onResponse(Call<Weatherresult5Days> call, Response<Weatherresult5Days> response) {
                    //Log.d(TAG, "onResponse: " + response.body().getCity().getName());
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

    private void insert5(Weatherresult5Days weatherresult5Days) {
        try {
            CityHoursWeather fiveHoursWeather = new CityHoursWeather(0, weatherresult5Days.getCity().getName()
                    , weatherresult5Days.getCity().getName());
            new InsertCityAsyncTask(cityHoursWeatherDao).execute(fiveHoursWeather);
            insertSingle(weatherresult5Days);
        }catch (Exception e){
            Log.e(TAG, "insert5: error");
        }
    }

    private void insertSingle(Weatherresult5Days weatherresult5Days) {
       try {
           SingleWeather singleWeather;
           for (int i = 0; i <= 13; i++) {
               singleWeather = new SingleWeather(0, weatherresult5Days.getList().get(i).getMain().getTemp(),
                       weatherresult5Days.getList().get(i).getDt(), weatherresult5Days.getList().get(i).getRain().getRain());
               new InsertSingeAsyncTask(singleWeatherDao).execute(singleWeather);
           }
       }catch (Exception e){
           Log.e(TAG, " insert single err" );
       }
    }


    public void insert(WeatherResult weatherResult) {
        try {
            CurrentWeather currentWeather = new CurrentWeather(
                    0,
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
                    weatherResult.getRain().getRain(),
                    weatherResult.getClouds().getAll(),
                    weatherResult.getDt(),
                    weatherResult.getSys().getSunrise(),
                    weatherResult.getSys().getSunset(),
                    weatherResult.getSys().getCountry(),
                    weatherResult.getName());
            new InsertCurrentAsynctask(currentWeatherDao).execute(currentWeather);
        } catch (Exception e) {
            Log.i(TAG, "insert err" + e.toString());
        }
    }
}
