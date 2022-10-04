package com.github.kohthecodemaster.utils;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

public class PermissionHelper {

    private static final String TAG = "L0G-PermissionHelper";
    private final Context context;

    public PermissionHelper(Context context) {
        this.context = context;
    }

    /**
     * Check if the user has permitted the accessibility permission or not.
     *
     * @return true when Accessibility Permission is available, Otherwise false.
     */
    public boolean isAccessibilityPermissionAvailable() {

        try {
            int temp = Settings.Secure.getInt(
                    context.getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED
            );
            if (temp == 1)
                Log.v(TAG, "isAccessibilityPermissionAvailable: Accessibility Permission Available.");
            else
                Log.v(TAG, "isAccessibilityPermissionAvailable: Accessibility Permission Required.");
            return temp == 1;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "isAccessibilityPermissionAvailable: Error finding setting, default accessibility is not found: " + e.getMessage());
            return false;
        }
    }

    /**
     * Prompt User to the System's Settings activity asking for Accessibility Permission.
     */
    public void promptForAccessibilityPermission() {

//        Log.v(TAG, "promptForAccessibilityPermission: Begin.");

        // Construct intent to request Accessibility Permission
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Request permission by starting settings activity
        context.startActivity(intent);

//        Log.v(TAG, "promptForAccessibilityPermission: End.");

    }

}
