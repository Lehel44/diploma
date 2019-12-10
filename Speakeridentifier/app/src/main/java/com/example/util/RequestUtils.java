package com.example.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.speakeridentifier.EnrollActivity;
import com.example.speakeridentifier.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class RequestUtils {

    private static final MediaType MEDIA_TYPE_WAV = MediaType.parse("audio/wave");
    private static final String FLASK_SERVER = "http://54.81.77.193:5000/";
    private Context context;

    public RequestUtils(Context context) {
        this.context = context;
    }

    public void upload(String path, String username, String password, String route) {

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", username)
                .addFormDataPart("password", password)
                .addFormDataPart("audio_file", "audio_file.wav",
                        RequestBody.create(MEDIA_TYPE_WAV, new File(path)))
                .build();

        Request request = new Request.Builder()
                .url(FLASK_SERVER + route)
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.HOURS)
                .callTimeout(1, TimeUnit.HOURS)
                .readTimeout(1, TimeUnit.HOURS)
                .writeTimeout(1, TimeUnit.HOURS)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
//                if (!response.isSuccessful()) {
//                    throw new IOException("Unexpected code " + response);
//                }
                if (response.code() == 403) {
                    Intent intent = new Intent(context, LoginActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("user_id", response.header("user_id"));
                    context.startActivity(intent);
                }
                String responseList = response.body().string();
                Log.i("OKHTTP3", responseList);
            }
        });
    }

}
