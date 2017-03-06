package com.example.hui.coolweather.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 项目名:    CoolWeather
 * 包名:      com.example.hui.coolweather.service
 * 文件名:    TimeRecivier
 * 创建者:    ZYL
 * 创建时间:  2017/2/26  18:50
 * 描述:     广播接收
 */
public class TimeRecivier extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 =  new Intent(context,WeatherService.class);
        context.startService(intent1);
    }
}
