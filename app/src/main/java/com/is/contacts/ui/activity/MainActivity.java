package com.is.contacts.ui.activity;


import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.is.contacts.R;
import com.is.contacts.base.BaseActivity;
import com.is.contacts.entity.Contacts;
import com.is.contacts.mvp.presenter.ContactsPresenterImpl;
import com.is.contacts.mvp.view.ContactsView;
import com.is.ui.eventbus.EventCenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements ContactsView {

    @Bind(R.id.btn_synchronization)
    CircularProgressButton btnSynchronization;
    @Bind(R.id.tv_contact)
    TextView tvContact;
    @Bind(R.id.activity_main)
    RelativeLayout activityMain;
    private ContactsPresenterImpl contactsPresenter;
    private List<Contacts> contactses;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewsAndEvents() {
        contactsPresenter = new ContactsPresenterImpl(mContext, this);
        contactsPresenter.getContactsList();
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }

    @Override
    public void showData(List<Contacts> contacts) {
        contactses = contacts;
        tvContact.setText(contacts.size() + "");
    }


    /**
     * 批量添加通讯录
     *
     * @throws OperationApplicationException
     * @throws RemoteException
     */
    public void BatchAddContact(List<Contacts> list)
            throws RemoteException, OperationApplicationException {
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
        Log.e(MainActivity.class.getSimpleName(), "成功啦");
    }

    public void Synchronization(View view) {
        switch (view.getId()) {
            case R.id.btn_synchronization:
                btnSynchronization.setIndeterminateProgressMode(true);
                Log.i("single", btnSynchronization.getProgress() + "");
                btnSynchronization.setProgress(50);
                Log.i("single", btnSynchronization.getProgress() + "");
                new Thread() {
                    public void run() {
                        try {
                            BatchAddContact(contactses);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        } catch (OperationApplicationException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnSynchronization.setProgress(0);
                            }

                        });
                    }
                }.start();
                break;
            default:
                break;

        }

    }
}
