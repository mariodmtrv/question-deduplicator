package edu.fmi.sudo.deduplicator;

/**
 * A primitive logger for the purpose of evaluating results
 *
 * Created by Miroslav Kramolinski
 */
public class Logger {
    private Logger() {} // Singleton

    private static boolean doLog = false;

    public static void enableLogging() {
        doLog = true;
    }

    public static void disableLogging() {
        doLog = false;
    }

    public static void log(String message) {
        if(doLog)
            System.out.println(message);
    }
}
