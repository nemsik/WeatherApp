package com.example.bartek.weatherapp.Model;

import com.google.gson.annotations.SerializedName;

public class Rain {
    @SerializedName("3h")
    private double rain;

    public Rain() {
    }

    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }
}
