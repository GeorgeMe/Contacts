package com.is.contacts;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.is.contacts.base.BaseActivity;
import com.is.contacts.presenter.ContactsPresenterImpl;
import com.is.contacts.view.ContactsView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements ContactsView {
    @Bind(R.id.btn_synchronization)
    Button btnSynchronization;
    @Bind(R.id.activity_main)
    RelativeLayout activityMain;
    @Bind(R.id.tv_contact)
    TextView tvContact;
    private Context mContext;
    private ContactsPresenterImpl contactsPresenter;
    private List<Contacts> contactses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        contactsPresenter = new ContactsPresenterImpl(this);
        contactsPresenter.loadData();
    }

    /**
     * 批量添加通讯录
     *
     * @throws OperationApplicationException
     * @throws RemoteException
     */
    public void BatchAddContact(List<Contacts> list)
            throws RemoteException, OperationApplicationException {
        Log.e(MainActivity.class.getSimpleName(), new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = 0;
        for (Contacts contact : list) {
            rawContactInsertIndex = ops.size(); // 有了它才能给真正的实现批量添加

            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .withYieldAllowed(true).build());

            // 添加姓名
            ops.add(ContentProviderOperation
                    .newInsert(
                            ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID,
                            rawContactInsertIndex)
                    .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName())
                    .withYieldAllowed(true).build());
            // 添加号码
            ops.add(ContentProviderOperation
                    .newInsert(
                            ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID,
                            rawContactInsertIndex)
                    .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getPhone())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.LABEL, "").withYieldAllowed(true).build());
        }
        if (ops != null) {
            // 真正添加
            ContentProviderResult[] results = mContext.getContentResolver()
                    .applyBatch(ContactsContract.AUTHORITY, ops);

            // for (ContentProviderResult result : results) {
            // GlobalConstants
            // .PrintLog_D("[GlobalVariables->]BatchAddContact "
            // + result.uri.toString());
            // }
        }
        Log.e(MainActivity.class.getSimpleName(), new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
        Log.e(MainActivity.class.getSimpleName(), "成功啦");
    }

    public void Synchronization(View view) {
        showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BatchAddContact(contactses);
                    hideProgress();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void showData(List<Contacts> contacts) {
        contactses = contacts;
        tvContact.setText(contacts.size() + "");
    }

    @Override
    public void showProgress() {
        showLoading("同步中,请稍后...");
    }

    @Override
    public void hideProgress() {
        hideLoading();
    }
}
