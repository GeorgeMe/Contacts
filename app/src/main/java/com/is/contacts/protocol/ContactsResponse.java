package com.is.contacts.protocol;

import com.is.contacts.entity.Contacts;

import java.util.List;

/**
 * Created by Administrator on 2016/10/14 0014.
 */

public class ContactsResponse {

    private String status;
    private String tipCode;
    private String tipMsg;
    private List<Contacts> data;

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

    public List<Contacts> getData() {
        return data;
    }

    public void setData(List<Contacts> data) {
        this.data = data;
    }


}
