package com.github.kohthecodemaster.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.github.kohthecodemaster.activity.MainActivity;
import com.github.kohthecodemaster.misc.TKServiceBinder;

import java.util.List;

public class TaskKillerAccessibilityService extends AccessibilityService {

    private static final String TAG = "L0G-TaskKillerAccessibilityService";
    private static volatile TaskKillerAccessibilityService service;
    public static TKServiceBinder tkServiceBinder;

    static {
        tkServiceBinder = MainActivity.tkServiceBinder;
    }

    @Override
    public void onServiceConnected() {

        Log.v(TAG, "onServiceConnected: Begin.");

//        getServiceInstance();

        this.setServiceInfo(getConfiguredServiceInfo());

        tkServiceBinder.emitService(this);

        Log.v(TAG, "onServiceConnected: End.");

    }

    public TaskKillerAccessibilityService getServiceInstance() {

//        Log.v(TAG, "getServiceInstance: Invoked.");

        if (service == null) {
            synchronized (TaskKillerAccessibilityService.class) {
                if (service == null) {
                    service = new TaskKillerAccessibilityService();
                    service.setServiceInfo(getConfiguredServiceInfo());
                } else {
                    Log.v(TAG, "[WARNING] getServiceInstance: Else Block Invoked.");
                }
            }
        }

        Log.v(TAG, "getServiceInstance: Service - " + service);
        return service;
    }

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

        /*
            If you only want this service to work with specific applications, set their
            package names here. Otherwise, when the service is activated, it will listen
            to events from all applications.
//        accessibilityServiceInfo.packageNames = new String[]{"abc123"};
//        accessibilityServiceInfo.packageNames = new String[]{"android.settings"};
         */
        /*
            Default services are invoked only if no package-specific ones are present
            for the type of AccessibilityEvent generated. This service *is*
            application-specific, so the flag isn't necessary. If this was a
            general-purpose service, it would be worth considering setting the
            DEFAULT flag.
//         accessibilityServiceInfo.flags = AccessibilityServiceInfo.DEFAULT;
        */
//        Log.v(TAG, "getConfiguredServiceInfo: End.");
        return accessibilityServiceInfo;
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

        Log.v(TAG, "onAccessibilityEvent: Begin.");
        if (AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED == event.getEventType()) {
            AccessibilityNodeInfo nodeInfo = event.getSource();

            if (nodeInfo == null) return;

//            List<AccessibilityNodeInfo> nodeInfoForceStopList = nodeInfo.findAccessibilityNodeInfosByText("Force Stop");
//            List<AccessibilityNodeInfo> nodeInfoOKList = nodeInfo.findAccessibilityNodeInfosByText("OK");

            simulateForceStop(nodeInfo);
//            simulateOKBtnClick(nodeInfo);

            // recycle the nodeInfo object
            nodeInfo.recycle();

        }
        Log.v(TAG, "onAccessibilityEvent: End.");

    }

    /**
     * Firstly, Find the node with text "Force Stop" & then check if that node is clickable?
     * if it's available then proceed for Simulation by performing CLICK Action on behalf of user to Force Stop the application.
     *
     * @param nodeInfo Source Node Info of the Accessibility Event
     * @return true when Force Stop Btn. is clicked, Otherwise false.
     */
    private boolean simulateForceStop(AccessibilityNodeInfo nodeInfo) {

        Log.v(TAG, "simulateForceStop: Begin Force Stop Simulation.");
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
        Log.v(TAG, "simulateForceStop: End.");
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
    public void onCreate() {
        // The service is being created
//        Log.v(TAG, "onCreate: Invoked.");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        Log.v(TAG, "onUnbind: Invoked.");
        tkServiceBinder.emitService(null);
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
        TaskKillerAccessibilityService.service = null;
    }

    @Override
    public void onInterrupt() {
        // TODO Auto-generated method stub
        Log.v(TAG, "onInterrupt: Invoked.");
    }
}