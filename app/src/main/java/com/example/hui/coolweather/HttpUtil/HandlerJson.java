package com.example.hui.coolweather.HttpUtil;

import com.example.hui.coolweather.db.LocationBean;
import com.example.hui.coolweather.db.WeatherLocationBean;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yanlongzh on 2017/1/15.
 */

public class HandlerJson {

    public static List<Map<String,String>> getCity(String response,String target){
        List<Map<String,String>> mlist = new ArrayList<Map<String, String>>();
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(response);
        JsonObject object = element.getAsJsonObject();
        JsonArray array = object.getAsJsonArray(target);
        mlist = gson.fromJson(array,new TypeToken< List<Map<String,String>>>(){}.getType());
        return mlist;
    }

    public static List<String> getProvinceList(List<String> list,String response,String target,String element) {
        Gson gson = new Gson();
        LocationBean locationBean = gson.fromJson(response, LocationBean.class);
        List<LocationBean.ResultBean> resultBeans = locationBean.getResult();
        for (int i = 0; i < resultBeans.size(); i++) {
            LocationBean.ResultBean resultBean = resultBeans.get(i);
            String province = resultBean.getProvince();
            if (!list.contains(province)) {
                list.add(province);
            }
        }
        return list;
    }

    public static WeatherLocationBean getWeather(String weather){
        Gson gson = new Gson();
        WeatherLocationBean weatherBean = gson.fromJson(weather,WeatherLocationBean.class);
        return  weatherBean;
    }
}
