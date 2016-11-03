package com.bwf.yibao.framwork.tools;

import android.os.Environment;

import java.io.File;
import java.security.MessageDigest;

/**
 * Created by nicholas on 2016/8/24.
 */
public class FilePath {
    public static String getImageCacheDir(){
             return Environment.getExternalStorageDirectory().getPath() + File.separator + "images";
    }

    /**
     * MD5加密算法
     * 在这里主要是为了格式化保存的图片的文件名（将Http://.........jpg 转化成不含特殊字符的文件名）
     * 加密后得到的文件名是唯一的
     * @param s
     * @return
     */
    public static String MD5(String s) {
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);//放入对象中
            byte[] md = mdInst.digest();//进行加密
            StringBuffer sb = new StringBuffer();
            //去掉特殊符号
            for (int i = 0; i < md.length; i++) {
                int val = ((int) md[i]) & 0xff;
                if (val < 16)
                    sb.append("0");
                sb.append(Integer.toHexString(val));
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
