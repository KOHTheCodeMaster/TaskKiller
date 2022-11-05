package com.github.kohthecodemaster.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.kohthecodemaster.R;
import com.github.kohthecodemaster.misc.TKServiceBinder;
import com.github.kohthecodemaster.misc.TaskKillerServiceConnection;
import com.github.kohthecodemaster.pojo.TaskPojo;
import com.github.kohthecodemaster.service.TaskKillerAccessibilityService;
import com.github.kohthecodemaster.utils.PackageHelper;
import com.github.kohthecodemaster.utils.PermissionHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "L0G-MainActivity";
    private PermissionHelper permissionHelper;
    private PackageHelper packageHelper;
    private TaskKillerAccessibilityService taskKillerAccessibilityService;
    private AlertDialog.Builder alertDialogBuilder;
    private TaskKillerServiceConnection taskKillerServiceConnection;
    public static TKServiceBinder tkServiceBinder;

    List<TaskPojo> tasksToBeKilled;
    private boolean isServiceActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(TAG, "onCreate: Begin.");
        this.major();
        Log.v(TAG, "onCreate: End.");

    }

    /**
     * Validate Accessibility Permission everytime the application is resumed.
     * In case of Permission is required,
     * display Alert Dialog prompting user to provide Accessibility Permission
     */
    @Override
    protected void onResume() {

        super.onResume();

        //  Popup Permission Accessibility Required Dialog
        if (PermissionHelper.isAccessibilityPermissionNOTAvailable(getApplicationContext())) popupAlertDialog();


    }

    private void major() {

//        Log.v(TAG, "major: Begin.");

        init();

        //  Populate list of installed applications along with their Logo in UI

        //  Power Kill Btn. will start Killing Apps that are selected by user

//        List<PackageInfo> packageList = packageHelper.listAllPackages();

//        Log.v(TAG, "major: End.");

    }

    /**
     * Initialize instance members.
     */
    private void init() {

//        Log.v(TAG, "init: Begin.");

        permissionHelper = new PermissionHelper(getApplicationContext());
        packageHelper = new PackageHelper(getApplicationContext());
        taskKillerServiceConnection = new TaskKillerServiceConnection();
//        taskKillerAccessibilityService = TaskKillerAccessibilityService.getServiceInstance();

        initializeBuilderAlertDialog();
        initializeTasksToBeKilledByDefault();
        initializeTKServiceBinder();

//        Log.v(TAG, "init: End.");

    }

    private void initializeTKServiceBinder() {
        tkServiceBinder = service -> {
            Log.d(TAG, "tkServiceBinder: Invoked.");
//            MainActivity.this.taskKillerAccessibilityService = service;
            this.taskKillerAccessibilityService = service;
            this.isServiceActive = service != null;
        };
    }

    private void initializeBuilderAlertDialog() {

        //  Initialize Alert Dialog Builder
        alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder
                .setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title)
                .setCancelable(false)
                .setPositiveButton("Allow", this::onAllowBtnDialogClick)
                .setNegativeButton("Quit", this::onQuitBtnDialogClick);
    }

    private void initializeTasksToBeKilledByDefault() {

        tasksToBeKilled = new ArrayList<>();

        TaskPojo taskPojo = new TaskPojo("com.google.android.youtube");
        tasksToBeKilled.add(taskPojo);

        taskPojo = new TaskPojo("com.google.android.apps.youtube.music");
        tasksToBeKilled.add(taskPojo);

    }

    /**
     * Display Alert Dialog to the user stating "Accessibility Permission Required"
     * Along with 2 Buttons:
     * 1. Allow -> Switch to Accessibility Permission Settings Activity.
     * 2. Quit -> Terminate the Task Killer application right away due to Permission Denied.
     */
    private void popupAlertDialog() {

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    public void onPowerKillBtnClickListener(View view) {
        Log.v(TAG, "onPowerKillBtnClickListener: Begin.");

        /*
            Steps to Kill Application:
            Pass packagesToBeKilled list to TaskKillerService
            Activate
            1. Launch Application Settings Activity
            2.
         */

        //  Activate Task Killer Service
//        if (!isServiceActive) activateTaskKillerService();


        //  Process package list selected by user
        //        processPackagesToBeKilled(packagesToBeKilled);
        tasksToBeKilled.forEach(taskPojo -> launchApplicationSettings(taskPojo.getPackageName()));

//        unbindService(taskKillerServiceConnection);

        Log.v(TAG, "onPowerKillBtnClickListener: End.");

    }

    private void activateTaskKillerService() {

        Log.v(TAG, "activateTaskKillerService: Begin.");

        Log.v(TAG, "activateTaskKillerService: taskKillerAccessibilityService: " + taskKillerAccessibilityService);

        Log.v(TAG, "activateTaskKillerService: End.");

    }

    private void processPackagesToBeKilled(List<String> packageList) {
    }

    /**
     * Launch the Application Settings Activity for the given package name.
     *
     * @param packageName Name of the package whose Settings Activity needs to be opened up.
     */
    private void launchApplicationSettings(String packageName) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", packageName, null));
        startActivity(intent);
    }


    public void onConnectBtnClickListener(View view) {
        Log.v(TAG, "onConnectBtnClickListener: Begin.");

//        this.isServiceActive = true;
//        this.taskKillerAccessibilityService = TaskKillerAccessibilityService.getServiceInstance();
        Log.v(TAG, "onConnectBtnClickListener: service - " + this.taskKillerAccessibilityService);

        Intent intent = new Intent(this, TaskKillerAccessibilityService.class);
        startService(intent);
//        bindService(intent, taskKillerServiceConnection, BIND_AUTO_CREATE);
//        taskKillerAccessibilityService.bindService(intent, taskKillerServiceConnection, BIND_AUTO_CREATE);

        Log.v(TAG, "onConnectBtnClickListener: End.");
    }

    public void onDisconnectBtnClickListener(View view) throws InterruptedException {
        Log.v(TAG, "onDisconnectBtnClickListener: Begin.");

//        this.isServiceActive = true;
//        this.taskKillerAccessibilityService = TaskKillerAccessibilityService.getServiceInstance();

        Log.v(TAG, "onDisconnectBtnClickListener: service - " + this.taskKillerAccessibilityService);

        taskKillerAccessibilityService.stopSelf();
//        stopService(new Intent(this, TaskKillerAccessibilityService.class));
//        taskKillerAccessibilityService.stopService(new Intent(this, TaskKillerAccessibilityService.class));
//        unbindService(taskKillerServiceConnection);

        Thread.sleep(1000);

//        Log.v(TAG, "onDisconnectBtnClickListener: service2 - " +
//                TaskKillerAccessibilityService.tkServiceBinder.emitService());

        Log.v(TAG, "onDisconnectBtnClickListener: End.");
    }

    private void onAllowBtnDialogClick(DialogInterface dialog, int id) {
        Log.v(TAG, "Alert Dialog - setPositiveButton(): 'Allow' Btn. Clicked.");
        Toast.makeText(getApplicationContext(), "Allow Accessibility Permission.", Toast.LENGTH_SHORT).show();
        permissionHelper.promptForAccessibilityPermission();
    }

    private void onQuitBtnDialogClick(DialogInterface dialog, int id) {
        String strMsg = "Accessibility Permission Required!\tApplication Terminated..!!";
        Log.v(TAG, "Alert Dialog - setNegativeButton(): 'Quit' Btn. Clicked.\n" + strMsg);
        Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();

        dialog.cancel();
        MainActivity.this.finish();
    }

}

