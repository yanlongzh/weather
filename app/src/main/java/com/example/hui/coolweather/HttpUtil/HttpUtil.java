package com.example.hui.coolweather.HttpUtil;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by yanlongzh on 2017/1/12.
 */

public class HttpUtil {

    private  static OkHttpClient mHttpClient;

    public static void sendHttpRequst(String url, Callback callback){
        mHttpClient = new OkHttpClient();
        Request  request = new Request.Builder().url(url).build();
       mHttpClient.newCall(request).enqueue(callback);
    }
}
