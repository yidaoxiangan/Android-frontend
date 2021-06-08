package com.donkingliang.photograph;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RemoteRequest {
    private static class RemoteURL {
        private static final String REMOTE_IP = "121.5.169.29";
        private static final String REMOTE_PORT = "8999";
        private static final String BASIC_URL = "http://" + REMOTE_IP + ":" + REMOTE_PORT;
        private static final String CREATE_URL = BASIC_URL + "/create/";
    }
    public static OkHttpClient httpClient = new OkHttpClient();

    private static void process_json_response(String responseData) {

        try{
            JSONObject jsonObject = new JSONObject(responseData);

            String success=jsonObject.optString("success");
            String task_id=jsonObject.optString("task_id");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void create_request() {
        try {
            Request request = new Request.Builder()
                    .url(RemoteURL.CREATE_URL)
                    .build();
            Log.d("URL",RemoteURL.CREATE_URL);
            Response response = httpClient.newCall(request).execute();
            Headers headers = response.headers();
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < headers.size(); i++) {
                result.append(headers.name(i) + ": " + headers.value(i) + "\n");
            }
            result.append("\n");
            process_json_response(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
