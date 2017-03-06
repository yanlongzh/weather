package com.example.hui.http;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.hui.http.HttpUtils.ImageDownload;

/**
 * 项目名:    CoolWeather
 * 包名:      com.example.hui.http
 * 文件名:    MainActivity
 * 创建者:    ZYL
 * 创建时间:  2017/2/27  19:36
 * 描述:      主页面
 */

public class MainActivity extends AppCompatActivity {
    private ImageView iv;
    private String uri = "http://7xi8d6.com1.z0.glb.clouddn.com/2017-02-28-15057157_446684252387131_4267811446148038656_n.jpg";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Button button = (Button) findViewById(R.id.button);
        iv = (ImageView) findViewById(R.id.image);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDownload download = new ImageDownload();
                download.displayImage(iv,uri);
            }
        });

    }
}
