package com.dailyworktracker.manager;

import com.dailyworktracker.model.DailyLog;
import java.io.*;

public class PersistenceManager {

    private static final String DATA_FILE = "data.txt";

    public void saveState(TimeTracker timeTracker, DailyLog dailyLog) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeLong(timeTracker.getElapsedSeconds());
            oos.writeObject(dailyLog);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object[] loadState() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            long elapsedSeconds = ois.readLong();
            DailyLog dailyLog = (DailyLog) ois.readObject();
            return new Object[]{elapsedSeconds, dailyLog};
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void clearState() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}
