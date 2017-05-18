package com.is.contacts.mvp.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.is.common.DeviceUtils;
import com.is.contacts.mvp.interactor.ContactsInteractorImpl;
import com.is.contacts.mvp.listener.BaseSingleLoadedListener;
import com.is.contacts.mvp.view.ContactsView;
import com.is.contacts.protocol.ContactsResponse;

import org.json.JSONObject;

/**
 * Created by MVPHelper on 2016/10/13
 */

public class ContactsPresenterImpl implements BaseSingleLoadedListener<ContactsResponse> {
    private Context mContext = null;
    private ContactsView contactsView;
    private ContactsInteractorImpl contactsInteractor;

    public ContactsPresenterImpl(Context mContext, ContactsView contactsView) {
        this.mContext = mContext;
        this.contactsView = contactsView;
        contactsInteractor = new ContactsInteractorImpl(mContext, this);
    }

    public void getContactsList(int id) {
        contactsView.showLoading("正在请求数据......");
        JSONObject json = new JSONObject();
        String mac = DeviceUtils.getDeviceID(mContext);
        try{
            json.put("userid",id);
            json.put("mac",mac);
        }catch (Exception e){
            e.printStackTrace();
        }
        contactsInteractor.getCommonListData(json);
    }
    public void exit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("确定要退出吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    @Override
    public void onSuccess(ContactsResponse data) {
        if (data.getData() != null && "success".equals(data.getStatus())) {
            contactsView.showData(data);
        }
        if ("NO".equals(data.getTipCode())){
            contactsView.showNo();
        }
        contactsView.hideLoading();
    }

    @Override
    public void onFailure(String msg) {
        contactsView.showToast(msg);
        contactsView.hideLoading();
    }
}