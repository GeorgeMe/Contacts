package com.is.contacts.ui.adapter;

import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/10/20 0020.
 */

public class ExpandableListViewaAdapter extends BaseExpandableListAdapter {
    AppCompatActivity activity;
    private List<String> groupArray;//组列表
    private List<List<String>> childArray;//子列表

    public ExpandableListViewaAdapter(AppCompatActivity activity, List<String> groupArray, List<List<String>> childArray) {
        this.activity = activity;
        this.groupArray = groupArray;
        this.childArray = childArray;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return childArray.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        String string = childArray.get(groupPosition).get(childPosition);
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView textView = new TextView(activity);
        textView.setLayoutParams(layoutParams);

        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

        textView.setPadding(100, 0, 0, 0);
        textView.setTextSize(15);
        textView.setText(string);
        return textView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return childArray.get(groupPosition).size();
    }

    /* ----------------------------Group */
    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return getGroup(groupPosition);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return groupArray.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String string = groupArray.get(groupPosition);
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView textView = new TextView(activity);
        textView.setLayoutParams(layoutParams);

        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

        textView.setPadding(80, 0, 0, 0);
        textView.setTextSize(20);
        textView.setText(string);
        return textView;
    }


    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }
}