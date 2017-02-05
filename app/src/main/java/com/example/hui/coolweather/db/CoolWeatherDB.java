package com.example.hui.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanlongzh on 2017/1/12.
 */

public class CoolWeatherDB {
    private  static final String DB_NAME = "cool_weather";
    private CoolWeatherDBHelper mDBHelper;
    private SQLiteDatabase sql;
    private int VERSION = 1;
    private static CoolWeatherDB mWeatherDB;
    private List<String> ProvincesList;


    private CoolWeatherDB(Context context){
        mDBHelper = new CoolWeatherDBHelper(context,DB_NAME,null,VERSION);
        sql= mDBHelper.getWritableDatabase();
        ProvincesList  = new ArrayList<>();
    }

    public static synchronized  CoolWeatherDB getInstance(Context context) {
        if (mWeatherDB == null) {
            mWeatherDB = new CoolWeatherDB(context);
        }
        return mWeatherDB;
    }

    public void saveProvinceOne(LocationBean.ResultBean resultBean){
        if(resultBean!=null){
            ContentValues cv = new ContentValues();
            cv.put("ProvinceName",resultBean.getProvince());
            cv.put("CityName",resultBean.getCity());
            cv.put("DistrictName",resultBean.getDistrict());
            sql.insert("ProvinceOne",null,cv);
        }
    }

    public List<String> loadProvince(){
        List<String> ProvincesList  = new ArrayList<>();
        Cursor mcusor = sql.query("ProvinceOne",null,null,null,null,null,null);
        if(mcusor!=null){
           while (mcusor.moveToNext()){
               String province = mcusor.getString(mcusor.getColumnIndex("ProvinceName"));
                if(!ProvincesList.contains(province)){
                    ProvincesList.add(province);
                }
           }
        }
            if (mcusor!=null){
                    mcusor.close();
            }
        return ProvincesList;
    }

    public List<String> loadCity(String cityName){
        ProvincesList  = new ArrayList<>();
        Cursor mcusor = sql.query("ProvinceOne",new String[]{"CityName"},"ProvinceName=?",new String[]{"cityName"},null,null,null);
        if(mcusor!=null){
            while (mcusor.moveToNext()){
                String province = mcusor.getString(mcusor.getColumnIndex("CityName"));
                if(!ProvincesList.contains(province)){
                    ProvincesList.add(province);
                }
            }
        }
        if (mcusor!=null){
            mcusor.close();
        }
        return ProvincesList;
    }
}
