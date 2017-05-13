package com.aiton.zjb.signal.model;

import java.io.Serializable;
import java.util.List;

public class GroupListEntity implements Serializable{
    private int id;
    private int areaId;
    private String groupName;
    private int groupEnable;
    /**
     * id : 1
     * groupId : 1
     * groupSequence : null
     * deviceName : 禾山路1号路口
     * ipAddress : 192.168.16.228
     * port : 5435
     * deviceVersion : 1.0
     * linkType : 1
     * protocolType : 1
     * longitude : 121.10942168167412
     * latitude : 31.469953945763848
     * maintainAppUserId : 2
     */

    private List<NodeListEntity> nodeList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupEnable() {
        return groupEnable;
    }

    public void setGroupEnable(int groupEnable) {
        this.groupEnable = groupEnable;
    }

    public List<NodeListEntity> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<NodeListEntity> nodeList) {
        this.nodeList = nodeList;
    }

}