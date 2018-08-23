package com.example.bartek.weatherapp.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "fiveWeather")
public class FiveHoursWeather {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String city_name;
    private String country_name;

    public FiveHoursWeather(String city_name, String country_name) {
        this.city_name = city_name;
        this.country_name = country_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }
}
