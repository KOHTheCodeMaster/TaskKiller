package com.github.kohthecodemaster.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class TaskKillerAccessibilityService extends AccessibilityService {

    private static final String TAG = "L0G-TaskKillerAccessibilityService";

    /**
     * This method is invoked as soon as the application acquires Accessibility Permission.
     * It initializes AccessibilityServiceInfo to keep track & process the
     * Accessibility Events anytime the UI Window State is changed.
     */
    @Override
    public void onServiceConnected() {

        Log.v(TAG, "onServiceConnected: Begin.");
        AccessibilityServiceInfo serviceInfo = new AccessibilityServiceInfo();

        // Set the type of events that this service wants to listen to.
        // Others won't be passed to this service.
        serviceInfo.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
//                | AccessibilityEvent.TYPE_VIEW_CLICKED
//                | AccessibilityEvent.TYPE_VIEW_FOCUSED
//                | AccessibilityEvent.TYPES_ALL_MASK
        ;

        /*
            If you only want this service to work with specific applications, set their
            package names here. Otherwise, when the service is activated, it will listen
            to events from all applications.
         */
//        serviceInfo.packageNames = new String[]
//                {"com.example.android.myFirstApp", "com.example.android.mySecondApp"};

        // Set the type of feedback your service will provide.
        serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;

        /*
            Default services are invoked only if no package-specific ones are present
            for the type of AccessibilityEvent generated. This service *is*
            application-specific, so the flag isn't necessary. If this was a
            general-purpose service, it would be worth considering setting the
            DEFAULT flag.
        */
        // serviceInfo.flags = AccessibilityServiceInfo.DEFAULT;

        serviceInfo.notificationTimeout = 100;

        this.setServiceInfo(serviceInfo);
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

//        Log.v(TAG, "onAccessibilityEvent: Begin.");
        if (AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED == event.getEventType()) {
            AccessibilityNodeInfo nodeInfo = event.getSource();

            if (nodeInfo == null) return;

//            List<AccessibilityNodeInfo> nodeInfoForceStopList = nodeInfo.findAccessibilityNodeInfosByText("Force Stop");
//            List<AccessibilityNodeInfo> nodeInfoOKList = nodeInfo.findAccessibilityNodeInfosByText("OK");

            simulateForceStop(nodeInfo);
            simulateOKBtnClick(nodeInfo);

            // recycle the nodeInfo object
            nodeInfo.recycle();

        }
//        Log.v(TAG, "onAccessibilityEvent: End.");

    }

    /**
     * Firstly, Find the node with text "Force Stop" & then check if that node is clickable?
     * if it's available then proceed for Simulation by performing CLICK Action on behalf of user to Force Stop the application.
     *
     * @param nodeInfo Source Node Info of the Accessibility Event
     * @return true when Force Stop Btn. is clicked, Otherwise false.
     */
    private boolean simulateForceStop(AccessibilityNodeInfo nodeInfo) {

//        Log.v(TAG, "simulateForceStop: Begin Force Stop Simulation.");
        List<AccessibilityNodeInfo> list;

        //  Find nodes with text "Force Stop"
        //  this.getRootInActiveWindow() -> Get Root Window - Not sure about how to use this to find force stop btn Id.
        list = nodeInfo.findAccessibilityNodeInfosByText("Force Stop");
        if (list.isEmpty()) return false;

        AccessibilityNodeInfo nodeForceStopBtn = list.get(0);
        if (nodeForceStopBtn.isClickable()) {
            nodeForceStopBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Log.d(TAG, "simulateForceStop: Clicked on Force Stop Btn.");

            return true;
        }
//        Log.v(TAG, "simulateForceStop: End.");
        return false;

    }

    /**
     * Firstly, Find the node with text "OK" & then check if that node is clickable?
     * if it's available then proceed for Simulation by performing CLICK Action on behalf of user to
     * Click on OK Button in order to force stop the application.
     *
     * @param nodeInfo Source Node Info of the Accessibility Event
     * @return true when OK Btn. is clicked, Otherwise false.
     */
    private boolean simulateOKBtnClick(AccessibilityNodeInfo nodeInfo) {

//        Log.v(TAG, "simulateOKBtnClick: Begin.");
        List<AccessibilityNodeInfo> list;

        //We can find button using button name or button id
        list = nodeInfo.findAccessibilityNodeInfosByText("OK");
        if (list.isEmpty()) return false;

        AccessibilityNodeInfo nodeOKBtn = list.get(0);
        if (nodeOKBtn.isClickable()) {
            nodeOKBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Log.d(TAG, "simulateOKBtnClick: Clicked on OK Btn.");
            return true;
        }
//        Log.v(TAG, "simulateOKBtnClick: End.");
        return false;

    }

    @Override
    public void onInterrupt() {
        // TODO Auto-generated method stub
        Log.v(TAG, "onInterrupt: Invoked.");
    }
}