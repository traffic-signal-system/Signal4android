package com.aiton.administrator.shane_library.shane.model;

/**
 * Created by Administrator on 2016/8/30.
 */
public class UsedPersonID {
    /**
     * resultcode : 200
     * reason : 成功的返回
     * result : {"area":"湖北省咸宁市崇阳县","sex":"男","birthday":"1991年10月24日","verify":""}
     * error_code : 0
     */

    private String resultcode;
    private String reason;
    /**
     * area : 湖北省咸宁市崇阳县
     * sex : 男
     * birthday : 1991年10月24日
     * verify :
     */

    private ResultEntity result;
    private int error_code;

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getResultcode() {
        return resultcode;
    }

    public String getReason() {
        return reason;
    }

    public ResultEntity getResult() {
        return result;
    }

    public int getError_code() {
        return error_code;
    }

    public static class ResultEntity {
        private String area;
        private String sex;
        private String birthday;
        private String verify;

        public void setArea(String area) {
            this.area = area;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public void setVerify(String verify) {
            this.verify = verify;
        }

        public String getArea() {
            return area;
        }

        public String getSex() {
            return sex;
        }

        public String getBirthday() {
            return birthday;
        }

        public String getVerify() {
            return verify;
        }
    }
}
