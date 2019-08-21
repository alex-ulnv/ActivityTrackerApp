package com.activitytrackerapp.activitytracker;

import android.app.IntentService;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class SensorService extends IntentService implements SensorEventListener {
    
    public static final String TAG = "SensorService";
    public static boolean sensorRunning = false;

    SensorManager sm;
    Sensor stepDetector;
    boolean train;

    private PowerManager.WakeLock wakeLock;

    public SensorService() {
        super("SensorService");
        setIntentRedelivery(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                "ActivityTracker:Wakelock"
        );
        wakeLock.acquire();
        Log.d(TAG, "Wakelock acquired");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                    .setContentTitle("Activity Tracker")
                    .setContentText("Recording your steps and activities ..")
                    .setSmallIcon(R.drawable.ic_sensor_running)
                    .build();

            startForeground(1, notification);
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: Intent Received");
        // decide training or not
        this.train = intent.getBooleanExtra("train", false);
        registerSensor();
    }

    private void registerSensor() {
        Log.d(TAG, "registerSensor: Registering Sensor");
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepDetector = sm.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sm.registerListener(this, stepDetector, SensorManager.SENSOR_DELAY_NORMAL);
        sensorRunning = true;
    }

    private void unregisterSensor() {
        Log.d(TAG, "unregisterSensor: Unregistering sensor");
        sm.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorRunning) {
            Log.d(TAG, "onSensorChanged: Sensor changed.");
            // if training mode
            if (train) {
                TrainingDataHandler.addTimeStamp(System.currentTimeMillis());
                Log.d(TAG, "onSensorChanged: Current test set size " + TrainingDataHandler.timeStamps.size());
                if (TrainingDataHandler.testSetIsReady())
                    sensorRunning = false;
            }
            else {
                DataHandler.processStep(System.currentTimeMillis());
            }
        }
        else
            unregisterSensor();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
