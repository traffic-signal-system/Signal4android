package com.aiton.zjb.signal.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/5.
 */
public class DengBanInfo implements Serializable{
    private String DengBan;
    private int tongDaoIndex;

    public String getDengBan() {
        return DengBan;
    }

    public void setDengBan(String dengBan) {
        DengBan = dengBan;
    }

    public int getTongDaoIndex() {
        return tongDaoIndex;
    }

    public void setTongDaoIndex(int tongDaoIndex) {
        this.tongDaoIndex = tongDaoIndex;
    }

    public DengBanInfo(String dengBan, int tongDaoIndex) {
        DengBan = dengBan;
        this.tongDaoIndex = tongDaoIndex;
    }
}
