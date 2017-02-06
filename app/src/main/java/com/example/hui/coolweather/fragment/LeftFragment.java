package com.example.hui.coolweather.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hui.coolweather.HttpUtil.HandlerJson;
import com.example.hui.coolweather.HttpUtil.JsonFileReader;
import com.example.hui.coolweather.R;
import com.example.hui.coolweather.db.CoolWeatherDB;
import com.example.hui.coolweather.db.LocationBean;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yanlongzh on 2017/1/15.
 */

public class LeftFragment extends Fragment implements AdapterView.OnItemClickListener{
    private static final int LEVEL_PROVINCE = 0;
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_DISTRICT = 2;
    private int CURRENT_LEVEN;
    private ListView mleftlist;
    private TextView leftlist_tv;
    private Button mleftbutton;
    private List<String> mDataList;
    private ArrayAdapter<String> mAdapter;
    private ProgressDialog dialog;
    private CoolWeatherDB mWeatherDB;
    private boolean isFrist;
    private ListItemListener mListener;
    private String textcity;
    private DrawerLayout mDrawerLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.left_layout,container,false);
        leftlist_tv = (TextView)view.findViewById(R.id.leftlist_tv);
        mleftbutton = (Button)view.findViewById(R.id.leftlist_button);
        mleftlist = (ListView)view.findViewById(R.id.left_listview);
        return view;
    }
    @Nullable
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.activity_main);
        mWeatherDB = CoolWeatherDB.getInstance(getActivity());
        mDataList = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,mDataList);
        mleftlist.setAdapter(mAdapter);
        mleftbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CURRENT_LEVEN==LEVEL_CITY){
                    queryProvince();
                }
                if(CURRENT_LEVEN==LEVEL_DISTRICT){
                    queryCity(textcity);
                }
            }
        });
        queryProvince();
        mleftlist.setOnItemClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (ListItemListener) context;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(CURRENT_LEVEN==LEVEL_PROVINCE){
           String textCity = ((TextView)view).getText().toString();
           textcity = textCity;
            queryCity(textCity);
        }
        else if(CURRENT_LEVEN==LEVEL_CITY){
            String textDistrict = ((TextView)view).getText().toString();
            queryDistrict(textDistrict);
        }
        else if(CURRENT_LEVEN==LEVEL_DISTRICT){
            String listText = ((TextView)view).getText().toString();
            mListener.onListItemListener(listText);
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }

    }

    /*查询地区*/
    private void queryDistrict( String textDistrict){
        mDataList.clear();
        leftlist_tv.setText(textDistrict);
        String JsonDistrst= JsonFileReader.getJson(getActivity(),"location.txt");
        List<Map<String,String>> mlist = HandlerJson.getCity(JsonDistrst, "result");
        for(int i=0;i<mlist.size();i++){
            if(textDistrict.equals(mlist.get(i).get("city"))){
                String district =  mlist.get(i).get("district");
                if(!mDataList.contains(mlist.get(i).get("district"))){
                    mDataList.add(district);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
        CURRENT_LEVEN = 2;
    }


    /**
     * 查询城市
     * */
    private void queryCity(String textCity){
        mleftbutton.setVisibility(View.VISIBLE);
        mDataList.clear();
        leftlist_tv.setText(textCity);
        String JsonCity = JsonFileReader.getJson(getActivity(),"location.txt");
        List<Map<String,String>> mlist = HandlerJson.getCity(JsonCity, "result");
        for(int i=0;i<mlist.size();i++){
            if(textCity.equals(mlist.get(i).get("province"))){
                String city =  mlist.get(i).get("city");
                if(!mDataList.contains(mlist.get(i).get("city"))){
                    mDataList.add(city);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
        CURRENT_LEVEN = 1;
    }

    /*查询省 直辖市*/
    private void queryProvince() {
        leftlist_tv.setText(R.string.left_titlename);
        mDataList.clear();
        showDialog();
        mleftbutton.setVisibility(View.GONE);
        String JsonProvince = JsonFileReader.getJson(getActivity(),"location.txt");
        mDataList = HandlerJson.getProvince(mDataList,JsonProvince, "result", "province");
        mAdapter.notifyDataSetChanged();
        CURRENT_LEVEN = 0;
        cancelDialog();
    }

    public void showDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle("正在加载。。。");
            dialog.setCanceledOnTouchOutside(false);
        }
    }

    public void cancelDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
    //下载数据到数据库
    public void isFirst(String response){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("read", Context.MODE_PRIVATE);
        isFrist = sharedPreferences.getBoolean("isFirst",true);
        if( isFrist){
            Gson gson = new Gson();
            LocationBean locationBean = gson.fromJson(response,LocationBean.class);
            List<LocationBean.ResultBean> listProvince = locationBean.getResult();
            for(int i=0;i<listProvince.size();i++){
                mWeatherDB.saveProvinceOne(listProvince.get(i));
            }
        }
    }
    public interface ListItemListener{
        void onListItemListener(String string);
    }
}
