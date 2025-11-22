package com.dailyworktracker.ui;

import com.dailyworktracker.manager.ActivityManager;
import com.dailyworktracker.manager.TimeTracker;
import com.dailyworktracker.model.Activity;
import com.dailyworktracker.model.ActivityType;
import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private ActivityManager activityManager;
    private TimeTracker timeTracker;
    private TimeDisplayPanel timeDisplayPanel;
    private ControlPanel controlPanel;
    private ActivityListPanel activityListPanel;
    private JButton closeDayButton;

    public MainWindow() {
        setTitle("Daily Work Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        this.activityManager = new ActivityManager();
        this.timeTracker = new TimeTracker();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        timeDisplayPanel = new TimeDisplayPanel();
        mainPanel.add(timeDisplayPanel);

        controlPanel = new ControlPanel();
        setupControlPanelListeners();
        mainPanel.add(controlPanel);

        mainPanel.add(Box.createVerticalStrut(10));

        activityListPanel = new ActivityListPanel();
        mainPanel.add(activityListPanel);

        mainPanel.add(Box.createVerticalStrut(10));

        closeDayButton = new JButton("Close Day");
        closeDayButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeDayButton.setBackground(new Color(52, 211, 153));
        closeDayButton.setForeground(Color.WHITE);
        closeDayButton.setPreferredSize(new Dimension(250, 45));
        closeDayButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        closeDayButton.setBorderPainted(false);
        closeDayButton.setFocusPainted(false);
        closeDayButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeDayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeDayButton.addActionListener(e -> closeDay());
        mainPanel.add(closeDayButton, BorderLayout.CENTER);
        mainPanel.add(closeDayButton);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupControlPanelListeners() {
        controlPanel.setStartButtonListener(e -> startActivity());
        controlPanel.setStopButtonListener(e -> stopActivity());
    }

    private void startActivity() {
        if (!activityManager.isActivityInProgress()) {
            ActivityType selectedType = controlPanel.getSelectedActivityType();
            activityManager.createAndStartActivity(selectedType.getDisplayName(), selectedType);
            controlPanel.setStartEnabled(false);
            controlPanel.setStopEnabled(true);

            timeTracker.start(() -> {
                timeDisplayPanel.updateTime(timeTracker.getFormattedTime());
            });
        }
    }

    private void stopActivity() {
        if (activityManager.isActivityInProgress()) {
            Activity stoppedActivity = activityManager.getCurrentActivity();
            activityManager.stopCurrentActivity();
            
            controlPanel.setStartEnabled(true);
            controlPanel.setStopEnabled(false);
            timeTracker.stop();
            timeDisplayPanel.reset();

            if (stoppedActivity != null) {
                activityListPanel.addActivity(stoppedActivity);
            }
        }
    }

    private void closeDay() {
        if (activityManager.isActivityInProgress()) {
            stopActivity();
        }
        
        int option = JOptionPane.showConfirmDialog(
            this,
            "¿Deseas cerrar el día?\nTotal: " + activityManager.getCurrentDailyLog().getTotalDurationFormatted(),
            "Close Day",
            JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            activityManager.startNewDay();
            activityListPanel.clearActivities();
            timeDisplayPanel.reset();
            controlPanel.resetState();
            timeTracker.reset();
        }
    }
}
