package com.is.contacts.ui.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.is.contacts.R;
import com.is.contacts.base.BaseActivity;
import com.is.contacts.ui.adapter.ExpandableListViewaAdapter;
import com.is.contacts.uitl.DatabaseUtil;
import com.is.ui.eventbus.EventCenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class TestActivity extends BaseActivity {
    @Bind(R.id.expandableListView)
    ExpandableListView expandableListView;
    @Bind(R.id.activity_test)
    LinearLayout activityTest;
    private List<String> groupArray;//组列表
    private List<List<String>> childArray;//子列表
    List<String> childItem;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_test;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    protected void initViewsAndEvents() {
        mContext = TestActivity.this;
        groupArray = new ArrayList<String>();
        childArray = new ArrayList<List<String>>();
        childItem = new ArrayList<String>();
        query();
        initdate();
        expandableListView.setAdapter(new ExpandableListViewaAdapter(TestActivity.this, groupArray, childArray));
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

    private void initdate() {
        groupArray.add("最近同步记录");
        childArray.add(childItem);
    }

    public void query() {
        DatabaseUtil dbUtil = new DatabaseUtil(this);
        dbUtil.open();
        Cursor cursor = dbUtil.fetchAll();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String count = cursor.getString(cursor.getColumnIndex("count"));
                childItem.add(time + count);
            }
        }
        dbUtil.close();
    }
}
