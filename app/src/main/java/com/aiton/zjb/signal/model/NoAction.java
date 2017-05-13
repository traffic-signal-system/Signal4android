package com.aiton.zjb.signal.model;

/**
 * Created by Administrator on 2016/12/20.
 */
public class NoAction {
    /**
     * success : true
     * message : null
     * object : null
     * /**
     * 消息状态码：0 正常；1 参数缺失（用户id为空值或设备id为空）；2 参数缺失（ 对象id为空）；
     * 3 登录已过期（在别的设备上重复登录）；4 用户不存在；5 对象不存在；6 其他错误
     */

    private boolean success;
    private String message;
    private Object object;
    private int messageCode;

    public int getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(int messageCode) {
        this.messageCode = messageCode;
    }

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

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
