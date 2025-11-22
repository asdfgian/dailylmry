package com.dailyworktracker.ui;

import com.dailyworktracker.model.Activity;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ActivityListPanel extends JPanel {
    private JPanel activityContainer;
    private Map<Activity, JPanel> activityPanels;

    public ActivityListPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(250, 250, 250));

        JLabel titleLabel = new JLabel("Today's Activities");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(titleLabel, BorderLayout.NORTH);

        activityContainer = new JPanel();
        activityContainer.setLayout(new BoxLayout(activityContainer, BoxLayout.Y_AXIS));
        activityContainer.setOpaque(false);
        activityContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(activityContainer);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(scrollPane, BorderLayout.CENTER);
        this.activityPanels = new HashMap<>();
    }

    public void addActivity(Activity activity) {
        JPanel activityPanel = createActivityItemPanel(activity);
        activityContainer.add(activityPanel);
        activityContainer.add(Box.createVerticalStrut(8));
        activityPanels.put(activity, activityPanel);
        activityContainer.revalidate();
        activityContainer.repaint();
    }

    public void removeActivity(Activity activity) {
        JPanel panel = activityPanels.remove(activity);
        if (panel != null) {
            activityContainer.remove(panel);
            activityContainer.revalidate();
            activityContainer.repaint();
        }
    }

    public void updateActivity(Activity activity) {
        JPanel panel = activityPanels.get(activity);
        if (panel != null) {
            activityContainer.remove(panel);
            JPanel updatedPanel = createActivityItemPanel(activity);
            activityContainer.add(updatedPanel);
            activityPanels.put(activity, updatedPanel);
            activityContainer.revalidate();
            activityContainer.repaint();
        }
    }

    public void clearActivities() {
        activityContainer.removeAll();
        activityPanels.clear();
        activityContainer.revalidate();
        activityContainer.repaint();
    }

    private JPanel createActivityItemPanel(Activity activity) {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panel.setMaximumSize(new Dimension(280, 65));
        panel.setPreferredSize(new Dimension(280, 65));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);
        JLabel iconLabel = new JLabel(activity.getType().getEmoji());
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        leftPanel.add(iconLabel);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setOpaque(false);
        JLabel nameLabel = new JLabel(activity.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(new Color(30, 30, 30));
        JLabel timeLabel = new JLabel(activity.getTimeRange());
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeLabel.setForeground(new Color(150, 150, 150));
        namePanel.add(nameLabel);
        namePanel.add(timeLabel);
        leftPanel.add(namePanel);

        panel.add(leftPanel, BorderLayout.CENTER);

        JLabel durationLabel = new JLabel(activity.getDurationFormatted());
        durationLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        durationLabel.setForeground(new Color(100, 100, 100));
        panel.add(durationLabel, BorderLayout.EAST);

        return panel;
    }
}
