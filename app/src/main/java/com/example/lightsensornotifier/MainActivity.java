package com.example.lightsensornotifier;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private TextView lightValueTextView;
    private boolean isSensorActive = false;
    private final float THRESHOLD = 10.0f;
    private float lastLightIntensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightValueTextView = findViewById(R.id.lightValueTextView);

        Button btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(v -> {
            if (!isSensorActive) {
                sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
                isSensorActive = true;
            }
        });

        Button btnStop = findViewById(R.id.btnStop);
        btnStop.setOnClickListener(v -> {
            if (isSensorActive) {
                sensorManager.unregisterListener(this);
                isSensorActive = false;
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float lightIntensity = event.values[0];
        lightValueTextView.setText("Intensitas Cahaya: " + lightIntensity + " lux");

        if (Math.abs(lightIntensity - lastLightIntensity) > THRESHOLD) {
            Toast.makeText(this, "Perubahan cahaya terdeteksi!", Toast.LENGTH_SHORT).show();
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.suara);
            mediaPlayer.start();
        }

        lastLightIntensity = lightIntensity;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this example
    }
}
