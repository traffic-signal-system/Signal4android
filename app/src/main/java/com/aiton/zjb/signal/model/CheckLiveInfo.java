package com.aiton.zjb.signal.model;

/**
 * Created by Administrator on 2016/11/14.
 */
public class CheckLiveInfo {
    /**
     * success : true
     * message : 获取成功
     * object : {"id":"1","androidVersionForApp":"1","androidVersionForMaintainApp":"1","iosversionForMaintainApp":"1","iosversionForApp":"1"}
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
     * androidVersionForApp : 1
     * androidVersionForMaintainApp : 1
     * iosversionForMaintainApp : 1
     * iosversionForApp : 1
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

    public static class ObjectEntity {
        private int id;
        private int androidVersionForApp;
        private int androidVersionForMaintainApp;
        private int iosversionForMaintainApp;
        private int iosversionForApp;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAndroidVersionForApp() {
            return androidVersionForApp;
        }

        public void setAndroidVersionForApp(int androidVersionForApp) {
            this.androidVersionForApp = androidVersionForApp;
        }

        public int getAndroidVersionForMaintainApp() {
            return androidVersionForMaintainApp;
        }

        public void setAndroidVersionForMaintainApp(int androidVersionForMaintainApp) {
            this.androidVersionForMaintainApp = androidVersionForMaintainApp;
        }

        public int getIosversionForMaintainApp() {
            return iosversionForMaintainApp;
        }

        public void setIosversionForMaintainApp(int iosversionForMaintainApp) {
            this.iosversionForMaintainApp = iosversionForMaintainApp;
        }

        public int getIosversionForApp() {
            return iosversionForApp;
        }

        public void setIosversionForApp(int iosversionForApp) {
            this.iosversionForApp = iosversionForApp;
        }
    }
}
