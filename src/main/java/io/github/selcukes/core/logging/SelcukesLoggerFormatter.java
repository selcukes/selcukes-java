package io.github.selcukes.core.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class SelcukesLoggerFormatter extends Formatter {

    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder(1000);
        builder.append("[").append(getLevel(record)).append("] - ");
        builder.append(df.format(new Date(record.getMillis()))).append(" - ");
        builder.append("[").append(record.getSourceClassName()).append(".");
        builder.append(record.getSourceMethodName()).append(":").append(getLineNumber(record)).append("] - ");
        builder.append(formatMessage(record));
        Throwable ex = record.getThrown();
        if (null != ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            builder.append('\n').append(sw.toString());
        }
        builder.append("\n");

        return builder.toString();
    }

    public String getLevel(LogRecord record) {
        String level = record.getLevel().getName();
        switch (level) {
            case "SEVERE":
                level = "ERROR";
                break;
            case "FINE":
                level = "DEBUG";
                break;
            case "FINER":
                level = "TRACE";
                break;
        }
        return level;
    }

    public String getHead(Handler h) {
        return super.getHead(h);
    }

    public String getTail(Handler h) {
        return super.getTail(h);
    }

    private StackTraceElement getCallerStackFrame(final String callerName) {
        StackTraceElement callerFrame = null;

        final StackTraceElement[] stack = new Throwable().getStackTrace();
        // Search the stack trace to find the calling class
        for (final StackTraceElement frame : stack) {
            if (callerName.equals(frame.getClassName())) {
                callerFrame = frame;
                break;
            }
        }
        return callerFrame;
    }

    private String getLineNumber(LogRecord record) {
        final StackTraceElement callerFrame = getCallerStackFrame(record.getSourceClassName());

        if (callerFrame != null) {

            final int lineNumber = callerFrame.getLineNumber();
            return lineNumber + "";
        }
        return "";
    }
}
