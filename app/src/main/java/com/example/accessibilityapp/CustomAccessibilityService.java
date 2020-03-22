package com.example.accessibilityapp;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.widget.Toast;

import com.example.accessibilityapp.utils.AccessibilityServiceLogger;
import com.example.accessibilityapp.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;
import static com.example.accessibilityapp.MainActivity.targetPackage;


public class CustomAccessibilityService extends AccessibilityService {
    private static final Random rnd = new Random();
    private static final int sleepTimeAfterAction = 200;
    private static final int backButtonPercentage = 1;
    private static final int maxDurationBetweenEvents = 5*1000;
    private static final int maxDurationUntilGoBack = 10 * 1000;
    private static final int maxDurationUntilRestart = 20 * 1000;
    private static int counter = 0;
    private static long lastEventTime = 0;
    private static ScheduledExecutorService executor;

    //service can perform actions on windows of these packages
    private static List<String> ALLOWED_PACKAGES = new ArrayList<>(Collections.singletonList("com.google.android.permissioncontroller"));

    private static final List<AccessibilityAction> ACTIONS = Collections.unmodifiableList(Arrays.asList(
            AccessibilityAction.ACTION_SET_TEXT,
            AccessibilityAction.ACTION_CLICK,
            AccessibilityAction.ACTION_LONG_CLICK,
            AccessibilityAction.ACTION_CUT,
            AccessibilityAction.ACTION_COPY,
            AccessibilityAction.ACTION_PASTE,
            AccessibilityAction.ACTION_SCROLL_FORWARD,
            AccessibilityAction.ACTION_SCROLL_BACKWARD,
            AccessibilityAction.ACTION_SET_TEXT,
            AccessibilityAction.ACTION_SCROLL_UP,
            AccessibilityAction.ACTION_SCROLL_DOWN
    ));


    @Override
    protected void onServiceConnected() {
        AccessibilityServiceLogger.logInfo(TAG, "Service Enabled");

        counter = 0;

        AccessibilityServiceInfo serviceInfo = getServiceInfo();

        serviceInfo.packageNames = Utils.mergeListToArray(serviceInfo.packageNames, ALLOWED_PACKAGES);

        setServiceInfo(serviceInfo);


        if (!Arrays.asList(getServiceInfo().packageNames).contains(targetPackage)) {
            disableSelf();
            Toast.makeText(getApplicationContext(), "Service cannot test selected app", Toast.LENGTH_LONG).show();
            return;
        }

        ALLOWED_PACKAGES.add(targetPackage);

        executor = Executors.newScheduledThreadPool(1);

        Runnable helloRunnable = new Runnable() {
            public void run() {
                long spentTime = System.currentTimeMillis() - lastEventTime;
                AccessibilityServiceLogger.logInfo(TAG, "Spent Time:" + spentTime);

                if (spentTime > maxDurationUntilRestart) {
                    restartApp();
                    lastEventTime = System.currentTimeMillis();
                } else if (spentTime > maxDurationUntilGoBack) {
                    goToApp(targetPackage);
                } else if (spentTime > maxDurationBetweenEvents) {
                    performCustomGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK, "GLOBAL_ACTION_BACK was performed");
                }
            }
        };

        executor.scheduleAtFixedRate(helloRunnable, 0, maxDurationBetweenEvents, TimeUnit.MILLISECONDS);

        goToApp(targetPackage);

        super.onServiceConnected();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();

        if (!isServiceRunning() || rootNode == null || !ALLOWED_PACKAGES.contains(rootNode.getPackageName().toString()))
            return;

        findNodePerformAction(rootNode);
        lastEventTime = System.currentTimeMillis();

    }

    private void findNodePerformAction(AccessibilityNodeInfo node) {

        if (isClickBackButton()) {
            performCustomGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK, "GLOBAL_ACTION_BACK was performed");
            return;
        }

        if (isExpandable(node)) {

            AccessibilityNodeInfo currentNode = chooseRandomNode(node);
            if (currentNode != null)
                findNodePerformAction(currentNode);
        }

        AccessibilityAction action = chooseRandomAction(node);
        if (action != null) {
            if (action == AccessibilityAction.ACTION_SET_TEXT) {

                Bundle arguments = new Bundle();
                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                        Utils.getRandomASCString(10));

                performCustomAction(node, action);

            } else {
                performCustomAction(node, action);
            }
        }

    }

    private static boolean isExpandable(AccessibilityNodeInfo node) {
        return node.getChildCount() > 0;
    }


    private static AccessibilityNodeInfo chooseRandomNode(AccessibilityNodeInfo node) {
        return node.getChild(rnd.nextInt(node.getChildCount()));
    }

    private static AccessibilityAction chooseRandomAction(AccessibilityNodeInfo node) {
        List<AccessibilityAction> performable = node.getActionList();
        performable.retainAll(ACTIONS);

        if (performable.size() == 0) return null;
        return performable.get(rnd.nextInt(performable.size()));
    }

    @Override
    public void onInterrupt() {
    }


    private static boolean isClickBackButton() {
        return rnd.nextInt(100 / backButtonPercentage) == 0;
    }


    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) return false;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (getClass().getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void goToApp(String packageName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        startActivity(intent);
        AccessibilityServiceLogger.logInfo(TAG, "Service tried to open the app.");
    }

    private void restartApp() {

        PackageManager manager = getApplicationContext().getPackageManager();
        Intent i = manager.getLaunchIntentForPackage(targetPackage);
        if (i != null) {
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        getApplicationContext().startActivity(i);
        AccessibilityServiceLogger.logInfo(TAG, "Service tried to restart the app.");

    }


    @Override
    public void onDestroy() {
        if (executor != null) executor.shutdown();
        AccessibilityServiceLogger.logInfo(TAG, "Service disabled");
        super.onDestroy();
    }


    private void performCustomGlobalAction(int action, String log) {
        performGlobalAction(action);
        AccessibilityServiceLogger.logAction(log, null, counter);
        checkActionRestrictions();
    }

    private void performCustomAction(AccessibilityNodeInfo node, AccessibilityAction action) {
        node.performAction(action.getId());
        AccessibilityServiceLogger.logAction(action.toString(), node.getClassName().toString(), counter);
        node.recycle();
        checkActionRestrictions();

    }

    private void checkActionRestrictions() {

        counter++;
        if (counter >= MainActivity.iteration) {
            disableSelf();
            AccessibilityServiceLogger.logInfo(TAG, "Service will be disabled because it reached the iteration count. Iteration Count:" + MainActivity.iteration);
            return;
        }
        SystemClock.sleep(sleepTimeAfterAction);
    }
}


