package com.aiton.zjb.signal.model;

/**
 * Created by Administrator on 2016/11/16.
 */
public class TeShuShiJiInfo {
    private Integer index;
    private int[] dayInfoArr;
    private int[] monthInfoArr;
    private int diaoDuHao;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public int[] getDayInfoArr() {
        return dayInfoArr;
    }

    public void setDayInfoArr(int[] dayInfoArr) {
        this.dayInfoArr = dayInfoArr;
    }

    public int[] getMonthInfoArr() {
        return monthInfoArr;
    }

    public void setMonthInfoArr(int[] monthInfoArr) {
        this.monthInfoArr = monthInfoArr;
    }

    public int getDiaoDuHao() {
        return diaoDuHao;
    }

    public void setDiaoDuHao(int diaoDuHao) {
        this.diaoDuHao = diaoDuHao;
    }

    public TeShuShiJiInfo(Integer index, int[] dayInfoArr, int[] monthInfoArr, int diaoDuHao) {
        this.index = index;
        this.dayInfoArr = dayInfoArr;
        this.monthInfoArr = monthInfoArr;
        this.diaoDuHao = diaoDuHao;
    }
}
