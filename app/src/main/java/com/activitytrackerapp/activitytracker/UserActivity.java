package com.activitytrackerapp.activitytracker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserActivity {

    int indexStartStep, indexEndStep;
    int code;
    long startTime = 0, endTime = 0;
    boolean finished = false;

    public UserActivity(int indexStartStep, int indexEndStep, Step startStep, Step endStep) {
        this.indexStartStep = indexStartStep;
        this.indexEndStep =  indexEndStep;
        this.startTime = startStep.timeStamp;
        this.endTime = endStep.timeStamp;
        this.code = startStep.code;
    }

    public UserActivity(int indexStartStep, Step startStep) {
        this.indexStartStep = indexStartStep;
        this.startTime = startStep.timeStamp;
        this.code = startStep.code;
    }

    public void endActivity(int indexEndStep, Step endStep) {
        if (!this.finished) {
            this.indexEndStep = indexEndStep;
            this.endTime = endStep.timeStamp;
            this.finished = true;
        }
    }

    public int getNumberOfSteps() {
        if (finished)
            return 1 + indexEndStep - indexStartStep;
        else
            return DataHandler.steps.size() - indexStartStep;
    }

    public String timeToString() {
        return String.format(
                "%s -> %s",
                startTimeToString(),
                endTimeToString()
        );
    }

    public String codeToString() {
        return StateTranslator.getStateByCode(code);
    }

    public String startTimeToString() {
        return new SimpleDateFormat(App.dateTimeFormat).format(new Date(startTime));
    }

    public String endTimeToString() {
        if (endTime != 0)
            return new SimpleDateFormat(App.dateTimeFormat).format(new Date(endTime));
        else
            return "N/A";
    }

    public static String[] getHeaders() {
        String[] headers = {"Activity", "StartTime", "EndTime", "NumOfSteps"};
        return headers;
    }

    public String[] toStringArray() {
        String[] result = {
                StateTranslator.getStateByCode(this.code),
                this.startTimeToString(),
                this.endTimeToString(),
                Integer.toString(this.getNumberOfSteps())
        };
        return result;
    }

    public String toString() {
        return String.format(
                "%s,%s,%s,%s",
                StateTranslator.getStateByCode(code),
                startTimeToString(),
                endTime,
                getNumberOfSteps()
        );
    }

}
