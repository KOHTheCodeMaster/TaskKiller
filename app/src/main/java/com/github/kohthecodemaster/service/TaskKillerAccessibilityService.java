package com.github.kohthecodemaster.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.github.kohthecodemaster.activity.MainActivity;
import com.github.kohthecodemaster.pojo.AppSettingsPojo;

import java.util.Arrays;
import java.util.LinkedList;

public class TaskKillerAccessibilityService extends AccessibilityService {

    private static final String TAG = "L0G-TaskKillerAccessibilityService";
    SimulationService simulationService;
    public static LinkedList<AppSettingsPojo> tasksToBeKilled;

    @Override
    public void onServiceConnected() {

        Log.v(TAG, "onServiceConnected: Begin.");

        simulationService = new SimulationService();

        //  Configuration done via accessibility_service_config.xml imported in Android Manifest
//        this.setServiceInfo(getConfiguredServiceInfo());

        Log.v(TAG, "onServiceConnected: End.");

    }

    /**
     * This method occurs everytime there's a change in UI Window State.
     * Firstly It validates the event's source for nullability, if it's null then simply discard this event.
     * Otherwise, attempt for Force Stop Btn simulation & OK Btn Simulation.
     *
     * @param event Accessibility Event which occurs everytime there's a change in UI Window State
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        handleEventForSimulation(event);
//        relaunchMainActivity();

    }

    public static void launchApplicationSettings(Context context, String packageName) {

        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", packageName, null));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //  Creates new instance & invoked onCreate()
        context.startActivity(intent);

    }

    private void relaunchMainActivity() {

        // TODO: 20-11-2022 - Instead of creating new instance of Activity, try to bring existing
        //  one in foreground and make use of it.

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //  Creates new instance & invoked onCreate()
/*
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
*/
        getApplicationContext().startActivity(intent);

    }

    private void handleEventForSimulation(AccessibilityEvent event) {

//        Log.v(TAG, "handleEventForSimulation: Begin.");
        if (AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED == event.getEventType()) {

            AccessibilityNodeInfo nodeInfo = event.getSource();
            if (nodeInfo == null) return;

            Runnable runWhenAppAlreadyStopped = () -> {

                simulationService.resetFlags();
                if (!tasksToBeKilled.isEmpty()) {
                    TaskKillerAccessibilityService.launchApplicationSettings(getApplicationContext(),
                            tasksToBeKilled.removeFirst().getPackageName());
                    Log.v(TAG, "handleEventForSimulation: Tasks Left: " +
                            Arrays.toString(tasksToBeKilled.stream()
                                    .map(AppSettingsPojo::getPackageName).toArray()));
                } else {
                    Log.v(TAG, "handleEventForSimulation: Tasks Completed - " + tasksToBeKilled.size());
                    relaunchMainActivity();
                }

            };
            simulationService.simulateMajor(nodeInfo, runWhenAppAlreadyStopped);
            whenOkIsClicked();


        }
//        Log.v(TAG, "handleEventForSimulation: End.");

    }

    private void whenOkIsClicked() {

        if (simulationService.isOkClicked()) {
            simulationService.resetFlags();
            if (!tasksToBeKilled.isEmpty()) {
                TaskKillerAccessibilityService.launchApplicationSettings(getApplicationContext(),
                        tasksToBeKilled.removeFirst().getPackageName());
                Log.v(TAG, "handleEventForSimulation: Tasks Left: " + tasksToBeKilled.size());
            } else {
                Log.v(TAG, "handleEventForSimulation: Tasks Completed - " + tasksToBeKilled.size());
                relaunchMainActivity();
            }
        }

    }

    private void testNodeInfoNotNull(AccessibilityEvent event) {

        Log.v(TAG, "testNodeInfoNotNull: Begin.\nText - " + event.getText() +
                "\nEvent Type - " + event.getEventType());

        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo == null) {
            Log.v(TAG, "testNodeInfoNotNull: 1. nodeInfo is NULL | packageName - "
                    + event.getPackageName());
            return;
        }

        Log.v(TAG, "testNodeInfoNotNull: 2. nodeInfo is NOT NULL | id - " + nodeInfo.getViewIdResourceName());

    }

    @Deprecated
    public static AccessibilityServiceInfo getConfiguredServiceInfo() {

//        Log.v(TAG, "getConfiguredServiceInfo: Begin.");

        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();

        // Set the type of events that this service wants to listen to.
        accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
//                | AccessibilityEvent.TYPE_VIEW_CLICKED
//                | AccessibilityEvent.TYPE_VIEW_FOCUSED
//                | AccessibilityEvent.TYPES_ALL_MASK
        ;

        // Set the type of feedback your service will provide.
        accessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        accessibilityServiceInfo.notificationTimeout = 100;
        accessibilityServiceInfo.packageNames = new String[]{"com.android.settings"};
        /*
            If you only want this service to work with specific applications, set their
            package names here. Otherwise, when the service is activated, it will listen
            to events from all applications.
//        accessibilityServiceInfo.packageNames = new String[]{"com.android.settings"};
         */
        /*
            Default services are invoked only if no package-specific ones are present
            for the type of AccessibilityEvent generated. This service *is*
            application-specific, so the flag isn't necessary. If this was a
            general-purpose service, it would be worth considering setting the
            DEFAULT flag.
         accessibilityServiceInfo.flags = AccessibilityServiceInfo.DEFAULT;
        */
//        Log.v(TAG, "getConfiguredServiceInfo: End.");
        return accessibilityServiceInfo;
    }

    @Override
    public void onCreate() {
        // The service is being created
//        Log.v(TAG, "onCreate: Invoked.");
        Log.v(TAG, "onCreate: Invoked.");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        Log.v(TAG, "onUnbind: Invoked.");
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
        Log.v(TAG, "onRebind: Invoked.");
    }

    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
        Log.v(TAG, "onDestroy: Invoked.");
    }

    @Override
    public void onInterrupt() {
        // TODO Auto-generated method stub
        Log.v(TAG, "onInterrupt: Invoked.");
    }
}