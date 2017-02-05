package com.example.hui.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yanlongzh on 2017/1/11.
 */

public class CoolWeatherDBHelper extends SQLiteOpenHelper{
    private static final String PROVINCE_ONE = "create table ProvinceOne("+"id integer primary key autoincrement,"+
            "ProvinceName text,"+"CityName text,"+"DistrictName text)";
//    //private static final String CITY = "create table City("+"id integer primary key autoincrement,"+
//            "CityName text,"+"CityCode text)";
//   // private static final String COUNTY = "create table County("+"id integer primary key autoincrement,"+
//            "DistrictName text,"+"DistrictCode text)";

    public CoolWeatherDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PROVINCE_ONE);
//        db.execSQL(COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
