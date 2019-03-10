package com.github.orthae.discordtriggerstuff;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

public class Logger {
    private Logger() {
    }

    public static Logger getInstance() {
        return LoggerHolder.INSTANCE;
    }

    public static class LoggerHolder {
        public static final Logger INSTANCE = new Logger();
    }

    private final Path LOG_PATH = Paths.get("dts_log.txt");

    public void log(String msgToLog) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Calendar.getInstance().getTime().toString());
        stringBuilder.append(" | ");
        stringBuilder.append(msgToLog);
        try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(LOG_PATH.toFile(), true))) {
            bWriter.append(stringBuilder);
            bWriter.newLine();
        } catch (IOException e) {
            System.out.println("Could not log message, IO Exception thrown");
        }
    }
}
