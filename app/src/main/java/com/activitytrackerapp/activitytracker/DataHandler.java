package com.activitytrackerapp.activitytracker;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataHandler {

    private static final String TAG = "DataHandler";

    // Operational variables
    public static int walkingThreshold;
    public static int runningThreshold;

    // Storing data
    public static ArrayList<Step> steps = setUpStepsList();
    public static ArrayList<UserActivity> activities = new ArrayList<>();

    // Data binding adapters
    public static ActivitiesListAdapter activitiesListAdapter;
    public static StepsListAdapter stepsListAdapter;

    // Layout object for updating the current steps/activities
    public static RecyclerView.LayoutManager activitiesLayoutManager;
    public static RecyclerView.LayoutManager stepsLayoutManager;

    public static void setThresholds(int walkingThreshold, int runningThreshold) {
        DataHandler.walkingThreshold = walkingThreshold;
        DataHandler.runningThreshold = runningThreshold;
    }

    public static void setActivitiesListAdapter(ActivitiesListAdapter activitiesListAdapter) {
        DataHandler.activitiesListAdapter = activitiesListAdapter;
    }

    public static void setStepsListAdapter(StepsListAdapter stepsListAdapter) {
        DataHandler.stepsListAdapter = stepsListAdapter;
    }

    public static void setActivitiesLayoutManager(RecyclerView.LayoutManager layoutManager) {
        DataHandler.activitiesLayoutManager = layoutManager;
    }

    public static void setStepsLayoutManager(RecyclerView.LayoutManager layoutManager) {
        DataHandler.stepsLayoutManager = layoutManager;
    }

    public static void reset() {
        steps = setUpStepsList();
        activities = new ArrayList<>();
    }

    public static void processStep() {
        processStep(System.currentTimeMillis());
    }

    public static ArrayList<Step> setUpStepsList() {
        ArrayList<Step> steps = new ArrayList<>();
        steps.add(new Step(0, System.currentTimeMillis()));
        return steps;
    }

    public static void processStep(long timeStamp) {
        int indexNewStep = steps.size();
        int indexPrevStep = indexNewStep - 1;

        long currentTime = timeStamp;
        long previousTime = steps.get(indexPrevStep).timeStamp;
        int newState = determineState(currentTime,  previousTime);

        Step newStep = new Step(newState, currentTime);
        Step prevStep = steps.get(indexPrevStep);

        steps.add(newStep);
        stepsListAdapter.localNotifyItemInserted(steps.size()-1);
        stepsLayoutManager.scrollToPosition(steps.size()-1);

        if (newStep.code != prevStep.code) {
            if (!activities.isEmpty()) {
                activities.get(activities.size() - 1).endActivity(indexPrevStep, prevStep);
                activitiesListAdapter.localNotifyItemChanged(activities.size()-1);
            }
            if (newState > 0) {
                activities.add(new UserActivity(indexNewStep, newStep));
                activitiesListAdapter.localNotifyItemInserted(activities.size()-1);
                activitiesLayoutManager.scrollToPosition(activities.size()-1);
            }
        }
    }

    public static int determineState(long currentTime, long previousTime) {
        int state;
        long difference = currentTime - previousTime;
        if (difference <= runningThreshold)
            state = 1;
        else if (difference > runningThreshold && difference <= walkingThreshold)
            state = 2;
        else
            state = -1;
        Log.d(TAG,
                "determineState: time difference with last step: " +
                        Long.toString(difference) +
                        " -> State: " +
                        Integer.toString(state));
        return state;
    }

    public static void endLastActivity() {
        if (activities.size() > 0) {
            int indexLastStep = steps.size()-1;
            Step lastStep = steps.get(indexLastStep);
            activities.get(activities.size()-1).endActivity(indexLastStep, lastStep);
            activitiesListAdapter.notifyItemChanged(activities.size());
        }
    }

    public static void printDataHandlerContent() {
        String result = "---------- Steps ----------\n";
        for (Step s : steps)
            result += s + "\n";
        result += "---------- Activities ----------\n";
        for (UserActivity a : activities)
            result += a + "\n";
        System.out.println(result);
    }
}
