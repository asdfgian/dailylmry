package com.dailyworktracker.manager;

import com.dailyworktracker.model.Activity;
import com.dailyworktracker.model.DailyLog;
import com.dailyworktracker.model.ActivityType;
import java.time.LocalDate;

public class ActivityManager {
    private DailyLog currentDailyLog;

    public ActivityManager() {
        this.currentDailyLog = new DailyLog(LocalDate.now());
    }

    public void createAndStartActivity(String name, ActivityType type) {
        Activity activity = new Activity(name, type);
        currentDailyLog.startActivity(activity);
    }

    public void stopCurrentActivity() {
        currentDailyLog.stopCurrentActivity();
    }

    public void addPredefinedActivity(ActivityType type, String customName) {
        Activity activity = new Activity(customName != null ? customName : type.getDisplayName(), type);
        currentDailyLog.addActivity(activity);
    }

    public void removeActivity(Activity activity) {
        currentDailyLog.removeActivity(activity);
    }

    public DailyLog getCurrentDailyLog() {
        return currentDailyLog;
    }

    public void startNewDay() {
        this.currentDailyLog = new DailyLog(LocalDate.now());
    }

    public boolean isActivityInProgress() {
        return currentDailyLog.getCurrentActivity() != null;
    }

    public Activity getCurrentActivity() {
        return currentDailyLog.getCurrentActivity();
    }
}
