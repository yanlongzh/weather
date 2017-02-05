package com.example.hui.coolweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.hui.coolweather.HttpUtil.LeftFragment;

public class MainActivity extends AppCompatActivity implements LeftFragment.ListItemListener{
    private LeftFragment.ListItemListener mListener;
    private DrawerLayout mDrawerLayout;
    private Button title_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=21){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        setContentView(R.layout.activity_main);
        initView();
        isFristIn();
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_main);
        title_button = (Button) findViewById(R.id.title_button);
        SharedPreferences sharedPreferences = getSharedPreferences("read", Context.MODE_PRIVATE);
        boolean first = sharedPreferences.getBoolean("isFirst",true);
        if(first){
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        title_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    public void isFristIn(){
        SharedPreferences sharedPreferences = getSharedPreferences("read", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirst",false);
        editor.apply();
    }

    @Override
    public void onListItemListener(String string) {
        FragmentManager manager = getSupportFragmentManager();
       MainFragment mainFragment = (MainFragment) manager.findFragmentById(R.id.main_fragment);
        mainFragment.initData(string);
    }
}
