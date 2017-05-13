package com.aiton.zjb.signal.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/22.
 */
public class ShiDuanValue implements Serializable{
    private List<ArrayList<byte[]>> shiduanByteListList;

    public ShiDuanValue(List<ArrayList<byte[]>> shiduanByteListList) {
        this.shiduanByteListList = shiduanByteListList;
    }

    public List<ArrayList<byte[]>> getShiduanByteListList() {
        return shiduanByteListList;
    }

    public void setShiduanByteListList(List<ArrayList<byte[]>> shiduanByteListList) {
        this.shiduanByteListList = shiduanByteListList;
    }
}
