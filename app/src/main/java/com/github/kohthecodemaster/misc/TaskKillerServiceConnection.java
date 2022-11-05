package com.github.kohthecodemaster.misc;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.github.kohthecodemaster.service.TaskKillerAccessibilityService;

public class TaskKillerServiceConnection implements ServiceConnection {

    private static final String TAG = "L0G-TaskKillerServiceConnection";
    private TaskKillerAccessibilityService service;

    @Override
    public void onBindingDied(ComponentName name) {
        ServiceConnection.super.onBindingDied(name);
        Log.v(TAG, "onBindingDied: Invoked.");

    }

    @Override
    public void onNullBinding(ComponentName name) {
        ServiceConnection.super.onNullBinding(name);
        Log.v(TAG, "onNullBinding: Invoked.");
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        Log.v(TAG, "onServiceConnected: Begin.");

        // We've bound to TaskKillerAccessibilityService, cast the IBinder and get service instance
//        TaskKillerAccessibilityService.LocalBinder binder = (TaskKillerAccessibilityService.LocalBinder) iBinder;
//        this.service = binder.getService();
//        this.service.setServiceInfo(getConfiguredServiceInfo());
//
//        Log.v(TAG, "onServiceConnected: Service: " + this.service);

        Log.v(TAG, "onServiceConnected: End.");

    }

    private AccessibilityServiceInfo getConfiguredServiceInfo() {

        Log.v(TAG, "getConfiguredServiceInfo: Begin.");

        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();

        // Set the type of events that this service wants to listen to.
        accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
//                | AccessibilityEvent.TYPE_VIEW_CLICKED
//                | AccessibilityEvent.TYPE_VIEW_FOCUSED
//                | AccessibilityEvent.TYPES_ALL_MASK
        ;

        /*
            If you only want this service to work with specific applications, set their
            package names here. Otherwise, when the service is activated, it will listen
            to events from all applications.
         */
        accessibilityServiceInfo.packageNames = new String[]{"abc123"};
//        accessibilityServiceInfo.packageNames = new String[]{"android.settings"};

        // Set the type of feedback your service will provide.
        accessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;

        /*
            Default services are invoked only if no package-specific ones are present
            for the type of AccessibilityEvent generated. This service *is*
            application-specific, so the flag isn't necessary. If this was a
            general-purpose service, it would be worth considering setting the
            DEFAULT flag.
        */
        // accessibilityServiceInfo.flags = AccessibilityServiceInfo.DEFAULT;

        accessibilityServiceInfo.notificationTimeout = 100;

        Log.v(TAG, "getConfiguredServiceInfo: End.");

        return accessibilityServiceInfo;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.v(TAG, "onServiceDisconnected: Begin.");
        this.service = null;
        Log.v(TAG, "onServiceDisconnected: End.");
    }

    public TaskKillerAccessibilityService getService() {
        return service;
    }

}
