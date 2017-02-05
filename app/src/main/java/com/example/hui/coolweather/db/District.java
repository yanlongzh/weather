package com.example.hui.coolweather.db;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yanlongzh on 2017/1/12.
 */

public class District {


    /**
     * id : 180
     * name : 长春
     * weather_id : CN101060101
     */
    @SerializedName("id")
    private int DistrictCode;
    @SerializedName("name")
    private String Districtname;

    private String weather_id;

    public int getDistrictCode() {
        return DistrictCode;
    }

    public void setDistrictCode(int DistrictCode) {
        this.DistrictCode = DistrictCode;
    }

    public String getDistrictname() {
        return Districtname;
    }

    public void setDistrictname(String Districtname) {
        this.Districtname = Districtname;
    }

    public String getWeather_id() {
        return weather_id;
    }

    public void setWeather_id(String weather_id) {
        this.weather_id = weather_id;
    }
}
