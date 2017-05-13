package com.aiton.zjb.signal.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/7.
 */
public class GroupNodesList implements Serializable{
    public GroupNodesList(boolean success, String message, int messageCode, List<NodeListEntity> object) {
        this.success = success;
        this.message = message;
        this.messageCode = messageCode;
        this.object = object;
    }

    /**
     * success : true
     * message : 获取成功
     * object : [{"id":1,"groupId":17,"groupSequence":1,"deviceName":"禾山路1号路口","ipAddress":"192.168.16.228","port":5435,"deviceVersion":"1.0","linkType":1,"protocolType":1,"longitude":"118.139131","latitude":"24.508191","maintainAppUserId":null},{"id":20,"groupId":17,"groupSequence":null,"deviceName":"测试节点","ipAddress":"192.168.16.228","port":5435,"deviceVersion":null,"linkType":1,"protocolType":1,"longitude":"121.10938241780629","latitude":"31.470005651679568","maintainAppUserId":null},{"id":15,"groupId":17,"groupSequence":1,"deviceName":"你们","ipAddress":"192.168.78.99","port":5687,"deviceVersion":"1","linkType":2,"protocolType":3,"longitude":"118.137808","latitude":"24.499138","maintainAppUserId":null},{"id":8,"groupId":17,"groupSequence":null,"deviceName":"打野专用信号机","ipAddress":"123.45.45.74","port":1258,"deviceVersion":"55566","linkType":1,"protocolType":null,"longitude":"118.135424","latitude":"24.50741","maintainAppUserId":null},{"id":21,"groupId":17,"groupSequence":null,"deviceName":"劳碌命","ipAddress":"192.168.16.220","port":5435,"deviceVersion":"啦啦啦","linkType":1,"protocolType":null,"longitude":"118.137298","latitude":"24.507878","maintainAppUserId":null},{"id":18,"groupId":17,"groupSequence":1,"deviceName":"鼻子长","ipAddress":"192.168.54.22","port":65325,"deviceVersion":"你","linkType":1,"protocolType":3,"longitude":"118.140258","latitude":"24.498848","maintainAppUserId":null},{"id":23,"groupId":17,"groupSequence":null,"deviceName":"投篮","ipAddress":"132.55.45.44","port":2587,"deviceVersion":"开机","linkType":1,"protocolType":null,"longitude":"118.131911","latitude":"24.506993","maintainAppUserId":null}]
     * messageCode : 0
     */

    private boolean success;
    private String message;
    private int messageCode;
    /**
     * id : 1
     * groupId : 17
     * groupSequence : 1
     * deviceName : 禾山路1号路口
     * ipAddress : 192.168.16.228
     * port : 5435
     * deviceVersion : 1.0
     * linkType : 1
     * protocolType : 1
     * longitude : 118.139131
     * latitude : 24.508191
     * maintainAppUserId : null
     */

    private List<NodeListEntity> object;

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

    public List<NodeListEntity> getObject() {
        return object;
    }

    public void setObject(List<NodeListEntity> object) {
        this.object = object;
    }

}
