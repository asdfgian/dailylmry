package com.dailyworktracker.model;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Activity implements Serializable {
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private ActivityType type;
    private long durationMinutes;

    public Activity(String name, LocalTime startTime, LocalTime endTime, ActivityType type) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.durationMinutes = calculateDuration();
    }

    public Activity(String name, ActivityType type) {
        this(name, null, null, type);
    }

    private long calculateDuration() {
        if (startTime != null && endTime != null) {
            return ChronoUnit.MINUTES.between(startTime, endTime);
        }
        return 0;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
        this.durationMinutes = calculateDuration();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public long getDurationMinutes() {
        return durationMinutes;
    }

    public String getDurationFormatted() {
        if (durationMinutes == 0)
            return "0 min";
        long hours = durationMinutes / 60;
        long minutes = durationMinutes % 60;
        if (hours == 0)
            return minutes + " min";
        if (minutes == 0)
            return hours + "h";
        return hours + "h " + minutes + "m";
    }

    public String getTimeRange() {
        if (startTime == null || endTime == null)
            return "N/A";
        return startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " - "
                + endTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
