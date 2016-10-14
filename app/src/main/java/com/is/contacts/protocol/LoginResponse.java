package com.is.contacts.protocol;

/**
 * Created by Administrator on 2016/10/14 0014.
 */

public class LoginResponse {

    /**
     * status : success
     * tipCode : 200
     * tipMsg : 55
     * data : 登录成功
     */

    private String status;
    private String tipCode;
    private String tipMsg;
    private String data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTipCode() {
        return tipCode;
    }

    public void setTipCode(String tipCode) {
        this.tipCode = tipCode;
    }

    public String getTipMsg() {
        return tipMsg;
    }

    public void setTipMsg(String tipMsg) {
        this.tipMsg = tipMsg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
