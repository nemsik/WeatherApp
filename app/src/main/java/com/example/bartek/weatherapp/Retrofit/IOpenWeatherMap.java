package com.example.bartek.weatherapp.Retrofit;

import android.arch.lifecycle.LiveData;

import com.example.bartek.weatherapp.Model.WeatherResult;
import com.example.bartek.weatherapp.Model.Weatherresult5Days;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMap {
    @GET("weather")
    Call<WeatherResult> getWeatherByLatLng(@Query("lat") String lat,
                                           @Query("lon") String lng,
                                           @Query("appid") String appid,
                                           @Query("units") String unit);

    @GET("forecast")
    Call<Weatherresult5Days> get5DaysWeatherByLatLng(@Query("lat") String lat,
                                                     @Query("lon") String lng,
                                                     @Query("appid") String appid,
                                                     @Query("units") String unit);
}
