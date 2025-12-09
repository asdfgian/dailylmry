package com.dailyworktracker.ui;

import com.dailyworktracker.manager.ActivityManager;
import com.dailyworktracker.manager.PersistenceManager;
import com.dailyworktracker.manager.TimeTracker;
import com.dailyworktracker.model.ActivityType;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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

        changeActivityCombo();

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
        controlPanel.setActivityComboListener(e -> changeActivityCombo());
    }

    private void changeActivityCombo() {
        if (activityManager.isActivityInProgress()) {
            stopActivity();
        }

        ActivityType selectedType = controlPanel.getSelectedActivityType();
        activityManager.createAndStartActivity(selectedType.getDisplayName(), selectedType);

        long loadedTime = persistenceManager.loadState(selectedType);
        timeTracker.setElapsedSeconds(loadedTime);
        timeDisplayPanel.updateTime(timeTracker.getFormattedTime());
    }

    private void startActivity() {
        controlPanel.setStartEnabled(false);
        controlPanel.setStopEnabled(true);

        timeTracker.start(() -> {
            timeDisplayPanel.updateTime(timeTracker.getFormattedTime());
        });
    }

    private void stopActivity() {
        if (!activityManager.isActivityInProgress()) {
            return;
        }
        timeTracker.stop();
        saveState();
        controlPanel.setStartEnabled(true);
        controlPanel.setStopEnabled(false);
    }

    private void closeDay() {
        if (activityManager.isActivityInProgress()) {
            stopActivity();
        }

        long coding = persistenceManager.loadState(ActivityType.CODING);
        long meeting = persistenceManager.loadState(ActivityType.MEETING);
        long testing = persistenceManager.loadState(ActivityType.QA_TESTING);

        String table = String.format("""
                <html>
                <body style='font-family:Segoe UI, sans-serif; font-size:13px;'>

                <h3>Do you want to end the day?</h3>

                <table border='1' cellpadding='5' cellspacing='0' style='border-collapse:collapse; text-align:center;'>
                    <tr style='background-color:#e0e0e0; font-weight:bold;'>
                        <td>Activity</td>
                        <td>Time</td>
                    </tr>
                    <tr>
                        <td>Coding</td>
                        <td>%s</td>
                    </tr>
                    <tr>
                        <td>Meeting</td>
                        <td>%s</td>
                    </tr>
                    <tr>
                        <td>QA Testing</td>
                        <td>%s</td>
                    </tr>
                </table>

                </body>
                </html>
                """,
                formatTime(coding),
                formatTime(meeting),
                formatTime(testing));

        int option = JOptionPane.showConfirmDialog(
                this,
                table,
                "Close Day",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            activityManager.startNewDay();
            timeDisplayPanel.reset();
            controlPanel.resetState();
            timeTracker.reset();
            persistenceManager.clearState();
        }
    }

    private void saveState() {
        if (activityManager.getCurrentActivity() != null) {
            persistenceManager.saveState(
                    activityManager.getCurrentDailyLog(),
                    timeTracker);
        }
    }

    private String formatTime(long seconds) {
        long h = seconds / 3600;
        long m = (seconds % 3600) / 60;
        long s = seconds % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }

}
