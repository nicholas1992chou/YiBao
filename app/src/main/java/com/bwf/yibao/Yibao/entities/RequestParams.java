package com.bwf.yibao.Yibao.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nicholas on 2016/9/2.
 */
public class RequestParams implements Serializable{
    public static final String TYPE_00 = "0";



    public static final String TYPE_01 = "1";
    public static final String TYPE_02 = "2";

    public String user;
    public String userName;
    public String type;
    public String conditions;
    public Goods goods;
    public RequestParams(){
        super();
    }

    public RequestParams(String type, String conditions) {
        this.type = type;
        this.conditions = conditions;
    }

    public Map<String, String> getParamsMap(){
        Map<String, String> params = new HashMap<String, String>();
        if(!TextUtils.isEmpty(user))
            params.put("user", user);
        if(!TextUtils.isEmpty(type))
            params.put("type", type);
        if(!TextUtils.isEmpty(conditions))
            params.put("conditions", conditions);
        if(!TextUtils.isEmpty(userName))
            params.put("userName", userName);
        if(goods != null){
            String good = new Gson().toJson(goods);
            params.put("info", good);
        }
        return params;
    }
    @Override
    public String toString() {
        return "RequestParams{" +
                "user='" + user + '\'' +
                ", userName='" + userName + '\'' +
                ", type='" + type + '\'' +
                ", conditions='" + conditions + '\'' +
                ", goods=" + goods +
                '}';
    }
}
