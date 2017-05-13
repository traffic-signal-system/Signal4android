package com.aiton.zjb.signal.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/28.
 */
public class AreaListInfo implements Serializable {
    private List<NodeAndGroupAndAreaInfo.ObjectEntity.AreaListEntity> mAreaList;

    public List<NodeAndGroupAndAreaInfo.ObjectEntity.AreaListEntity> getAreaList() {
        return mAreaList;
    }

    public void setAreaList(List<NodeAndGroupAndAreaInfo.ObjectEntity.AreaListEntity> areaList) {
        mAreaList = areaList;
    }

    public AreaListInfo(List<NodeAndGroupAndAreaInfo.ObjectEntity.AreaListEntity> areaList) {
        mAreaList = areaList;
    }
}
