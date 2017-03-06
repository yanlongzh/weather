package com.example.hui.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;

import com.example.hui.coolweather.fragment.MainFragment;

/**
 * 项目名:    CoolWeather
 * 包名:      com.example.hui.coolweather
 * 文件名:    WeatherService
 * 创建者:    ZYL
 * 创建时间:  2017/2/26  18:04
 * 描述:      后台更新服务
 */

public class WeatherService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
            }
        });
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int  clock = 8*60*60*1000;
        long updateTime = SystemClock.elapsedRealtime()+clock;
        Intent in = new Intent(this,TimeRecivier.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,in,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,updateTime,pendingIntent);
        return super.onStartCommand(intent, flags, startId);

    }

    private void updateWeather() {
        SharedPreferences shared = getSharedPreferences("weatherLocation", Context.MODE_PRIVATE);
        String listText = shared.getString("district",null);
        new MainFragment().initData(listText);
    }
}
