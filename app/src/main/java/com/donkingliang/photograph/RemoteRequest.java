package com.donkingliang.photograph;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.FileUtils;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RemoteRequest {
    private static class RemoteURL {
        private static final String REMOTE_IP = "121.5.169.29";
        private static final String REMOTE_PORT = "8999";
        private static final String BASIC_URL = "http://" + REMOTE_IP + ":" + REMOTE_PORT;
        private static final String CREATE_URL = BASIC_URL + "/create/";
        private static final String UPLOAD_URL = BASIC_URL + "/upload/";
        private static final String QUERY_URL = BASIC_URL + "/query/";
        private static final String DOWNLOAD_URL = BASIC_URL + "/download/";

    }

    public static OkHttpClient httpClient = new OkHttpClient();

    private static String process_json_response(String responseData) {

        try {
            JSONObject jsonObject = new JSONObject(responseData);

            String success = jsonObject.optString("success");
            String task_id = jsonObject.optString("task_id");

            SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
//            cv.put("id",null);
            cv.put("task_id", task_id);
            cv.put("status", "CREATED");
            db.insert("task", null, cv);
            db.close();

            return task_id;


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String create_request() {
        try {
            Request request = new Request.Builder()
                    .url(RemoteURL.CREATE_URL)
                    .build();
            Log.d("URL", RemoteURL.CREATE_URL);
            Response response = httpClient.newCall(request).execute();

            return process_json_response(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void query_request(String task_id) {
        try {
            String query_url =RemoteURL.QUERY_URL + "?task_id=" + task_id;
                    Request request = new Request.Builder()
                    .url(query_url)
                    .build();
            Log.d("URL", query_url);
            Response response = httpClient.newCall(request).execute();
            String responseString = response.body().string();
            Log.d("QUERY_REQUEST", responseString);
            try {

                JSONObject jsonObject = new JSONObject(responseString);

                String success = jsonObject.optString("success");
                String status = jsonObject.optString("status");

                if (success.equals("success")) {
                    SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put("status", status);
                    db.update("task", cv, "task_id=?", new String[]{task_id});
                    db.close();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void download_request(Context context, String task_id) {
        String download_url = RemoteURL.DOWNLOAD_URL + "?task_id=" + task_id;

        Log.d("DOWNLOAD", "download url is " + download_url);
        DownloadUtil.get().download(context, download_url,new DownloadUtil.OnDownloadListener(){

            @Override
            public void onDownloadSuccess() {
                Log.d("DOWNLOAD", "download success!");
            }

            @Override
            public void onDownloading(int progress) {
                Log.d("DOWNLOAD", "downloading! progress: " + String.valueOf(progress) + "%");
            }

            @Override
            public void onDownloadFailed() {

                Log.d("DOWNLOAD", "download failed!");
            }
        });

    }
    public static void upload_request(Uri file_uri, String task_id, Context context) {
        File file = null;

        if (file_uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver contentResolver = context.getContentResolver();

            Cursor cursor = contentResolver.query(file_uri, null, null, null, null);
            String displayName = "tmp";
            if (cursor.moveToFirst()) {
                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }

            try {
                InputStream is = contentResolver.openInputStream(file_uri);
                File cache = new File(context.getCacheDir().getAbsolutePath(), displayName);
                FileOutputStream fos = new FileOutputStream(cache);
                FileUtils.copy(is, fos);
                file = cache;
                fos.close();
                is.close();
                Log.d("TMP","test");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .build();

        String upload_url = RemoteURL.UPLOAD_URL + "?task_id=" + task_id;
        Request request = new Request.Builder()
                .url(upload_url)
                .post(requestBody)
                .build();

        try {
            Response response = httpClient.newCall(request).execute();
            Log.d("REQEUST", "post finished, the response is: " + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
