package com.example.hui.coolweather.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hui.coolweather.HttpUtil.HandlerJson;
import com.example.hui.coolweather.HttpUtil.HttpUtil;
import com.example.hui.coolweather.R;
import com.example.hui.coolweather.db.WeatherLocationBean;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by yanlongzh on 2017/1/11.
 */

public class MainFragment extends Fragment {
    private TextView title_tv;
    private TextView title_time;
    private Button title_button;
    private TextView now_temper;
    private TextView now_cloud;
    private TextView quality_humidity;
    private TextView quality_uv;
    private TextView dressing_advice;
    private TextView travel_advice;
    private TextView exercise_advice;
    private View view;
    private LinearLayout mLayout;
    private TextView data_tv,wind_tv;
    private TextView temper_tv;
    private TextView weather_tv;
    private SwipeRefreshLayout refreshLayout;


    public MainFragment() {
        super();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_layout,container,false);
        initView();
        initFragment();
        return view;
    }

    private void initFragment() {
        SharedPreferences shared = getActivity().getSharedPreferences("weatherLocation",Context.MODE_PRIVATE);
        String jsonString = shared.getString("weather",null);
        String listText = shared.getString("district",null);
        if(jsonString!=null&&listText!=null){
            refreshLayout.setRefreshing(true);
            initData(listText);
        }
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SharedPreferences shared = getActivity().getSharedPreferences("weatherLocation",Context.MODE_PRIVATE);
                String district = shared.getString("district",null);
                if (district != null){
                    initData(district);
                }
            }
        });
    }

    private void initView() {
        refreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        title_tv = (TextView) view.findViewById(R.id.title_tv);
        title_time =(TextView) view.findViewById(R.id.title_time);
        title_button = (Button) view.findViewById(R.id.title_button);
        now_temper=(TextView) view.findViewById(R.id.now_temper);
        now_cloud=(TextView) view.findViewById(R.id.now_cloud);
        mLayout = (LinearLayout) view.findViewById(R.id.forecast_layout);
        quality_humidity=(TextView) view.findViewById(R.id.quality_AQI);
        quality_uv=(TextView) view.findViewById(R.id.quality_uv);
        dressing_advice=(TextView) view.findViewById(R.id.dress_tv);
        travel_advice=(TextView) view.findViewById(R.id.travel_tv);
        exercise_advice=(TextView) view.findViewById(R.id.exercise_tv);
    };

    /**
     * 请求网络数据
     * @param listText
     */
    public void initData(final String listText) {
        String url ="http://api.jisuapi.com/weather/query?appkey=d4b8a9a7cb555e9f&city="+listText;
        HttpUtil.sendHttpRequst(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"网路异常",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String jsonString = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(jsonString!=null) {
                            SharedPreferences sharedPreferences = getActivity().
                                    getSharedPreferences("weatherLocation", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("weather", jsonString).putString("district", listText)
                                    .apply();
                            requstWeather(listText, jsonString);
                        }else{
                            Toast.makeText(getActivity(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    public void requstWeather(String listText,String jsonString){
        WeatherLocationBean weatherBean =  HandlerJson.getWeather(jsonString);
        WeatherLocationBean.ResultBean resultBean = weatherBean.getResult();
        title_tv.setText(resultBean.getCity());
        title_time.setText(formatTime(resultBean.getUpdatetime()));
        now_temper.setText(resultBean.getTemp()+"℃");
        now_cloud.setText(resultBean.getWeather());
        List<WeatherLocationBean.ResultBean.IndexBean> indexBean = resultBean.getIndex();
        List<WeatherLocationBean.ResultBean.DailyBean> dailyBeanList = resultBean.getDaily();
        mLayout.removeAllViews();
        for(WeatherLocationBean.ResultBean.DailyBean dailyBean:dailyBeanList){
            WeatherLocationBean.ResultBean.DailyBean.DayBean dayBean = dailyBean.getDay();
            View viewlist = LayoutInflater.from(getActivity()).inflate(R.layout.forecast_item,null);
            data_tv = (TextView) viewlist.findViewById(R.id.data_tv);
            temper_tv = (TextView) viewlist.findViewById(R.id.temper_tv);
            weather_tv = (TextView) viewlist.findViewById(R.id.weather_tv);
            wind_tv = (TextView) viewlist.findViewById(R.id.wind_tv);
            data_tv.setText(dailyBean.getDate());
            temper_tv.setText(dayBean.getTemphigh()+"℃");
            weather_tv.setText(dayBean.getWeather());
            wind_tv.setText(dayBean.getWindpower());
            mLayout.addView(viewlist);
        }
        quality_humidity.setText(resultBean.getHumidity());
        quality_uv.setText(resultBean.getWindpower());
        List<WeatherLocationBean.ResultBean.IndexBean> index = resultBean.getIndex();

        dressing_advice.setText("空调指数："+index.get(0).getDetail());
        travel_advice.setText("感冒指数："+index.get(3).getDetail());
        exercise_advice.setText("锻炼建议："+index.get(1).getDetail());
    }

    /**
     * 格式化时间类
     * @param time
     * @return
     */
    private String formatTime(String time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
        Date date = null;
        String string  = null;
        try {
            date = format.parse(time);
            string = format1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return string;
    }
}
