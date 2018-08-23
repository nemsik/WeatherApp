package com.example.bartek.weatherapp.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.example.bartek.weatherapp.Model.WeatherResult;

@Entity
public class CurrentWeatherModel {

    @PrimaryKey(autoGenerate = true)
    public int id;
    private String weather_desc;
    private String weather_icon;
    private double temp;
    private double min_temp;
    private double max_temp;
    private float pressure;
    private int humidity;
    private int visibility;
    private double wind_speed;
    private double wind_deg;
    private int clouds;
    private long dt;
    private int sunrise;
    private int sunset;
    private String country;
    private String city_name;

    public CurrentWeatherModel(String weather_desc, String weather_icon, double temp, double min_temp, double max_temp, float pressure, int humidity, int visibility, double wind_speed, double wind_deg, int clouds, long dt, int sunrise, int sunset, String country, String city_name) {
        this.id = id;
        this.weather_desc = weather_desc;
        this.weather_icon = weather_icon;
        this.temp = temp;
        this.min_temp = min_temp;
        this.max_temp = max_temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.visibility = visibility;
        this.wind_speed = wind_speed;
        this.wind_deg = wind_deg;
        this.clouds = clouds;
        this.dt = dt;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.country = country;
        this.city_name = city_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getWeather_desc() {
        return weather_desc;
    }

    public void setWeather_desc(String weather_desc) {
        this.weather_desc = weather_desc;
    }

    public String getWeather_icon() {
        return weather_icon;
    }

    public void setWeather_icon(String weather_icon) {
        this.weather_icon = weather_icon;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getMin_temp() {
        return min_temp;
    }

    public void setMin_temp(double min_temp) {
        this.min_temp = min_temp;
    }

    public double getMax_temp() {
        return max_temp;
    }

    public void setMax_temp(double max_temp) {
        this.max_temp = max_temp;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public double getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public double getWind_deg() {
        return wind_deg;
    }

    public void setWind_deg(double wind_deg) {
        this.wind_deg = wind_deg;
    }

    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public int getSunrise() {
        return sunrise;
    }

    public void setSunrise(int sunrise) {
        this.sunrise = sunrise;
    }

    public int getSunset() {
        return sunset;
    }

    public void setSunset(int sunset) {
        this.sunset = sunset;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }
}
