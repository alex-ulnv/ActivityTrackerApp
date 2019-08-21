package com.activitytrackerapp.activitytracker;

public class ActivityItem {

    private int imageResource;
    private String title, dates_from, dates_to, steps;

    public ActivityItem(UserActivity userActivity) {
        title = userActivity.codeToString();
        dates_from = userActivity.startTimeToString();
        dates_to = userActivity.endTimeToString();
        steps = Integer.toString(userActivity.getNumberOfSteps());
        switch (title) {
            case "Walking":
                imageResource = R.drawable.ic_walking;
                break;
            case "Running":
                imageResource = R.drawable.ic_running;
                break;
            default:
                imageResource = R.drawable.ic_default;

        }
    }

    public int getImageResource() { return imageResource; }

    public String getTitle() { return title; }

    public String getDatesFrom() { return dates_from; }

    public String getDatesTo() { return dates_to; }

    public String getSteps() { return steps; }

}
