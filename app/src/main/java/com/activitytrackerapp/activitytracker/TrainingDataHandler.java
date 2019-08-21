package com.activitytrackerapp.activitytracker;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class TrainingDataHandler {

    public static final String TAG = "TrainingDataHandler";

    public static ArrayList<Long> timeStamps;
    public static int testSetSize = 20;

    public static void reset() {
        timeStamps = new ArrayList<>();
    }

    public static void addTimeStamp(long timeStamp) {
        timeStamps.add(timeStamp);
        if (timeStamps.size() == testSetSize/2) {
            ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
            toneGen1.startTone(ToneGenerator.TONE_PROP_PROMPT,1500);
        }
    }

    public static int getWalkingThreshold() {
        Log.d(TAG, "getWalkingThreshold: Raw results: " + timeStamps.toString());
        long [] walkingTimeStamps = new long[timeStamps.size() / 2];
        for (int i = 0; i < timeStamps.size() / 2; i++) {
            walkingTimeStamps[i] = timeStamps.get(i);
        }
        // find max
        Log.d(TAG, "getWalkingThreshold: Finding max for: " + Arrays.toString(walkingTimeStamps));
        Long max = 0l;
        long difference;
        for (int i = 1; i < walkingTimeStamps.length; i++) {
            difference =  walkingTimeStamps[i] - walkingTimeStamps[i-1];
            if (difference > max)
                max = difference;
        }
        return max.intValue();
    }

    public static int getRunningThreshold() {
        int subArraySize = timeStamps.size() - (timeStamps.size() / 2);
        long [] runningTimeStamps = new long[subArraySize];
        for (int i = 0; i < subArraySize; i++) {
            runningTimeStamps[i] = timeStamps.get(i+subArraySize);
        }
        // find min
        Long min = timeStamps.get(0);
        long difference;
        for (int i = 1; i < runningTimeStamps.length; i++) {
            difference =  runningTimeStamps[i] - runningTimeStamps[i-1];
            if (difference < min)
                min = difference;
        }
        return min.intValue();
    }

    public static boolean testSetIsReady() {
        if (timeStamps.size() >= testSetSize)
            return true;
        else
            return false;
    }

}
