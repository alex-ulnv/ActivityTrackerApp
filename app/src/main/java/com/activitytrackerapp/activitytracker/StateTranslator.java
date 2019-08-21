package com.activitytrackerapp.activitytracker;

public class StateTranslator {

    // 1 = Running
    // 2 = Walking

    public static String getStateByCode(int code) {
        switch (code) {
            case 1:
                return "Running";
            case 2:
                return "Walking";
        }
        return "None";
    }

    public static int getCodeForState(String state) {
        switch (state) {
            case "Running":
                return 1;
            case "Walking":
                return 2;
        }
        return -1;
    }

}
