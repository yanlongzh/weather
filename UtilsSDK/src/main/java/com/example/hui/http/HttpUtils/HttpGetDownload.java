package com.example.hui.http.HttpUtils;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 项目名:    CoolWeather
 * 包名:      com.example.hui.http.HttpUtils
 * 文件名:    HttpDownload
 * 创建者:    ZYL
 * 创建时间:  2017/2/27  21:09
 * 描述:      文件下载
 */

public class HttpGetDownload {
    private HttpURLConnection conn;
    private File SD_PATH= Environment.getExternalStorageDirectory();
    private int FILE_SIZE = 10*1024;

    public File getFile(String documentName){
        File file = new File(SD_PATH,documentName);
        return file;
    }

    public  void downLoad(String url,String fileName){
        InputStream iStream = null;
        FileOutputStream fos = null;
        try {
            URL mURL = new URL(url);
            conn = (HttpURLConnection) mURL.openConnection();
            iStream = conn.getInputStream();
            fos = new FileOutputStream(getFile(fileName));
            int len = 0;
            byte[] buffer = new byte[FILE_SIZE];
            while ((len= iStream.read(buffer))!=-1){
                 fos.write(buffer,0,len);
            }
            fos.flush();
        }  catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                iStream.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
