package com.aiton.zjb.signal.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */
public class UserInfo implements Serializable{

    /**
     * success : true
     * message : 登录成功
     * object : {"id":1,"phone":"15871105320","permissionList":[{"id":1,"permissionName":"信号管理系统"},{"id":2,"permissionName":"GPS定位系统"},{"id":3,"permissionName":"交通信息采集"},{"id":4,"permissionName":"卡口电警系统"},{"id":5,"permissionName":"诱导管理系统"},{"id":6,"permissionName":"视频监控系统"},{"id":7,"permissionName":"故障管理系统"},{"id":8,"permissionName":"GIS功能"},{"id":9,"permissionName":"系统功能"},{"id":10,"permissionName":"公共功能"},{"id":11,"permissionName":"数据共享"}],"admin":true}
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
     * id : 1
     * phone : 15871105320
     * permissionList : [{"id":1,"permissionName":"信号管理系统"},{"id":2,"permissionName":"GPS定位系统"},{"id":3,"permissionName":"交通信息采集"},{"id":4,"permissionName":"卡口电警系统"},{"id":5,"permissionName":"诱导管理系统"},{"id":6,"permissionName":"视频监控系统"},{"id":7,"permissionName":"故障管理系统"},{"id":8,"permissionName":"GIS功能"},{"id":9,"permissionName":"系统功能"},{"id":10,"permissionName":"公共功能"},{"id":11,"permissionName":"数据共享"}]
     * admin : true
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
        private String phone;
        private boolean admin;
        /**
         * id : 1
         * permissionName : 信号管理系统
         */

        private List<PermissionListEntity> permissionList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public boolean isAdmin() {
            return admin;
        }

        public void setAdmin(boolean admin) {
            this.admin = admin;
        }

        public List<PermissionListEntity> getPermissionList() {
            return permissionList;
        }

        public void setPermissionList(List<PermissionListEntity> permissionList) {
            this.permissionList = permissionList;
        }

    }
}
