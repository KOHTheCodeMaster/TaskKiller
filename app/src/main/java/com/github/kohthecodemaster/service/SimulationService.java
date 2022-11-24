package com.github.kohthecodemaster.service;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class SimulationService {

    private static final String TAG = "L0G-SimulationService";
    private boolean forceStopClicked;
    private boolean okClicked;

    public void simulateMajor(AccessibilityNodeInfo nodeInfo, Runnable runWhenAppIsStopped) {

        Runnable runnable = () -> {

            if (!forceStopClicked) simulateForceStop(nodeInfo, runWhenAppIsStopped);
            else simulateOKBtnClick(nodeInfo, runWhenAppIsStopped);

            nodeInfo.recycle();     // recycle the nodeInfo object

        };

        Thread thread = new Thread(runnable);
        thread.start();

    }

    /**
     * Firstly, Find the node with text "Force Stop" & then check if that node is clickable?
     * if it's available then proceed for Simulation by performing CLICK Action on behalf of user to Force Stop the application.
     *
     * @param nodeInfo Source Node Info of the Accessibility Event
     * @param runWhenAppAlreadyStopped
     */
    public void simulateForceStop(AccessibilityNodeInfo nodeInfo, Runnable runWhenAppAlreadyStopped) {

//        Log.v(TAG, "simulateForceStop: Begin Force Stop Simulation.");
        List<AccessibilityNodeInfo> list;
        String idForceBtn = "com.android.settings:id/button3";

//        list = nodeInfo.findAccessibilityNodeInfosByText("Force Stop");
        list = nodeInfo.findAccessibilityNodeInfosByViewId(idForceBtn);
        if (list.isEmpty()) return;

        Log.v(TAG, "simulateForceStop: Begin Force Stop Simulation.");

        AccessibilityNodeInfo nodeForceStopBtn = list.get(0);
        if (nodeForceStopBtn.isClickable()) {
            forceStopClicked = nodeForceStopBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            if (forceStopClicked) Log.v(TAG, "simulateForceStop: Clicked on Force Stop Btn.");
            else {
                Log.v(TAG, "simulateForceStop: App already Force Stopped.");
                runWhenAppAlreadyStopped.run();
            }
        }
        Log.v(TAG, "simulateForceStop: End.");

    }

    /**
     * Firstly, Find the node with text "OK" & then check if that node is clickable?
     * if it's available then proceed for Simulation by performing CLICK Action on behalf of user to
     * Click on OK Button in order to force stop the application.
     *
     * @param nodeInfo Source Node Info of the Accessibility Event
     * @param runWhenAppIsStopped
     */
    public void simulateOKBtnClick(AccessibilityNodeInfo nodeInfo, Runnable runWhenAppIsStopped) {

        Log.v(TAG, "simulateOKBtnClick: Begin.");
        List<AccessibilityNodeInfo> list;
        String idOkBtn = "android:id/button1";

        //  Find nodes with text "Force Stop" or button id
//        list = nodeInfo.findAccessibilityNodeInfosByText("OK");
        list = nodeInfo.findAccessibilityNodeInfosByViewId(idOkBtn);
        if (list.isEmpty()) return;

        AccessibilityNodeInfo nodeOKBtn = list.get(0);
        if (nodeOKBtn.isClickable()) {
            okClicked = nodeOKBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            if (okClicked) {
                Log.d(TAG, "simulateOKBtnClick: Clicked on OK Btn.");
                runWhenAppIsStopped.run();
            }
        }
        Log.v(TAG, "simulateOKBtnClick: End.");

    }

    public void resetFlags() {
        forceStopClicked = false;
        okClicked = false;
        Log.v(TAG, "Reset Flags invoked.");
    }

    public boolean isOkClicked() {
        return okClicked;
    }

}

