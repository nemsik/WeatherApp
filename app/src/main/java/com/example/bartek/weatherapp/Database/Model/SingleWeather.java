package com.example.bartek.weatherapp.Database.Model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.example.bartek.weatherapp.Database.Model.CityHoursWeather;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = CityHoursWeather.class,
        parentColumns = "id",
        childColumns = "fiveId",
        onDelete = CASCADE))

public class SingleWeather {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private int fiveId;
    private double temp;
    private long dt;
    private double rain;

    public SingleWeather(int fiveId, double temp, long dt, double rain) {
        this.fiveId = fiveId;
        this.temp = temp;
        this.dt = dt;
        this.rain = rain;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFiveId() {
        return fiveId;
    }

    public void setFiveId(int fiveId) {
        this.fiveId = fiveId;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }
}


