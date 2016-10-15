package com.is.common;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class TLog {
    /**
     * This flag to indicate the log is enabled or disabled.
     */
    private static boolean isLogEnable = false;

    /**
     * Disable the log output.
     */
    public static void disableLog() {
        isLogEnable = false;
    }

    /**
     * Enable the log output.
     */
    public static void enableLog() {
        isLogEnable = true;
    }

    /**
     * Debug
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (isLogEnable) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.d(tag, rebuildMsg(stackTraceElement, msg));
        }
    }

    /**
     * Information
     *
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (isLogEnable) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.i(tag, rebuildMsg(stackTraceElement, msg));
        }
    }

    /**
     * Verbose
     *
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if (isLogEnable) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.v(tag, rebuildMsg(stackTraceElement, msg));
        }
    }

    /**
     * Warning
     *
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (isLogEnable) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.w(tag, rebuildMsg(stackTraceElement, msg));
        }
    }

    /**
     * Error
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (isLogEnable) {
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
            Log.e(tag, rebuildMsg(stackTraceElement, msg));
        }
    }

    /**
     * Rebuild Log Msg
     *
     * @param msg
     * @return
     */
    private static String rebuildMsg(StackTraceElement stackTraceElement, String msg) {
        StringBuffer sb = new StringBuffer();
        sb.append(stackTraceElement.getFileName());
        sb.append(" (");
        sb.append(stackTraceElement.getLineNumber());
        sb.append(") ");
        sb.append(stackTraceElement.getMethodName());
        sb.append(": ");
        sb.append(msg);
        writeToFile(sb.toString());
        return sb.toString();
    }
    public static void writeToFile(String msg) {
        if (isLogEnable) {
            try {
                BufferedWriter bos = new BufferedWriter(new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() +  "/000/" + "log.txt",true));
                bos.write("\r\n"+"--------------------------0-----------------------------"+"\r\n");
                bos.write(msg);
                bos.write("\r\n"+"--------------------------1-----------------------------"+"\r\n");
                bos.flush();
                bos.close();
                Log.d("TLog","打印到文件："+msg);
            } catch (Exception e) {
                Log.d("TLog","打印到文件失败");
                e.printStackTrace();
            }
        }
    }

}
