package com.example.speakeridentifier;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private TextView username;
    private TextView distance;
    private TextView authenticated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        username = findViewById(R.id.username_value);
        distance = findViewById(R.id.distance_value);
        authenticated = findViewById(R.id.authenticated);

        final String usernameValue = getIntent().getStringExtra("user_name");
        final String distanceValue = getIntent().getStringExtra("distance");
        final boolean isAuthenticated = Boolean.valueOf(getIntent().getStringExtra("authenticated"));

        username.setText(usernameValue);
        distance.setText(distanceValue);
        authenticated.setText(isAuthenticated ? "Authentication successful" : "Authentication denied");
    }
}
