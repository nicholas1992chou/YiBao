package com.bwf.yibao.Yibao.entities;

/**
 * Created by Lizhangfeng on 2016/7/15 0015.
 * Description:
 */
public class BaseBean<T> {

    public String resultStatus;//接口返回码 200表示请求成功，其他表示失败

    public String resultMsg;//返回msg

    @Override
    public String toString() {
        return "BaseBean{" +
                "resultStatus='" + resultStatus + '\'' +
                ", resultMsg='" + resultMsg + '\'' +
                '}';
    }
}
