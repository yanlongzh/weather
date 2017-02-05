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
//    public  List<String> getSampleProvince(List<LocationBean.ResultBean> mlists){
//        List<String> listDistrict = new ArrayList<>();
//
//        List<String> listCity = new ArrayList<>();
//        List<String> listProvince = new ArrayList<>();
//        int i = 0 ;
//        int j = 0 ;
//        for(i=0;i<mlists.size();i++){
//            String province =mlists.get(i).getProvince();
//            if(!listProvince.contains(province)){
//                listProvince.add(province);
//            }
//        }
//        for(j=0;j<listProvince.size();j++){
//            for (i=0;i<mlists.size();i++){
//                if(listProvince.get(j).equals(mlists.get(i).getProvince())){
//                    String city = mlists.get(i).getCity();
//                    if(!listCity.contains(city)){
//                        listCity.add(city);
//                    }
//                }
//            }
//        }
//        return listProvince;
//    }