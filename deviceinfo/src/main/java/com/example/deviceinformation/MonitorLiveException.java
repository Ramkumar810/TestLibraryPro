package com.example.deviceinformation;

/**
 * Created by kannas on 6/30/2017.
 */

public class MonitorLiveException extends RuntimeException {
    static final long serialVersionUID = 1;

    /**
     * Constructs a new MonitorLiveException.
     */
    public MonitorLiveException() {
        super();
    }

    /**
     * Constructs a new MonitorLiveException.
     *
     * @param message the detail message of this exception
     */
    public MonitorLiveException(String message) {
        super(message);
    }

    /**
     * Constructs a new MonitorLiveException.
     *
     * @param format the format string (see {@link java.util.Formatter#format})
     * @param args   the list of arguments passed to the formatter.
     */
    public MonitorLiveException(String format, Object... args) {
        this(String.format(format, args));
    }

    /**
     * Constructs a new MonitorLiveException.
     *
     * @param message   the detail message of this exception
     * @param throwable the cause of this exception
     */
    public MonitorLiveException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * Constructs a new MonitorLiveException.
     *
     * @param throwable the cause of this exception
     */
    public MonitorLiveException(Throwable throwable) {
        super(throwable);
    }

    @Override
    public String toString() {
        // Throwable.toString() returns "MonitorLiveException:{message}". Returning just "{message}"
        // should be fine here.
        return getMessage();
    }
}
