package com.github.kohthecodemaster.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.List;

public class PackageHelper {

    private static final String TAG = "L0G-PackageHelper";
    private final Context context;
    private final ActivityManager activityManager;
    private final PackageManager packageManager;

    public PackageHelper(Context context) {
        this.context = context;
        this.activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        this.packageManager = context.getPackageManager();
    }

/*
    public void demoAll() {

        listMyRunningAppProcesses();

        listAllPackages();

        List<String> packagesToBeKilled = Arrays.asList(
                "com.google.android.youtube",
                "com.google.android.apps.youtube.music"
        );

        killBackgroundProcesses(packagesToBeKilled);

    }
*/

    /**
     * Get the list of processes only for the current package.
     *
     * @return list of RunningAppProcessInfo only for the current package
     */
    public List<ActivityManager.RunningAppProcessInfo> listMyRunningAppProcesses() {

        Log.v(TAG, "listMyRunningAppProcesses: Begin.");

        //  ActivityManager.getRunningAppProcesses() returns list of processes only for the current package.
        List<ActivityManager.RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();

        Log.v(TAG, "listMyRunningAppProcesses: Iterating over list [" +
                list.size() + "]");
        list.forEach(runningAppProcessInfo -> Log.v(TAG, runningAppProcessInfo.processName));

        Log.v(TAG, "listMyRunningAppProcesses: End.");
        return list;
    }

    /**
     * Get the list of all the packages that are currently installed in Android.
     *
     * @return list of all installed packages
     */
    public List<PackageInfo> listAllPackages() {

        Log.v(TAG, "listAllPackages: Begin.");
        Log.v(TAG, "listAllPackages: package - " + context.getPackageName());

//        List<ApplicationInfo> packageList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);   //  get a list of installed apps.
        List<PackageInfo> packageList = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);   //  get a list of installed apps.

        Log.v(TAG, "listAllPackages: Iterating over packageList [" + packageList.size() + "]");
        packageList.forEach(packageInfo -> Log.v(TAG, "Package: " + packageInfo.packageName));

        Log.v(TAG, "listAllPackages: End.");
        return packageList;
    }

    /**
     * Kill the background processes for the given package name.
     * Note: Killing Background Process won't "FORCE STOP" the application, but only clear their activity stack from Memory.
     *
     * @param packagesToBeKilled Name of the Package whose background processes needs to be killed.
     */
    private void killBackgroundProcesses(List<String> packagesToBeKilled) {

        Log.v(TAG, "killBackgroundProcesses: Begin.");

//        List<ApplicationInfo> packageList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);   //  get a list of installed apps
        List<PackageInfo> packageList = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);   //  get a list of installed packages
        Log.v(TAG, "killBackgroundProcesses: Total Packages To Be Killed - [" + packageList.size() + "]");

        packageList.forEach(packageInfo -> {
            Log.v(TAG, "killBackgroundProcesses: Package: " + packageInfo.packageName);
            if (packagesToBeKilled.contains(packageInfo.packageName))
                activityManager.killBackgroundProcesses(packageInfo.packageName);
        });

        Log.v(TAG, "killBackgroundProcesses: End.");
    }

}
