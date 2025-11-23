package com.dailyworktracker.manager;

import java.time.LocalTime;
import javax.swing.Timer;

public class TimeTracker {
    private LocalTime startTime;
    private long elapsedSeconds;
    private Timer timer;
    private Runnable onTickCallback;
    private boolean isRunning;

    public TimeTracker() {
        this.elapsedSeconds = 0;
        this.isRunning = false;
    }

    public void start(Runnable onTick) {
        if (!isRunning) {
            this.startTime = LocalTime.now();
            this.onTickCallback = onTick;
            this.isRunning = true;

            timer = new Timer(1000, e -> {
                elapsedSeconds++;
                if (onTickCallback != null) {
                    onTickCallback.run();
                }
            });
            timer.start();
        }
    }

    public void stop() {
        if (isRunning && timer != null) {
            timer.stop();
            isRunning = false;
        }
    }

    public void reset() {
        stop();
        elapsedSeconds = 0;
        startTime = null;
    }

    public String getFormattedTime() {
        long hours = elapsedSeconds / 3600;
        long minutes = (elapsedSeconds % 3600) / 60;
        long seconds = elapsedSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public long getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(long elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public LocalTime getStartTime() {
        return startTime;
    }
}
