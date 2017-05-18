package com.is.contacts.ui.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.is.contacts.R;
import com.is.contacts.base.BaseActivity;
import com.is.contacts.ui.adapter.ListViewAdapter;
import com.is.contacts.uitl.DatabaseUtil;
import com.is.ui.eventbus.EventCenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class SuccessActivity extends BaseActivity {

    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.activity_test)
    LinearLayout activityTest;
    List<String> timeItem;
    List<String> countItem;
    LayoutInflater layoutInflater;
    View header;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_success;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    protected void initViewsAndEvents() {
        layoutInflater = LayoutInflater.from(mContext);
        timeItem = new ArrayList<String>();
        countItem = new ArrayList<String>();
        header = layoutInflater.inflate(R.layout.listview_head, null);
        query();
        if (listView.getHeaderViewsCount() == 0) {
            listView.addHeaderView(header);
        }
        listView.setAdapter(new ListViewAdapter(mContext, timeItem, countItem));
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

    public void query() {
        DatabaseUtil dbUtil = new DatabaseUtil(mContext);
        dbUtil.open();
        Cursor cursor = dbUtil.fetchAll();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String count = cursor.getString(cursor.getColumnIndex("count"));
                timeItem.add(time);
                countItem.add(count);
            }
        }
        dbUtil.close();
    }
}
