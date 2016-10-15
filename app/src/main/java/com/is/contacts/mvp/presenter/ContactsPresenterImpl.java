package com.is.contacts.mvp.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.is.contacts.mvp.interactor.ContactsInteractorImpl;
import com.is.contacts.mvp.listener.BaseSingleLoadedListener;
import com.is.contacts.mvp.view.ContactsView;
import com.is.contacts.protocol.ContactsResponse;

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

    public void getContactsList() {
        contactsInteractor.getCommonListData(null);
    }
    public void exit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("确定要退出吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    @Override
    public void onSuccess(ContactsResponse data) {
        if (data.getData() != null) {
            contactsView.showData(data.getData());
        }
    }

    @Override
    public void onFailure(String msg) {

    }
}