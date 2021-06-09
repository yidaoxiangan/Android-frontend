package com.donkingliang.photograph;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

public class VideoElementAdapter extends BaseAdapter {

    private LinkedList<VideoElement> mData;
    private Context mContext;

    public VideoElementAdapter(LinkedList<VideoElement> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.video_list_item, parent, false);
        TextView primary_id = (TextView) convertView.findViewById(R.id.task_primary_id);
        TextView task_status = (TextView) convertView.findViewById(R.id.task_status);

        primary_id.setText("Task No." + mData.get(position).getPrimary_id());
        task_status.setText("Task status: " + mData.get(position).getStatus());


        return convertView;
    }
}
