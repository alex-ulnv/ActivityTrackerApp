package com.activitytrackerapp.activitytracker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Step {

    int code;
    long timeStamp;

    public Step(int code, long timeStamp) {
        this.code = code;
        this.timeStamp = timeStamp;
    }

    public static String[] getHeaders() {
        String[] headers = {"State", "TimeStamp"};
        return headers;
    }

    public String timeToString() {
        return new SimpleDateFormat(App.dateTimeFormat).format(new Date(timeStamp));
    }

    public String[] toStringArray() {
        String[] result = {StateTranslator.getStateByCode(code),  timeToString()};
        return result;
    }

    public String toString() {
        return String.format("%s,%s", StateTranslator.getStateByCode(code), timeToString());
    }

}
