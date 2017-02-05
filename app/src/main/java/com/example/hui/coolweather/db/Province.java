package com.example.hui.coolweather.db;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yanlongzh on 2017/1/12.
 */

public class Province {

    /**
     * id : 1
     * name : 北京
     */
    @SerializedName("id")
    private int ProvinceCode;
    @SerializedName("name")
    private String ProvinceName;
//    private int id;
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public int getProvinceCode() {
        return ProvinceCode;
    }

    public void setProvinceCode(int ProvinceCode) {
        this.ProvinceCode = ProvinceCode;
    }

    public String getProvinceName() {
        return ProvinceName;
    }
    public void setProvinceName(String ProvinceName) {
        this.ProvinceName= ProvinceName;
    }
}
