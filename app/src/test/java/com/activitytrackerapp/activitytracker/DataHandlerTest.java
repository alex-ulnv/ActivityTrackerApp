package com.activitytrackerapp.activitytracker;

import androidx.recyclerview.widget.LinearLayoutManager;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

public class DataHandlerTest {


    LinearLayoutManager linearLayoutManager;
    ActivitiesListAdapter activitiesListAdapter;
    StepsListAdapter stepsListAdapter;


    @Before
    public void setUp() {

        linearLayoutManager = mock(LinearLayoutManager.class);
        activitiesListAdapter = mock(ActivitiesListAdapter.class);
        stepsListAdapter = mock(StepsListAdapter.class);

        DataHandler.reset();

        DataHandler.setActivitiesLayoutManager(linearLayoutManager);
        DataHandler.setActivitiesListAdapter(activitiesListAdapter);
        DataHandler.setStepsLayoutManager(linearLayoutManager);
        DataHandler.setStepsListAdapter(stepsListAdapter);

        doNothing().when(activitiesListAdapter).localNotifyItemInserted(anyInt());
        doNothing().when(stepsListAdapter).localNotifyItemInserted(anyInt());

        DataHandler.runningThreshold = 250;
        DataHandler.walkingThreshold = 500;

    }


    @Test
    public void testProcessStep() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i=0; i<5; i++) {
            DataHandler.processStep();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i=0; i<5; i++) {
            DataHandler.processStep();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int indexLastActivity = DataHandler.activities.size()-1;
        int indexLastStep = DataHandler.steps.size()-1;
        UserActivity lastUserActivity = DataHandler.activities.get(indexLastActivity);
        Step lastStep = DataHandler.steps.get(indexLastStep);

        lastUserActivity.endActivity(indexLastStep, lastStep);

        assertEquals(2, DataHandler.activities.size());
        assertEquals(11, DataHandler.steps.size());

    }


}