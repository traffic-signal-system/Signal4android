package com.aiton.zjb.signal.model;

/**
 * Created by Administrator on 2016/11/16.
 */
public class ShiJiInfo {
    private Integer index;
    private int[] shiJiInfoArr;
    private int diaoDuHao;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public int[] getShiJiInfoArr() {
        return shiJiInfoArr;
    }

    public void setShiJiInfoArr(int[] shiJiInfoArr) {
        this.shiJiInfoArr = shiJiInfoArr;
    }

    public int getDiaoDuHao() {
        return diaoDuHao;
    }

    public void setDiaoDuHao(int diaoDuHao) {
        this.diaoDuHao = diaoDuHao;
    }

    public ShiJiInfo(Integer index, int[] shiJiInfoArr, int diaoDuHao) {
        this.index = index;
        this.shiJiInfoArr = shiJiInfoArr;
        this.diaoDuHao = diaoDuHao;
    }
}
