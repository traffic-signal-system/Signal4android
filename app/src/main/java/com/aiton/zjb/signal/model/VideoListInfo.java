package com.aiton.zjb.signal.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */
public class VideoListInfo implements Serializable{
    /**
     * success : true
     * message : 获取成功
     * object : [{"id":1,"nodeId":38,"servername":"偷看你","serverip":"192.168.16.111","serverport":"8435","username":"admin","password":"admin","rebarks":"uuhjj"}]
     * messageCode : 0
     */

    private boolean success;
    private String message;
    private int messageCode;
    /**
     * id : 1
     * nodeId : 38
     * servername : 偷看你
     * serverip : 192.168.16.111
     * serverport : 8435
     * username : admin
     * password : admin
     * rebarks : uuhjj
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

    public int getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(int messageCode) {
        this.messageCode = messageCode;
    }

    public List<ObjectEntity> getObject() {
        return object;
    }

    public void setObject(List<ObjectEntity> object) {
        this.object = object;
    }

    public static class ObjectEntity implements Serializable{
        private int id;
        private int nodeId;
        private String servername;
        private String serverip;
        private String serverport;
        private String username;
        private String password;
        private String rebarks;

        public ObjectEntity(int id, int nodeId, String servername, String serverip, String serverport, String username, String password, String rebarks) {
            this.id = id;
            this.nodeId = nodeId;
            this.servername = servername;
            this.serverip = serverip;
            this.serverport = serverport;
            this.username = username;
            this.password = password;
            this.rebarks = rebarks;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getNodeId() {
            return nodeId;
        }

        public void setNodeId(int nodeId) {
            this.nodeId = nodeId;
        }

        public String getServername() {
            return servername;
        }

        public void setServername(String servername) {
            this.servername = servername;
        }

        public String getServerip() {
            return serverip;
        }

        public void setServerip(String serverip) {
            this.serverip = serverip;
        }

        public String getServerport() {
            return serverport;
        }

        public void setServerport(String serverport) {
            this.serverport = serverport;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRebarks() {
            return rebarks;
        }

        public void setRebarks(String rebarks) {
            this.rebarks = rebarks;
        }
    }
}
