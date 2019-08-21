package com.activitytrackerapp.activitytracker;

public class StepItem {

    private String step_num, type, timestamp;

    public StepItem(Step step, int step_num) {

        this.step_num = Integer.toString(step_num);
        this.type = StateTranslator.getStateByCode(step.code);
        this.timestamp = step.timeToString();
    }

    public String getStep_num() {
        return step_num;
    }

    public String getType() {
        return type;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
