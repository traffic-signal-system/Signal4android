package com.aiton.zjb.signal.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/15.
 */
public class NodeAndGroupAndAreaInfo implements Serializable{

    /**
     * success : true
     * message : 获取成功
     * object : {"id":null,"areaList":[{"id":1,"name":"枋湖片区","groupList":[{"id":1,"groupId":1,"areaId":1,"groupName":"禾山路","groupEnable":1,"nodeList":[{"id":1,"groupId":1,"groupSequence":null,"deviceName":"禾山路1号路口","ipAddress":"192.168.16.228","port":5435,"deviceVersion":"1.0","linkType":1,"protocolType":1,"longitude":"121.10942168167412","latitude":"31.469953945763848","maintainAppUserId":2},{"id":6,"groupId":1,"groupSequence":null,"deviceName":"上单专用红绿灯","ipAddress":"192.168.2.254","port":2554,"deviceVersion":"345566","linkType":null,"protocolType":3,"longitude":"118.142025","latitude":"24.508506","maintainAppUserId":1}]}]},{"id":2,"name":"英雄联盟","groupList":[]},{"id":3,"name":"召唤师峡谷","groupList":[]},{"id":-1,"name":"其他域","groupList":[{"id":-1,"groupId":null,"areaId":null,"groupName":"其他节点","groupEnable":null,"nodeList":[{"id":2,"groupId":null,"groupSequence":null,"deviceName":"禾山路2号路口","ipAddress":"192.168.16.229","port":5435,"deviceVersion":"1.0","linkType":1,"protocolType":3,"longitude":"121.1159713959532","latitude":"31.473281200617613","maintainAppUserId":2}]}]}]}
     */

    private boolean success;
    private String message;
    private int messageCode;

    public int getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(int messageCode) {
        this.messageCode = messageCode;
    }
    /**
     * id : null
     * areaList : [{"id":1,"name":"枋湖片区","groupList":[{"id":1,"groupId":1,"areaId":1,"groupName":"禾山路","groupEnable":1,"nodeList":[{"id":1,"groupId":1,"groupSequence":null,"deviceName":"禾山路1号路口","ipAddress":"192.168.16.228","port":5435,"deviceVersion":"1.0","linkType":1,"protocolType":1,"longitude":"121.10942168167412","latitude":"31.469953945763848","maintainAppUserId":2},{"id":6,"groupId":1,"groupSequence":null,"deviceName":"上单专用红绿灯","ipAddress":"192.168.2.254","port":2554,"deviceVersion":"345566","linkType":null,"protocolType":3,"longitude":"118.142025","latitude":"24.508506","maintainAppUserId":1}]}]},{"id":2,"name":"英雄联盟","groupList":[]},{"id":3,"name":"召唤师峡谷","groupList":[]},{"id":-1,"name":"其他域","groupList":[{"id":-1,"groupId":null,"areaId":null,"groupName":"其他节点","groupEnable":null,"nodeList":[{"id":2,"groupId":null,"groupSequence":null,"deviceName":"禾山路2号路口","ipAddress":"192.168.16.229","port":5435,"deviceVersion":"1.0","linkType":1,"protocolType":3,"longitude":"121.1159713959532","latitude":"31.473281200617613","maintainAppUserId":2}]}]}]
     */

    private ObjectEntity object;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ObjectEntity getObject() {
        return object;
    }

    public void setObject(ObjectEntity object) {
        this.object = object;
    }

    public static class ObjectEntity implements Serializable{
        private int id;
        /**
         * id : 1
         * name : 枋湖片区
         * groupList : [{"id":1,"groupId":1,"areaId":1,"groupName":"禾山路","groupEnable":1,"nodeList":[{"id":1,"groupId":1,"groupSequence":null,"deviceName":"禾山路1号路口","ipAddress":"192.168.16.228","port":5435,"deviceVersion":"1.0","linkType":1,"protocolType":1,"longitude":"121.10942168167412","latitude":"31.469953945763848","maintainAppUserId":2},{"id":6,"groupId":1,"groupSequence":null,"deviceName":"上单专用红绿灯","ipAddress":"192.168.2.254","port":2554,"deviceVersion":"345566","linkType":null,"protocolType":3,"longitude":"118.142025","latitude":"24.508506","maintainAppUserId":1}]}]
         */

        private List<AreaListEntity> areaList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<AreaListEntity> getAreaList() {
            return areaList;
        }

        public void setAreaList(List<AreaListEntity> areaList) {
            this.areaList = areaList;
        }

        public static class AreaListEntity implements Serializable{
            private int id;
            private String name;
            /**
             * id : 1
             * groupId : 1
             * areaId : 1
             * groupName : 禾山路
             * groupEnable : 1
             * nodeList : [{"id":1,"groupId":1,"groupSequence":null,"deviceName":"禾山路1号路口","ipAddress":"192.168.16.228","port":5435,"deviceVersion":"1.0","linkType":1,"protocolType":1,"longitude":"121.10942168167412","latitude":"31.469953945763848","maintainAppUserId":2},{"id":6,"groupId":1,"groupSequence":null,"deviceName":"上单专用红绿灯","ipAddress":"192.168.2.254","port":2554,"deviceVersion":"345566","linkType":null,"protocolType":3,"longitude":"118.142025","latitude":"24.508506","maintainAppUserId":1}]
             */

            private List<GroupListEntity> groupList;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<GroupListEntity> getGroupList() {
                return groupList;
            }

            public void setGroupList(List<GroupListEntity> groupList) {
                this.groupList = groupList;
            }
        }
    }
}
