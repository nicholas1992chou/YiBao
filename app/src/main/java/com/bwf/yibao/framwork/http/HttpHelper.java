package com.bwf.yibao.framwork.http;

import com.bwf.yibao.Yibao.entities.RequestParams;
import com.bwf.yibao.Yibao.entities.User;
import com.bwf.yibao.framwork.tools.UtilsURLPath;

import java.util.Map;

/**
 * Created by nicholas on 2016/9/6.
 */
public class HttpHelper {
    //获取商品列表
    public static void requestCategory(HttpUtil.Method method, RequestParams requestParams, HttpUtil.CallBack callBack){
        String url = UtilsURLPath.getSortPath;
        Map<String, String> params = requestParams.getParamsMap();
        HttpUtil.newInstance().request(url, HttpUtil.Method.POST, params, callBack);
    }
    public static void userLogin(RequestParams requestParams, HttpUtil.CallBack callBack){
        String url = UtilsURLPath.userLogin;
        HttpUtil.newInstance().request(url, HttpUtil.Method.POST, requestParams.getParamsMap(), callBack);

    }
    public static void registerUser(RequestParams requestParams, HttpUtil.CallBack callBack){
        String url = UtilsURLPath.userRegister;
        HttpUtil.newInstance().request(UtilsURLPath.userRegister,HttpUtil.Method.POST, requestParams.getParamsMap(), callBack);
    }
    public static void getGoods(RequestParams requestParams, HttpUtil.CallBack callBack){
        HttpUtil.newInstance().request(UtilsURLPath.getGoodsPath,HttpUtil.Method.POST, requestParams.getParamsMap(), callBack);
    }
    public static void queryCollection(RequestParams requestParams, HttpUtil.CallBack callBack){
        HttpUtil.newInstance().request(UtilsURLPath.queryCollectList,HttpUtil.Method.POST, requestParams.getParamsMap(), callBack);
    }
    public static void queryPublishedGoods(RequestParams requestParams, HttpUtil.CallBack callBack){
        HttpUtil.newInstance().request(UtilsURLPath.getGoodsPath,HttpUtil.Method.POST, requestParams.getParamsMap(), callBack);
    }
    public static void publishGoods(RequestParams requestParams, HttpUtil.CallBack callBack){
        HttpUtil.newInstance().request(UtilsURLPath.publishgGoodsPath,HttpUtil.Method.POST, requestParams.getParamsMap(), callBack);
    }
}
