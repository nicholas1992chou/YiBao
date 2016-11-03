package com.bwf.yibao;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.bwf.yibao.Yibao.entities.User;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Description: 自定义Application 。
 *   1.可以存全局变量或者对象（整个app）
 *   2.app一启动最先创建application
 *   3.一个app只有一个application,但是每多加一个进程都会初始化一次Application
 *   4.如果像初始化一次的话，可以只在一个进程中初始化(实际用的时候说)
 */
public class MyApplication extends Application {
    private String str;
    private User user;
    private UMShareAPI umShareAPI;


    public UMShareAPI getUmShareAPI() {
        return umShareAPI;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        notify(user);
    }

    private static MyApplication app;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        //UM
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");

        umShareAPI = UMShareAPI.get(getApplicationContext());
        //Fresco
        Fresco.initialize(this);
        //Jpush
        JPushInterface.init(getApplicationContext());
        JPushInterface.setDebugMode(true);
        //Zxing
        ZXingLibrary.initDisplayOpinion(this);
        getUserInfo();
    }
    public static MyApplication getApplication(){
        return app;
    }

    public static Context getAppContext(){
        return app.getApplicationContext();
    }

    public void setStr(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }
    private void getUserInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userInfo = sharedPreferences.getString("userInfo", "");
        if(!TextUtils.isEmpty(userInfo)){
            User user = new Gson().fromJson(userInfo, User.class);
            setUser(user);
        }
    }

    public List<OnLoginListener> onLoginListenerList = new ArrayList<>();
    public interface OnLoginListener{
        void onLogin(User user);
    }
    public void notify(User user){
        for(OnLoginListener onLoginListener: onLoginListenerList){
            onLoginListener.onLogin(user);
        }
    }
    public void registerOnLoginListener(OnLoginListener onLoginListener){
        if(!onLoginListenerList.contains(onLoginListener)){
            onLoginListenerList.add(onLoginListener);
            onLoginListener.onLogin(user);
        }
    }
    public void unRegisterOnLoginListener(OnLoginListener onLoginListener){
            onLoginListenerList.remove(onLoginListener);
    }
}
