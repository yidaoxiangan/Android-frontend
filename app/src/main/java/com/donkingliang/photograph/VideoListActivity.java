package com.donkingliang.photograph;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

public class VideoListActivity extends Activity {
    private Context mContext;
    private List<VideoElement> mData;
    private VideoElementAdapter mAdapter;
    private ListView list_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        fetchData();

    }
    private void renewRemoteData() {

        SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * from task", null);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            String task_id = c.getString(c.getColumnIndex("task_id"));
            RemoteRequest.query_request(task_id);
            c.moveToNext();
        }

    }

    private void fetchData() {
        renewRemoteData();
        mContext = VideoListActivity.this;
        list_video = (ListView) findViewById(R.id.video_list_view);

        mData = new LinkedList<>();

        SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * from task", null);
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) {
            int id = c.getInt(c.getColumnIndex("id"));
            String task_id = c.getString(c.getColumnIndex("task_id"));
            String status = c.getString(c.getColumnIndex("status"));

            mData.add(new VideoElement(id, task_id, status));

            Log.e("DATABASE", String.valueOf(id) + " " + task_id + " " + status);
            c.moveToNext();
        }
        mAdapter = new VideoElementAdapter((LinkedList<VideoElement>) mData, mContext);
        list_video.setAdapter(mAdapter);
    }
}
