package net.sevecek.boot.standalone;

import java.io.*;
import java.lang.ref.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

public class WindowLoggingHandler extends Handler {

    private static WindowLoggingHandler instance;
    private StringWriter buffer;
    private List<WeakReference<LoggingListener>> listeners;

    public WindowLoggingHandler() {
        super();
        buffer = new StringWriter();
        listeners = new CopyOnWriteArrayList<>();
        instance = this;
    }

    public static WindowLoggingHandler getInstance() {
        return instance;
    }

    public synchronized String getBuffer() {
        String result = buffer.toString();
        buffer.getBuffer().setLength(0);
        return result;
    }

    @Override
    public synchronized void publish(LogRecord record) {
        Throwable exception = record.getThrown();
        if (exception != null) {
            buffer.append(record.getMessage());
            buffer.append("\n");
            StringWriter stringWriter = new StringWriter();
            exception.printStackTrace(new PrintWriter(stringWriter));
            buffer.append(stringWriter.toString());
            buffer.append("\n");
        } else {
            buffer.append(record.getMessage());
            buffer.append("\n");
        }

        String buffer = getBuffer();
        Iterator<WeakReference<LoggingListener>> iterator = listeners.iterator();
        while (iterator.hasNext()) {
            WeakReference<LoggingListener> listenerRef = iterator.next();
            LoggingListener listener = listenerRef.get();
            if (listener == null) {
                iterator.remove();
            } else {
                listener.onLog(buffer);
            }
        }
    }

    public synchronized void addLoggingListener(LoggingListener lst) {
        listeners.add(new WeakReference<>(lst));
    }

    public synchronized void removeLoggingListener(LoggingListener lst) {
        Iterator<WeakReference<LoggingListener>> iterator = listeners.iterator();
        while (iterator.hasNext()) {
            WeakReference<LoggingListener> listenerRef = iterator.next();
            LoggingListener listener = listenerRef.get();
            if (listener == null || listener == lst) {
                iterator.remove();
            }
        }
    }

    @Override
    public synchronized void flush() {
    }

    @Override
    public synchronized void close() {
    }

    public static interface LoggingListener {
        void onLog(String text);
    }
}
