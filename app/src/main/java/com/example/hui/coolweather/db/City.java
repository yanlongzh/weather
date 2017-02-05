package com.example.hui.coolweather.db;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yanlongzh on 2017/1/12.
 */

public class City {

    @SerializedName("id")
    private int CityCode;
    @SerializedName("name")
    private String CityName;


    public int getCityCode() {
        return CityCode;
    }

    public void setCityCode(int CityCode) {
        this.CityCode = CityCode;
    }

    public String getCityName() {
        return CityName;
    }
    public void setCityName(String CityName) {
        this.CityName = CityName;
    }
}
