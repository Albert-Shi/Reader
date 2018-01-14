package com.shishuheng.reader.datastructure;

import java.io.Serializable;

/**
 * Created by shishuheng on 2018/1/2.
 */
//此类用来存储书本的数据
public class TxtDetail implements Serializable{
    private String name;
    private String path;
    private long hasReadPointer = 0;
    private String firstLineLastExit = "";

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setPath(String path) {
        this.path = path;
    }
    public String getPath() {
        return path;
    }

    public void setHasReadPointer(long hasReadPointer) {
        this.hasReadPointer = hasReadPointer;
    }

    public long getHasReadPointer() {
        return hasReadPointer;
    }

    public String getFirstLineLastExit() {
        return firstLineLastExit;
    }

    public void setFirstLineLastExit(String firstLineLastExit) {
        this.firstLineLastExit = firstLineLastExit;
    }
}
