package com.aiton.zjb.signal.model;

/**
 * Created by Administrator on 2016/11/22.
 */
public class ShiDuanInfo {
    private int shiDuanNum;
    private int shiDuanShi;
    private int ShiDuanFen;
    private int controlType;
    private int peiShiFangAn;

    public int getShiDuanNum() {
        return shiDuanNum;
    }

    public void setShiDuanNum(int shiDuanNum) {
        this.shiDuanNum = shiDuanNum;
    }

    public int getShiDuanShi() {
        return shiDuanShi;
    }

    public void setShiDuanShi(int shiDuanShi) {
        this.shiDuanShi = shiDuanShi;
    }

    public int getShiDuanFen() {
        return ShiDuanFen;
    }

    public void setShiDuanFen(int shiDuanFen) {
        ShiDuanFen = shiDuanFen;
    }

    public int getControlType() {
        return controlType;
    }

    public void setControlType(int controlType) {
        this.controlType = controlType;
    }

    public int getPeiShiFangAn() {
        return peiShiFangAn;
    }

    public void setPeiShiFangAn(int peiShiFangAn) {
        this.peiShiFangAn = peiShiFangAn;
    }

    public ShiDuanInfo(int shiDuanNum, int shiDuanShi, int shiDuanFen, int controlType, int peiShiFangAn) {
        this.shiDuanNum = shiDuanNum;
        this.shiDuanShi = shiDuanShi;
        ShiDuanFen = shiDuanFen;
        this.controlType = controlType;
        this.peiShiFangAn = peiShiFangAn;
    }
}
