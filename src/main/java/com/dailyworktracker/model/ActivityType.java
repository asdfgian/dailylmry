package com.dailyworktracker.model;

public enum ActivityType {
    MEETING("Meeting", "📅"),
    CODING("Coding", "💻"),
    QA_TESTING("QA / Testing", "🧪");

    private final String displayName;
    private final String emoji;

    ActivityType(String displayName, String emoji) {
        this.displayName = displayName;
        this.emoji = emoji;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmoji() {
        return emoji;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
