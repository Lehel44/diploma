package com.example.speakeridentifier;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://104.248.130.20:5000/";
    private static final String ROUTE = "authenticate";

    private EditText password;
    private TextView userIdView;
    private TextView userNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        password = findViewById(R.id.password);
        userIdView = findViewById(R.id.user_id);
        userNameView = findViewById(R.id.user_name);
    }

    public void checkPassword(View view) {
        final String userId = getIntent().getStringExtra("user_id");
        final String distance = getIntent().getStringExtra("distance");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userId)
                .addFormDataPart("distance", distance)
                .addFormDataPart("password", password.getText().toString())
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + ROUTE)
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

                //String responseBody = response.body().string();
                //Log.i("OKHTTP3", responseBody);

                //String userName = getStringByKey(responseBody, "user_name");

                //backgroundThreadUpdateTexts(getApplicationContext(), userIdView, userNameView, userId, userName);
                if (response.code() == 202) {
                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    intent.putExtra("user_name", response.header("user_name"));
                    intent.putExtra("distance", response.header("distance"));
                    intent.putExtra("authenticated", "true");
                    startActivity(intent);
                } else if (response.code() == 403) {
                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    intent.putExtra("user_name", response.header("user_name"));
                    intent.putExtra("distance", response.header("distance"));
                    intent.putExtra("authenticated", "false");
                    startActivity(intent);
                }

            }
        });
    }

    private String getStringByKey(String responseBody, String key) {
        JSONObject json;
        String userName = null;
        try {
            json = new JSONObject(responseBody);
            userName = json.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userName;
    }

    public static void backgroundThreadUpdateTexts(final Context context, final TextView userIdView,
                                                   final TextView userNameView, final String userId,
                                                   final String userName) {
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, "Login successful!", Toast.LENGTH_LONG).show();
                    userIdView.setText(userId);
                    userNameView.setText(userName);
                    userIdView.setVisibility(View.VISIBLE);
                    userNameView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

}
