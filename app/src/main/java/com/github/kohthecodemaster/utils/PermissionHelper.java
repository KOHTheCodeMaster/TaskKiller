package com.github.kohthecodemaster.utils;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

public class PermissionHelper {

    private static final String TAG = "L0G-PermissionHelper";

    /**
     * Check if the user has permitted the accessibility permission or not.
     *
     * @return true when Accessibility Permission is available, Otherwise false.
     */
    public static boolean isAccessibilityPermissionNOTAvailable(Context context) {

        try {
            int temp = Settings.Secure.getInt(
                    context.getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED
            );

            return temp != 1;   //  1 -> Accessibility Permission is enabled

        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "isAccessibilityPermissionAvailable: Error finding setting, default accessibility is not found: " + e.getMessage());
            return false;
        }
    }

    /**
     * Prompt User to the System's Settings activity asking for Accessibility Permission.
     */
    public static void promptForAccessibilityPermission(Context context) {

//        Log.v(TAG, "promptForAccessibilityPermission: Begin.");

        // Construct intent to request Accessibility Permission
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Request permission by starting settings activity
        context.startActivity(intent);

//        Log.v(TAG, "promptForAccessibilityPermission: End.");

    }

}
