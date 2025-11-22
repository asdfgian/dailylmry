package com.dailyworktracker.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DailyLog {
    private LocalDate date;
    private List<Activity> activities;
    private Activity currentActivity;

    public DailyLog(LocalDate date) {
        this.date = date;
        this.activities = new ArrayList<>();
        this.currentActivity = null;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void removeActivity(Activity activity) {
        activities.remove(activity);
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
            if (!activities.contains(currentActivity)) {
                activities.add(currentActivity);
            }
            currentActivity = null;
        }
    }

    public long getTotalDurationMinutes() {
        return activities.stream().mapToLong(Activity::getDurationMinutes).sum();
    }

    public String getTotalDurationFormatted() {
        long totalMinutes = getTotalDurationMinutes();
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        if (hours == 0) return minutes + " min";
        if (minutes == 0) return hours + "h";
        return hours + "h " + minutes + "m";
    }

    public LocalDate getDate() {
        return date;
    }

    public List<Activity> getActivities() {
        return new ArrayList<>(activities);
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }
}
