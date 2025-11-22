package com.dailyworktracker.ui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TimeDisplayPanel extends JPanel {
    private JLabel timeLabel;
    private JLabel dateLabel;

    public TimeDisplayPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        JLabel titleLabel = new JLabel("Daily Work Tracker");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        dateLabel = new JLabel();
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(100, 100, 100));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateDate();

        add(Box.createVerticalStrut(10));
        add(titleLabel);
        add(Box.createVerticalStrut(5));
        add(dateLabel);
        add(Box.createVerticalStrut(10));

        timeLabel = new JLabel("00:00:00");
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeLabel.setForeground(new Color(50, 50, 50));
        add(timeLabel);
        add(Box.createVerticalStrut(20));

        setMaximumSize(new Dimension(400, 200));
    }

    public void updateTime(String time) {
        timeLabel.setText(time);
    }

    private void updateDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE MMMM d, yyyy", java.util.Locale.of("en", "US"));
        dateLabel.setText(today.format(formatter));
    }

    public void reset() {
        timeLabel.setText("00:00:00");
    }
}
