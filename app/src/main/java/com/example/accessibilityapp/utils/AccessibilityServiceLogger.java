package com.example.accessibilityapp.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AccessibilityServiceLogger {

    public static void logAction(String action, String uiView,int iteration) {
        String output = String.format(Locale.getDefault(),"The action %s was performed on %s. Iteration:%d", action, uiView,iteration);
        logInfo("ACTION", output);
    }

    public static void logInfo(String tag, String message) {
        message=String.format(Locale.getDefault(),"%s : %s", getTime(), message);
        Log.i(tag, message);
    }

    private static String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",Locale.getDefault());
        return sdf.format(new Date());
    }
}
