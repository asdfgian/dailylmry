package com.dailyworktracker.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class DailyLog implements Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDate date;
    private Activity currentActivity;

    public DailyLog(LocalDate date) {
        this.date = date;
        this.currentActivity = null;
    }

    public void startActivity(Activity activity) {
        if (currentActivity != null) {
            currentActivity.setEndTime(LocalTime.now());
        }
        activity.setStartTime(LocalTime.now());
        this.currentActivity = activity;
    }

    public void stopCurrentActivity() {
        if (currentActivity != null) {
            currentActivity.setEndTime(LocalTime.now());
        }
    }

    public String getTotalDurationFormatted() {
        long totalMinutes = currentActivity.getDurationMinutes();
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        if (hours == 0)
            return minutes + " min";
        if (minutes == 0)
            return hours + "h";
        return hours + "h " + minutes + "m";
    }

    public LocalDate getDate() {
        return date;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }
}
