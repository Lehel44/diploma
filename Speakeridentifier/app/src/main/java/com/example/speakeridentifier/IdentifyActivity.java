package com.example.speakeridentifier;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.util.RequestUtils;
import com.example.util.WavRecorder;

public class IdentifyActivity extends AppCompatActivity {

    private WavRecorder wavRecorder;
    private Button identifyButton;
    private TextView info;
    private static final String OUTPUT_FILE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.wav";
    private static final String ROUTE = "identify";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify);

        identifyButton = findViewById(R.id.button_start_identify);
        info = findViewById(R.id.identify_info);

        wavRecorder = new WavRecorder(OUTPUT_FILE);
    }

    public void identify(View view) {
        identifyButton.setEnabled(false);

        final Handler handler = new Handler();

        Runnable textUpdate = new Runnable() {
            private int sec = 3;
            public void run() {
                info.setText("Start talking in " + sec + "...");
                if (sec == 0) {
                    info.setText("Recording...");
                }
                if (sec == -1) {
                    info.setVisibility(View.INVISIBLE);
                }
                sec--;
            }
        };
        handler.postDelayed(textUpdate, 2000);
        handler.postDelayed(textUpdate, 4000);
        handler.postDelayed(textUpdate, 6000);
        handler.postDelayed(textUpdate, 8000);
        handler.postDelayed(textUpdate, 12000);

        Runnable startAudioRecorder = new Runnable() {
            @Override
            public void run() {
                wavRecorder.startRecording();
                Toast.makeText(getApplicationContext(), "Recording started.", Toast.LENGTH_LONG).show();

            }
        };

        handler.postDelayed(startAudioRecorder, 8000);

        Runnable stopAudioRecorder = new Runnable() {
            @Override
            public void run() {
                wavRecorder.stopRecording();
                Toast.makeText(getApplicationContext(), "Recording stopped.", Toast.LENGTH_LONG).show();
                // Start upload wav.
                new RequestUtils(getApplicationContext()).upload(OUTPUT_FILE, "", "", "identify");
            }
        };

        handler.postDelayed(stopAudioRecorder, 12000);
    }
}
