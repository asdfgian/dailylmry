package com.dailyworktracker.manager;

import com.dailyworktracker.model.ActivityType;
import com.dailyworktracker.model.DailyLog;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

public class PersistenceManager {

    private static final String DATA_FILE = "data.csv";
    private static final String TEMP_DATA_FILE = "data.tmp";

    public void saveState(DailyLog dailyLog, TimeTracker timeTracker) {
        File originalFile = new File(DATA_FILE);
        File tempFile = new File(TEMP_DATA_FILE);
        LocalDate today = LocalDate.now();
        String activityName = dailyLog.getCurrentActivity().getName();
        long elapsedSeconds = timeTracker.getElapsedSeconds();
        boolean entryFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].equals(today.toString()) && parts[1].equals(activityName)) {
                    writer.write(today.toString() + "," + activityName + "," + elapsedSeconds);
                    writer.newLine();
                    entryFound = true;
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }

            if (!entryFound) {
                writer.write(today.toString() + "," + activityName + "," + elapsedSeconds);
                writer.newLine();
            }

        } catch (FileNotFoundException e) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                writer.write("DATE,ACTIVITY,TIME");
                writer.newLine();
                writer.write(today.toString() + "," + activityName + "," + elapsedSeconds);
                writer.newLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Files.move(tempFile.toPath(), originalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long loadState(ActivityType activity) {
        File file = new File(DATA_FILE);
        LocalDate today = LocalDate.now();
        String activityName = activity.getDisplayName();

        if (!file.exists()) {
            return 0;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].equals(today.toString()) && parts[1].equals(activityName)) {
                    return Long.parseLong(parts[2]);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void clearState() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}
