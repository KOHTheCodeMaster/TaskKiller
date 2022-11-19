package com.github.kohthecodemaster.utils;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class SimulationHelper {

    private static final String TAG = "L0G-SimulationHelper";

    /**
     * Firstly, Find the node with text "Force Stop" & then check if that node is clickable?
     * if it's available then proceed for Simulation by performing CLICK Action on behalf of user to Force Stop the application.
     *
     * @param nodeInfo Source Node Info of the Accessibility Event
     * @return true when Force Stop Btn. is clicked, Otherwise false.
     */
    public static boolean simulateForceStop(AccessibilityNodeInfo nodeInfo) {

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
    public static boolean simulateOKBtnClick(AccessibilityNodeInfo nodeInfo) {

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

    public static void test1(AccessibilityNodeInfo nodeInfo) {

        String packageName = nodeInfo.getPackageName().toString();
        Log.v(TAG, "test1: packageName - " + packageName);

    }
}
