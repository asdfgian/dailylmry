package com.dailyworktracker.ui;

import com.dailyworktracker.model.ActivityType;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ControlPanel extends JPanel {
    private JButton startButton;
    private JButton stopButton;
    private JComboBox<ActivityType> activityCombo;

    public ControlPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        setOpaque(false);

        activityCombo = new JComboBox<>(ActivityType.values());
        activityCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        activityCombo.setPreferredSize(new Dimension(150, 35));

        startButton = new JButton("Start / Resume");
        startButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        startButton.setBackground(new Color(52, 211, 153));
        startButton.setForeground(Color.WHITE);
        startButton.setPreferredSize(new Dimension(140, 40));
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        stopButton = new JButton("Stop / Pause");
        stopButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        stopButton.setBackground(new Color(239, 68, 68));
        stopButton.setForeground(Color.WHITE);
        stopButton.setPreferredSize(new Dimension(140, 40));
        stopButton.setBorderPainted(false);
        stopButton.setFocusPainted(false);
        stopButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        stopButton.setEnabled(false);

        add(new JLabel("Select Activity"));
        add(activityCombo);
        add(startButton);
        add(stopButton);

        setPreferredSize(new Dimension(10, 95));
    }

    public void setStartButtonListener(ActionListener listener) {
        startButton.addActionListener(listener);
    }

    public void setStopButtonListener(ActionListener listener) {
        stopButton.addActionListener(listener);
    }

    public ActivityType getSelectedActivityType() {
        return (ActivityType) activityCombo.getSelectedItem();
    }

    public void setStartEnabled(boolean enabled) {
        startButton.setEnabled(enabled);
    }

    public void setStopEnabled(boolean enabled) {
        stopButton.setEnabled(enabled);
    }

    public void resetState() {
        setStartEnabled(true);
        setStopEnabled(false);
    }
}
