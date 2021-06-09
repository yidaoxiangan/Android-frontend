package com.donkingliang.photograph;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        list_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String task_id = mData.get(i).getTask_id();
                String status = mData.get(i).getStatus();

                if (status.equals("UPLOADED")) {
                    Toast.makeText(VideoListActivity.this, "Start downloading Task No." + (i + 1), Toast.LENGTH_SHORT).show();
                    RemoteRequest.download_request(VideoListActivity.this, task_id);
                } else {
                    Toast.makeText(VideoListActivity.this, "Video not uploaded yet.", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
    public void refresh(View view) {
        Log.d("BUTTON", "refresh clicked");
        Toast.makeText(VideoListActivity.this, "Refresh data, please wait."  , Toast.LENGTH_SHORT).show();
        fetchData();
        Toast.makeText(VideoListActivity.this, "Refresh finished!"  , Toast.LENGTH_SHORT).show();

    }
}
