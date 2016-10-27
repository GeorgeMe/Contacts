package com.is.contacts.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.is.contacts.R;

import java.util.List;

/**
 * Created by Administrator on 2016/10/25 0025.
 */

public class ListViewAdapter extends BaseAdapter {
    private List<String> time;
    private List<String> count;
    private LayoutInflater mInflater;

    public ListViewAdapter(Context context, List<String> time, List<String> count) {
        this.time = time;
        this.count = count;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return time.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        public TextView time;
        public TextView count;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.lv_item, null);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.count = (TextView) convertView.findViewById(R.id.count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.time.setText(time.get(position));
        holder.count.setText(count.get(position));
        return convertView;
    }
}
