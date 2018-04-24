package com.fb.jjyyzjy.buildlayoutdemo.utils;

import android.util.Log;

public class DebugLog {

    private static String LOG_TAG = "DEBUG_LOG";
    private static String className;
    private static String methodName;
    private static int lineNumber;
    private static boolean isDebug = true;

    public static void initDebug(boolean isDebug) {
        DebugLog.isDebug = isDebug;
    }

    private static boolean isDebug() {
        return isDebug;
    }

    private static String createLog(String log) {
        return "[" +
                className +
                ":" +
                methodName +
                "+" +
                lineNumber +
                "]" +
                log;
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static void e(String message) {
        if (isDebug()) {
            getMethodNames((new Throwable()).getStackTrace());
            Log.e(LOG_TAG, createLog(message));
        }
    }

    public static void i(String message) {
        if (isDebug()) {
            getMethodNames((new Throwable()).getStackTrace());
            Log.i(LOG_TAG, createLog(message));
        }
    }

    public static void d(String message) {
        if (isDebug()) {
            getMethodNames((new Throwable()).getStackTrace());
            Log.d(LOG_TAG, createLog(message));
        }
    }

    public static void v(String message) {
        if (isDebug()) {
            getMethodNames((new Throwable()).getStackTrace());
            Log.v(LOG_TAG, createLog(message));
        }
    }

    public static void w(String message) {
        if (isDebug()) {
            getMethodNames((new Throwable()).getStackTrace());
            Log.w(LOG_TAG, createLog(message));
        }
    }
}
