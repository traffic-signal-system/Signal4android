package com.aiton.zjb.signal.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/28.
 */
public class GroupListInfo implements Serializable{
    private List<GroupListEntity> mAllGroupList;

    public List<GroupListEntity> getAllGroupList() {
        return mAllGroupList;
    }

    public void setAllGroupList(List<GroupListEntity> allGroupList) {
        mAllGroupList = allGroupList;
    }

    public GroupListInfo(List<GroupListEntity> allGroupList) {
        mAllGroupList = allGroupList;
    }
}
