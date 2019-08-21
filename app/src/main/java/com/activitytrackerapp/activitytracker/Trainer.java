package com.activitytrackerapp.activitytracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class Trainer{

    Context c;
    public int runningThreshold;
    public int walkingThreshold;
    TextView resultTextView;
    ProgressBar trainingProgressBar;
    Button startBttn, updateThresholdsBttn;

    public static final String SHARED_PREF = "sharedPrefs";
    public static final String RUNNING_THRESHOLD = "runningThreshold";
    public static final String WALKING_THRESHOLD = "walkingThreshold";


    public ArrayList<Long> timeStamps;

    public Trainer(Context c) {
        this.c = c;
    }

    public void reset() {
        timeStamps = new ArrayList<>();
        runningThreshold = -1;
        walkingThreshold = -1;
    }

    public void train() {
        startBttn = ((Activity) c).findViewById(R.id.trainStartBttn);
        updateThresholdsBttn = ((Activity) c).findViewById(R.id.updateThresholdBttn);
        startBttn.setEnabled(false);
        final Animation myAnim = AnimationUtils.loadAnimation(c, R.anim.scale_in_out);

        resultTextView = ((Activity) c).findViewById(R.id.trainResultsTextView);
        resultTextView.setText("Training ..");

        trainingProgressBar = ((Activity) c).findViewById(R.id.trainProgressBar);
        trainingProgressBar.setVisibility(View.VISIBLE);
        trainingProgressBar.setProgress(0);

        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);

        toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP,200);
        SystemClock.sleep(1000);
        toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP,200);
        SystemClock.sleep(1000);
        toneGen1.startTone(ToneGenerator.TONE_PROP_PROMPT,1500);

        resultTextView.setVisibility(View.VISIBLE);
        resultTextView.startAnimation(myAnim);

        TrainingDataHandler.reset();
        Intent serviceIntent = new Intent(c, SensorService.class);
        serviceIntent.putExtra("train", true);

        Toast.makeText(c, "Training started!", Toast.LENGTH_SHORT).show();
        ContextCompat.startForegroundService(c, serviceIntent);

        final Handler progressHandler = new Handler();
        final Runnable updateProgress = new Runnable() {
            @Override
            public void run() {
                float progress =
                        100 * (float) TrainingDataHandler.timeStamps.size() / TrainingDataHandler.testSetSize;
                trainingProgressBar.setProgress(Math.round(progress));
            }
        };

        final Handler handler = new Handler();
        final Runnable notifyComplete = new Runnable() {
            @Override
            public void run() {
                c.stopService(new Intent(c, SensorService.class));
                walkingThreshold = TrainingDataHandler.getWalkingThreshold();
                runningThreshold = TrainingDataHandler.getRunningThreshold();
                resultTextView.setText(
                        String.format(
                                Locale.getDefault(),
                                "%s\nWalking: %d | Running: %d",
                                c.getResources().getString(R.string.your_new_threshold),
                                walkingThreshold,
                                runningThreshold
                        ));
                resultTextView.clearAnimation();
                startBttn.setText("Restart");
                myAnim.cancel();
                myAnim.reset();
                trainingProgressBar.setVisibility(View.GONE);
                startBttn.setEnabled(true);
                updateThresholdsBttn.setVisibility(View.VISIBLE);

            }
        };

        Thread thread = new Thread(new Runnable() {
            public void run() {
                while (!TrainingDataHandler.testSetIsReady()) {
                    progressHandler.post(updateProgress);
                    SystemClock.sleep(500);
                }
                handler.post(notifyComplete);
            }
        });

        thread.start();

    }

    public void updateThresholds() {
        SharedPreferences sharedPreferences = c.getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(WALKING_THRESHOLD, walkingThreshold);
        editor.putInt(RUNNING_THRESHOLD, runningThreshold);
        editor.commit();
        DataHandler.walkingThreshold = walkingThreshold;
        DataHandler.runningThreshold = runningThreshold;
        Toast.makeText(
                c,
                String.format(
                        Locale.getDefault(),
                        "Updated: (Walking: %d, Running: %d)",
                        walkingThreshold,
                        runningThreshold
                ),
                Toast.LENGTH_SHORT
        ).show();
    }

}
