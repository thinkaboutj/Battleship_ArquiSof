/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.*;

/**
 *
 * @author Chris
 */
public class AppLogger {

    public static final Logger logger = Logger.getLogger(AppLogger.class.getName());
    static Handler handler = null;
    public static final Level DEFAULT_LEVEL = Level.CONFIG;
    private static boolean isInitialized = false;

    public static void initializeLogger() {
        if (isInitialized) {
            return;
        }
        try {
            handler = MyFileHandler.createLoggerInstance("BattleshipLog.log", false);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not create file", e);
        }
        handler.setFormatter(new SimpleFormatter());
        logger.addHandler(handler);
        logger.setLevel(Level.CONFIG);
        isInitialized = true;
    }

    private AppLogger() {
    }

    static class MyFileHandler extends FileHandler {

        static MyFileHandler createLoggerInstance(String filename, boolean append) throws IOException {
            String folderName = "logs";
            Path logFilePath = Paths.get(folderName, filename);
            if (createLogFolder(folderName)) {
                return new MyFileHandler(logFilePath.toString(), append);
            } else {
                return new MyFileHandler(filename, append);
            }
        }

        private MyFileHandler(String pattern, boolean append) throws IOException, SecurityException {
            super(pattern, append);
        }

        private static boolean createLogFolder(String folderName) {
            File dir = new File(folderName);
            return dir.exists() || dir.mkdir();
        }
    }
}
