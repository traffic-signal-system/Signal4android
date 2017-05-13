package com.aiton.zjb.signal.model;

import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */
public class GetUserInfo {

    /**
     * success : true
     * message : 获取成功
     * object : [{"id":1,"phone":"15871105320","username":null,"permissionList":null,"admin":true},{"id":2,"phone":"15960298749","username":null,"permissionList":null,"admin":false}]
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
     * username : null
     * permissionList : null
     * admin : true
     */

    private List<ObjectEntity> object;

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

    public List<ObjectEntity> getObject() {
        return object;
    }

    public void setObject(List<ObjectEntity> object) {
        this.object = object;
    }

    public static class ObjectEntity {
        private int id;
        private String phone;
        private String username;
        private List<PermissionListEntity> permissionList;
        private boolean admin;

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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public List<PermissionListEntity> getPermissionList() {
            return permissionList;
        }

        public void setPermissionList(List<PermissionListEntity> permissionList) {
            this.permissionList = permissionList;
        }

        public boolean isAdmin() {
            return admin;
        }

        public void setAdmin(boolean admin) {
            this.admin = admin;
        }
    }
}
