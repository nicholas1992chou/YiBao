package com.bwf.yibao.framwork.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bwf.yibao.framwork.utils.LogUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by nicholas on 2016/9/27.
 */
public class ZcImageUploader {
    public static final int DEFAULT_COUNT = 3;
    ExecutorService mThreadPool;
    private static ZcImageUploader mInstance;
    private ZcImageUploader(){
        init();
    }

    private void init() {
        mThreadPool = Executors.newFixedThreadPool(DEFAULT_COUNT);
    }
    public static ZcImageUploader getInstance(){
        if(mInstance == null){
            synchronized (ZcImageUploader.class){
                if(mInstance == null){
                    mInstance = new ZcImageUploader();
                }
            }
        }
        return mInstance;
    }
    public void uploadImage(final String urlPath, final String imagePath, final String imageId, final String imageName){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlPath);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(10000);
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("Charset", "UTF-8");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=*****");
                    OutputStream out = conn.getOutputStream();
                    out.write(("--*****\r\nContent-Disposition: form-data; " + "name = \""+imageId+ "\";filename=\"" + imageName + "\"" + "\r\n\r\n").getBytes());
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    ByteArrayOutputStream bous = new ByteArrayOutputStream();
                    int radio = 90;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, radio, bous);
                    while(bous.toByteArray().length >= 1024 * 1024){
                        bitmap = BitmapFactory.decodeByteArray(bous.toByteArray(), 0, bous.toByteArray().length);
                        bous.reset();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, radio, bous);
                    }
                    out.write(bous.toByteArray());

                    out.write("\r\n--******\r\n".getBytes());
                    BufferedReader buffIn = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer();
                    String line = null;
                    while((line = buffIn.readLine()) != null){
                        sb.append(line);
                    }
                    out.close();
                } catch (Exception e) {
                }
            }
        };
        mThreadPool.submit(r);
    }
}
