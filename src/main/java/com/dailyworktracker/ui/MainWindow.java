package com.dailyworktracker.ui;

import com.dailyworktracker.manager.ActivityManager;
import com.dailyworktracker.manager.PersistenceManager;
import com.dailyworktracker.manager.TimeTracker;
import com.dailyworktracker.model.ActivityType;
import com.dailyworktracker.model.DailyLog;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWindow extends JFrame {
    private ActivityManager activityManager;
    private TimeTracker timeTracker;
    private PersistenceManager persistenceManager;
    private TimeDisplayPanel timeDisplayPanel;
    private ControlPanel controlPanel;
    private JButton closeDayButton;

    public MainWindow() {
        setTitle("Daily Work Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 325);
        setLocationRelativeTo(null);
        setResizable(false);

        this.activityManager = new ActivityManager();
        this.timeTracker = new TimeTracker();
        this.persistenceManager = new PersistenceManager();

        loadState();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        timeDisplayPanel = new TimeDisplayPanel();
        mainPanel.add(timeDisplayPanel);

        controlPanel = new ControlPanel();
        setupControlPanelListeners();
        mainPanel.add(controlPanel);

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

        updateUIFromLoadedState();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveState();
            }
        });
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
            activityManager.stopCurrentActivity();

            controlPanel.setStartEnabled(true);
            controlPanel.setStopEnabled(false);
            timeTracker.stop();

            saveState();
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
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            activityManager.startNewDay();
            timeDisplayPanel.reset();
            controlPanel.resetState();
            timeTracker.reset();
            persistenceManager.clearState();
        }
    }

    private void saveState() {
        persistenceManager.saveState(timeTracker, activityManager.getCurrentDailyLog());
    }

    private void loadState() {
        Object[] loadedData = persistenceManager.loadState();
        if (loadedData != null) {
            long elapsedSeconds = (long) loadedData[0];
            DailyLog dailyLog = (DailyLog) loadedData[1];

            timeTracker.setElapsedSeconds(elapsedSeconds);
            activityManager.setCurrentDailyLog(dailyLog);
        }
    }

    private void updateUIFromLoadedState() {
        timeDisplayPanel.updateTime(timeTracker.getFormattedTime());
        if (activityManager.isActivityInProgress()) {
            controlPanel.setStartEnabled(false);
            controlPanel.setStopEnabled(true);
            timeTracker.start(() -> timeDisplayPanel.updateTime(timeTracker.getFormattedTime()));
        }
    }
}
