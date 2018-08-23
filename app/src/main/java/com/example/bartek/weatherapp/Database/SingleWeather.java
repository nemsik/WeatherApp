package com.example.bartek.weatherapp.Database;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = FiveHoursWeather.class,
        parentColumns = "id",
        childColumns = "singleId",
        onDelete = CASCADE))

public class SingleWeather {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int singleId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSingleId() {
        return singleId;
    }

    public void setSingleId(int singleId) {
        this.singleId = singleId;
    }
}


