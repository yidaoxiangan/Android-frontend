package com.donkingliang.photograph;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private static final int CAMERA_REQUEST_CODE = 0x00000010;
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 0x00000012;


    private static final int VIDEO_GALLERY_REQUEST_CODE = 0x00000013;

    public static DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dbHelper = new DatabaseHelper(MainActivity.this, "test_db", null, 1);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();


        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                break;
            case VIDEO_GALLERY_REQUEST_CODE:
                String task_id = RemoteRequest.create_request();
                if (!task_id.equals("")) {
                    Uri uri = data.getData();
                    Toast.makeText(this, "Select video in" + uri.getPath().toString(), Toast.LENGTH_SHORT).show();
                    Log.d("BUTTON", "Get video from gallery, the file path is " + uri.getPath());
                    RemoteRequest.upload_request(uri, task_id, this);
                    Toast.makeText(this, "Video uploaded", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public void chooseVideo(View view) {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), VIDEO_GALLERY_REQUEST_CODE);
    }

    public void onClickVideoList(View view) {
        Intent intent = new Intent(MainActivity.this, VideoListActivity.class);
        intent.setClass(this, VideoListActivity.class);
        startActivity(intent);


    }


}



