package logs;

public class Logger {
    private static final org.apache.log4j.Logger Log = org.apache.log4j.Logger.getLogger(Logger.class.getName());//

    public static void debug(String message) {
        Log.debug(message);
    }
    public static void error(String message) {
        Log.error(message);
    }
    public static void fatal(String message) {
        Log.fatal(message);
    }
    public static void info(String message) {
        Log.info(message);
    }
    public static void warn(String message) {
        Log.warn(message);
    }
}
