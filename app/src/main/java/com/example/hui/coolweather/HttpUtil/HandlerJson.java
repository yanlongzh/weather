package com.example.hui.coolweather.HttpUtil;

import com.example.hui.coolweather.db.WeatherBean;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yanlongzh on 2017/1/15.
 */

public class HandlerJson {

    public static List<String> getProvince(List<String> list,String response,String target,String element){
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(response).getAsJsonObject().getAsJsonArray(target);
        List<Map<String,String>> mlist = gson.fromJson(array,new TypeToken< List<Map<String,String>>>(){}.getType());
        for(int i=0;i<mlist.size();i++){
             String last = mlist.get(i).get(element);
            if(!list.contains(last)){
                list.add(last);
            }
        }
        return list;
    }
    public static List<Map<String,String>> getCity(String response,String target){
        List<Map<String,String>> mlist = new ArrayList<Map<String, String>>();
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(response).getAsJsonObject().getAsJsonArray(target);
        mlist = gson.fromJson(array,new TypeToken< List<Map<String,String>>>(){}.getType());
        return mlist;
    }

    public static List<String> getProvinceList(List<String> list,String response,String target,String element){
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(response).getAsJsonObject().getAsJsonArray(target);
        List<Map<String,String>> mlist = gson.fromJson(array,new TypeToken< List<Map<String,String>>>(){}.getType());
        for(int i=0;i<mlist.size();i++){
            String last = mlist.get(i).get(element);
            list.add(last);
        }
        return list;
    }

    public static WeatherBean getWeather(String weather){
        Gson gson = new Gson();
        WeatherBean weatherBean = gson.fromJson(weather,WeatherBean.class);
        return  weatherBean;
    }
}
