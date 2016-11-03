package com.bwf.yibao.Yibao.entities;

import java.util.List;

/**
 * Created by nicholas on 2016/9/23.
 */
public class FileBean {
    public String filePath;
    public String fileName;
    public List<String> picPathList;

    public FileBean(String filePath, String fileName,List<String> picPathList) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.picPathList = picPathList;
    }
}
