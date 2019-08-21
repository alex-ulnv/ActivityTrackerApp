package com.activitytrackerapp.activitytracker;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Environment;

public class App extends Application {

    // Settings
    public static final int DEFAULT_WALKING_THRESHOLD = 1000;
    public static final int DEFAULT_RUNNING_THRESHOLD = 250;
    public static String dateTimeFormat = "HH:mm:ss.SSS";
    public static String activitiesCSVFile = "Activities.csv";
    public static String stepsCSVFile = "Steps.csv";
    public static String fileDirectory =
            android.os.Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .getAbsolutePath();

    public static final String CHANNEL_ID = "sensorServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Activity Tracker Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
