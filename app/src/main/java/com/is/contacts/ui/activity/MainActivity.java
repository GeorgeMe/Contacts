package com.is.contacts.ui.activity;


import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.RawContacts;
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
import com.is.contacts.protocol.ContactsResponse;
import com.is.contacts.uitl.DatabaseUtil;
import com.is.contacts.uitl.TimeUtil;
import com.is.ui.eventbus.EventCenter;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements ContactsView {

    @Bind(R.id.tv_contact)
    TextView tvContact;
    @Bind(R.id.btn_synchronization)
    CircularProgressButton btnSynchronization;
    @Bind(R.id.tv_info)
    TextView tvInfo;
    @Bind(R.id.tv_begin_time)
    TextView tvBeginTime;
    @Bind(R.id.tv_post_time)
    TextView tvPostTime;
    @Bind(R.id.tv_total)
    TextView tvTotal;
    private ContactsPresenterImpl contactsPresenter;
    private List<Contacts> contactses = new ArrayList<>();

    private int total = 0;
    private int time = 0;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                time = ++time;
                tvPostTime.setText("同步时间：" + TimeUtil.secToTime(time));
            } else if (msg.what == 2) {
                Bundle data = msg.getData();
                int val = data.getInt("girl");
                total = total + val;
                tvTotal.setText("已经同步完成：" + total + " 位学员");
            } else if (msg.what == 3) {
                tvTotal.setVisibility(View.VISIBLE);
                tvBeginTime.setVisibility(View.VISIBLE);
                tvPostTime.setVisibility(View.VISIBLE);

                tvInfo.setVisibility(View.INVISIBLE);
                tvBeginTime.setText("开始时间：" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
            } else if (msg.what == 4) {
                tvTotal.setVisibility(View.INVISIBLE);
                tvBeginTime.setVisibility(View.INVISIBLE);
                tvPostTime.setVisibility(View.INVISIBLE);
            }
            super.handleMessage(msg);
        }
    };
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            // 需要做的事:发送消息
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };
    private int userId;
    @Override
    protected void getBundleExtras(Bundle extras) {
        userId = extras.getInt("userId");
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewsAndEvents() {
        tvTotal.setVisibility(View.INVISIBLE);
        tvBeginTime.setVisibility(View.INVISIBLE);
        tvPostTime.setVisibility(View.INVISIBLE);
        contactsPresenter = new ContactsPresenterImpl(mContext, this);
        contactsPresenter.getContactsList(userId);
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
    public void showData(ContactsResponse contacts) {
        contactses = contacts.getData();
        //contactses = contacts;
        //tvContact.setText("系统中有 " + contacts.size() + " 位学员");
        tvContact.setText(contacts.getTipMsg());
        Log.d("contacts", contacts.getTipMsg());
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
     * 点击按钮进行授权和同步
     */
    @OnClick(R.id.btn_synchronization)
    public void onClick() {
        Acp.getInstance(this).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.READ_CONTACTS
                                , Manifest.permission.WRITE_CONTACTS
                                , Manifest.permission.SEND_SMS)
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        //取得权限执行同步方法

                        Message message = new Message();
                        message.what = 3;
                        handler.sendMessage(message);
                        timer.schedule(task, 1000, 1000);

                        syncContacts();
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        //未取得权限
                        Toast.makeText(mContext, permissions.toString() + "权限拒绝", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 分解数据： 每次执行100条，解决android.os.TransactionTooLargeException
     *
     * @param list 全部通讯录
     */
    public void onDecompose(List<Contacts> list) {

        int op = list.size();
        //  总条数对100取余  如果值不为0 先取出末尾通讯录
        if (op % 100 != 0) {
            CopyPhoneRecords(list.subList(op - (op % 100), op));
        }
        //正常执行代码
        for (int i = 0; i < (op / 100); i++) {
            CopyPhoneRecords(list.subList(i * 100, (i + 1) * 100));
        }
    }

    /**
     * 真正执行同步联系人到手机通讯录的方法
     *
     * @param list 联系人列表（在分解方法里面使用此方法）
     */
    public void CopyPhoneRecords(List<Contacts> list) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex;
        if (list == null || list.size() == 0) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            rawContactInsertIndex = ops.size();

            ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                    .withValue(RawContacts.ACCOUNT_TYPE, null)
                    .withValue(RawContacts.ACCOUNT_NAME, null)
                    .withYieldAllowed(true)
                    .build());
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                            rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(StructuredName.DISPLAY_NAME, list.get(i).getName())
                    .withYieldAllowed(true)
                    .build());
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            Phone.CONTENT_ITEM_TYPE)
                    .withValue(Phone.NUMBER, list.get(i).getPhone())
                    .withValue(Phone.TYPE, Phone.TYPE_MOBILE)
                    .withYieldAllowed(true)
                    .build());

        }
        try {
            //这里才调用的批量添加
            this.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            //完成一批  通知界面更改数据
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putInt("girl", list.size());
            msg.setData(data);
            msg.what = 2;
            handler.sendMessage(msg);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    /**
     * 添加数据到数据库
     *
     * @param time  同步时间
     * @param count 同步条数
     */

    public void onDBRecord(String time, String count) {
        DatabaseUtil dbUtil = new DatabaseUtil(mContext);
        dbUtil.open();
        dbUtil.insert(time, count);
        dbUtil.close();
    }


    /**
     * 同步联系人
     */
    public void syncContacts() {

        btnSynchronization.setClickable(false);
        btnSynchronization.setIndeterminateProgressMode(true);
        btnSynchronization.setProgress(1);
        new Thread() {
            public void run() {
                onDecompose(contactses);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 4;
                        handler.sendMessage(message);
                        tvInfo.setVisibility(View.VISIBLE);
                        btnSynchronization.setProgress(100);
                        tvInfo.setText("您的通讯录同步完成，本次总共同步："+tvTotal+" 个学员");
                        onDBRecord(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()), contactses.size() + "");
                        readyGo(SuccessActivity.class);
                    }

                });
            }
        }.start();
    }

    @Override
    public void showNo() {
        btnSynchronization.setEnabled(false);
        showToast("没有要同步的数据,按钮已禁用");
    }
}
