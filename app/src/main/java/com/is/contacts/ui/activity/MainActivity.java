package com.is.contacts.ui.activity;


import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.is.contacts.R;
import com.is.contacts.base.BaseActivity;
import com.is.contacts.entity.Contacts;
import com.is.contacts.mvp.presenter.ContactsPresenterImpl;
import com.is.contacts.mvp.view.ContactsView;
import com.is.contacts.uitl.DatabaseUtil;
import com.is.ui.eventbus.EventCenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements ContactsView {

    @Bind(R.id.tv_contact)
    TextView tvContact;
    @Bind(R.id.btn_synchronization)
    CircularProgressButton btnSynchronization;
    @Bind(R.id.tv_info)
    TextView tvInfo;
    private ContactsPresenterImpl contactsPresenter;
    private List<Contacts> contactses = new ArrayList<>();
    final public static int REQUEST_CODE_ASK_WRITE_CONTACTS = 123;

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
        tvContact.setText("系统中有 " + contacts.size() + " 位学员");
    }

    @Override
    public void exit() {
        contactsPresenter.exit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return true;
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
        //获取系统时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String systime = formatter.format(curDate);
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
        Insert(systime, String.valueOf(list.size()));
        Log.e(MainActivity.class.getSimpleName(), "成功啦");
    }

    /**
     * 添加数据到数据库
     *
     * @param time  同步时间
     * @param count 同步条数
     */
    public void Insert(String time, String count) {
        DatabaseUtil dbUtil = new DatabaseUtil(this);
        dbUtil.open();
        dbUtil.insert(time, count);
        Log.i("single", time);
        dbUtil.close();
    }

    /**
     * 点击按钮进行同步
     *
     * @param view
     */
    public void Synchronization(View view) {
        switch (view.getId()) {
            case R.id.btn_synchronization:
                btnSynchronization.setIndeterminateProgressMode(true);
                btnSynchronization.setProgress(1);
                new Thread() {
                    public void run() {
                        requestPermission(REQUEST_CODE_ASK_WRITE_CONTACTS, Manifest.permission.WRITE_CONTACTS, new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    BatchAddContact(contactses);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                } catch (OperationApplicationException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Contact Denied", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnSynchronization.setProgress(100);
                                tvInfo.setText("同步完成");
                                btnSynchronization.setClickable(false);
                            }

                        });
                    }
                }.start();
                readyGo(SuccessActivity.class);
                break;
            default:
                break;

        }

    }
}
