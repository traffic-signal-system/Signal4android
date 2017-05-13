package com.aiton.zjb.signal.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/7.
 */
public class XiangWeiInfo implements Serializable {
    private List<byte[]> tongDaoPeiZhiByteArrList;
    private Map<Integer,Integer> xiangWeiCheLiuMap;

    public List<byte[]> getTongDaoPeiZhiByteArrList() {
        return tongDaoPeiZhiByteArrList;
    }

    public void setTongDaoPeiZhiByteArrList(List<byte[]> tongDaoPeiZhiByteArrList) {
        this.tongDaoPeiZhiByteArrList = tongDaoPeiZhiByteArrList;
    }

    public Map<Integer, Integer> getXiangWeiCheLiuMap() {
        return xiangWeiCheLiuMap;
    }

    public void setXiangWeiCheLiuMap(Map<Integer, Integer> xiangWeiCheLiuMap) {
        this.xiangWeiCheLiuMap = xiangWeiCheLiuMap;
    }

    public XiangWeiInfo(List<byte[]> tongDaoPeiZhiByteArrList, Map<Integer, Integer> xiangWeiCheLiuMap) {
        this.tongDaoPeiZhiByteArrList = tongDaoPeiZhiByteArrList;
        this.xiangWeiCheLiuMap = xiangWeiCheLiuMap;
    }
}
