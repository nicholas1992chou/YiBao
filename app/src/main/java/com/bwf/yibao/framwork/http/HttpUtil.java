package com.bwf.yibao.framwork.http;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.bwf.yibao.framwork.utils.LogUtils;
import com.bwf.yibao.framwork.utils.NetWorkUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by nicholas on 2016/9/2.
 */
public class HttpUtil {


    private static final int FAILED = 0;
    private static final int SUCCESS = 1;
    private static final String ERROR = "请求失败";


    private CallBack callBack;
    private static HttpUtil httpUtil;
    private HttpUtil(){

    }
    /*public static HttpUtil newInstance(){
        if(httpUtil == null){
            synchronized (HttpUtil.class){
                if(httpUtil == null){
                    httpUtil = new HttpUtil();
                    mExecutor = Executors.newFixedThreadPool(4);
                }
            }
        }
        return httpUtil;
    }*/
    static {
        mExecutor = Executors.newFixedThreadPool(4);
    }
    public static HttpUtil newInstance(){
        return new HttpUtil();
    }
    private static ExecutorService mExecutor;
    public static ExecutorService getmExecutor(){
        return mExecutor;
    }
    //handler
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS:
                    String result = (String)msg.obj;
                   callBack.onSuccess(result);
                break;
                case FAILED:
                    callBack.onFailed(ERROR);
                default:
                    break;
            }
        }
    };
    //开线程,请求数据
    class RequestRunnable implements Runnable{
        String url;
        Method method;
        Map<String, String> params;

        public RequestRunnable(String url, Method method, Map<String, String> params) {
            this.url = url;
            this.method = method;
            this.params = params;
        }

        @Override
        public void run() {
                if("POST".equals(method.str)){
                    try {
                        HttpURLConnection conn =  buildConnection(url);
                        conn.setRequestMethod(method.str);
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                        String params = getParams(this.params);
                        bw.write(params);
                        bw.flush();
                        bw.close();
                        if(200 == conn.getResponseCode()){
                            StringBuffer sb = new StringBuffer();
                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            String line = null;
                            while((line = br.readLine()) != null){
                                sb.append(line);
                            }
                            Message message = Message.obtain();
                            message.what = SUCCESS;
                            message.obj = sb.toString();
                            mHandler.sendMessage(message);
                        }else{
                            mHandler.sendEmptyMessage(FAILED);
                            return;
                        }
                    } catch (Exception e) {
                        mHandler.sendEmptyMessage(FAILED);
                        return;
                    }
                }else{
                    String params = getParams(this.params);
                    url = url + "?" + params;
                    try {
                        HttpURLConnection conn =  buildConnection(url);
                        conn.setRequestMethod(method.str);
                        if(200 == conn.getResponseCode()){
                            StringBuffer sb = new StringBuffer();
                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            String line = null;
                            while((line = br.readLine()) != null){
                                sb.append(line);
                            }
                            Message message = Message.obtain();
                            message.what = SUCCESS;
                            message.obj = sb.toString();
                            mHandler.sendMessage(message);
                        }else{
                            mHandler.sendEmptyMessage(FAILED);
                            return;
                        }
                    } catch (Exception e) {
                        mHandler.sendEmptyMessage(FAILED);
                        return;
                    }

            }
        }
    }
    private String getParams(Map<String, String> params){
        StringBuffer sb = new StringBuffer();
        for(Map.Entry<String, String> entry : params.entrySet()){
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    //创建连接,执行在子线程
    private HttpURLConnection buildConnection(String urlPath){
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlPath);
            conn = (HttpURLConnection) url.openConnection();
            //设置参数
            conn.setConnectTimeout(8 * 1000);
            conn.setReadTimeout(8 * 1000);
            conn.setDoOutput(true);// 是否输入参数
            conn.setDoInput(true);
        } catch (Exception e) {
            return null;
        }
        return conn;
    }
    //发送请求
    public void request(String url, Method method, Map<String, String> params, HttpUtil.CallBack callBack){
        this.callBack = callBack;
        mExecutor.submit(new RequestRunnable(url, method, params));
    }
    //接口回调
    public interface CallBack{
        void onSuccess(String result);
        void onFailed(String error);
    }
    //枚举，限制参数类型
    public enum Method{
        GET("GET"), POST("POST");
        public String str;
        Method(String str){
            this.str = str;
        }

    }
}
