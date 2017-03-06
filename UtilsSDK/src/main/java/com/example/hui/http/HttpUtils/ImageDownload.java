package com.example.hui.http.HttpUtils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.example.hui.http.Utils.EncryptUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.util.Log.i;

/**
 * 项目名:    CoolWeather
 * 包名:      com.example.hui.http.HttpUtils
 * 文件名:    ImageDownload
 * 创建者:    ZYL
 * 创建时间:  2017/2/28  22:34
 * 描述:     下载图片 三级缓存
 */

public class ImageDownload {
    private MemoryCacheImage mMemoryCacheImage;
    private LocalCacheImage mLocalCacheImage;
    private NetDownloadUiTask mNetDownloadUiTask;

    public ImageDownload() {
         mMemoryCacheImage = new MemoryCacheImage();
        mLocalCacheImage = new LocalCacheImage();
        mNetDownloadUiTask = new NetDownloadUiTask(mMemoryCacheImage,mLocalCacheImage);
    }

    public void displayImage(ImageView iv,String uri){
        Bitmap bitmap = mMemoryCacheImage.getBitmapFromMemory(uri);
        if (bitmap!=null){
            i("TAG", "zaiaaaaaa");
            iv.setImageBitmap(bitmap);
            return;
        }
        bitmap = mLocalCacheImage.getBitmapFromLocalCache(uri);
        if (bitmap!=null){
            i("TAG", "zaiaaaaaa");
            iv.setImageBitmap(bitmap);
            mMemoryCacheImage.setBitmapToMemory(uri,bitmap);
            return;
        }
       mNetDownloadUiTask.execute(iv,uri);
    }


    /**
     * 可在UI线程调用
     */
    class NetDownloadUiTask extends AsyncTask<Object, Void, Bitmap> {
        LocalCacheImage mLocalCacheImage;
        MemoryCacheImage mMemoryCacheImage ;
        ImageView mImageView;
        String uri;

        public NetDownloadUiTask(MemoryCacheImage memoryCacheImage, LocalCacheImage localCacheImage) {
            mMemoryCacheImage = memoryCacheImage;
            mLocalCacheImage = localCacheImage;
        }

        @Override
        protected Bitmap doInBackground(Object[] params) {
            mImageView = (ImageView) params[0];
            uri = (String) params[1];
            Bitmap bitmap =downloadFromNet(uri);
            return bitmap;
        }

        //耗时操作执行之前执行
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        //在主线程执行
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap!=null){
                mImageView.setImageBitmap(bitmap);
            }
        }

        private Bitmap downloadFromNet(String url) {
            Bitmap bitmap = null;
            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(5000);
                urlConnection.setRequestMethod("GET");
                if (urlConnection.getResponseCode() == 200) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    InputStream inputStream = urlConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    i("TAG youhua",bitmap.getByteCount()+"");
                    mLocalCacheImage.setToLocalMemory(url,bitmap);
                    mMemoryCacheImage.setBitmapToMemory(url,bitmap);
                    return bitmap;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    /**
     * 本地缓存类
     */
    public class LocalCacheImage {

        private String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/imagesss";

        private File getFilePath(String uri){
            String fileName = EncryptUtil.encrypt(uri);
            File file = new File(SDPath,fileName);
            File parentPath = file.getParentFile();
            if(!parentPath.exists()){
                parentPath.mkdirs();
                Log.i("filepath",parentPath.toString());
            }
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.i("filepath",file.toString());
            return file;
        }
        private Bitmap getBitmapFromLocalCache(String uri) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(getFilePath(uri)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        //网络下载后缓存至本地内存中
        private void setToLocalMemory(String uri,Bitmap bitmap){
            try {
                //中间的参数 100表示不压缩，如果是30 表示压缩70%
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(getFilePath(uri)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 内存缓存类
     */
    public class MemoryCacheImage {
        LruCache<String, Bitmap> mLruCache;
        /**
         * 初始化缓存的大小为内存的八分之一
         */
        public MemoryCacheImage() {
            long maxMemory = Runtime.getRuntime().maxMemory() / 8;
            mLruCache = new LruCache<String, Bitmap>((int) maxMemory) {
                //计算每个条目的大小
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    int bitmapSize = value.getByteCount();
                    return bitmapSize;
                }
            };
        }

        /**
         * 从内存读取图片
         * @param uri
         * @return
         */
        public Bitmap getBitmapFromMemory(String uri) {
            Bitmap bitmap = mLruCache.get(uri);
            return bitmap;
        }

        public void setBitmapToMemory(String uri, Bitmap bitmap) {
            mLruCache.put(uri, bitmap);
        }
    }

    /**
     * inPurgeable:表明当内存不够时候，对bitmap进行回收
     *
     * @return
     */
    public Bitmap inpurgeableBitmap(String filePath) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        if (filePath != null) {
            bitmap = BitmapFactory.decodeFile(filePath, options);
        }
        return bitmap;
    }

    public Bitmap decodeImgageFromResource(Resources resources,int resId,int reqWidth,int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources,resId,options);
        options.inSampleSize = calculateSimpleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources,resId,options);
    }

    private int calculateSimpleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int simpleSize = 1;
        if(width>reqWidth||height>reqHeight){
            int halfWidth = width/2;
            int halfHeiget = height/2;
            while ((halfHeiget/simpleSize)>reqHeight||(halfWidth/simpleSize)>reqWidth){
               simpleSize *=2;
            }
        }
        return simpleSize;
    }
}
