package com.github.kohthecodemaster.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.kohthecodemaster.R;
import com.github.kohthecodemaster.utils.PackageHelper;
import com.github.kohthecodemaster.utils.PermissionHelper;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "L0G-MainActivity";
    private PermissionHelper permissionHelper;
    private PackageHelper packageHelper;
    List<String> packagesToBeKilled;

    AlertDialog.Builder builderAlertDialog;

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
        Log.v(TAG, "onResume: Begin.");

        //  Popup Permission Accessibility Required Dialog
        if (!permissionHelper.isAccessibilityPermissionAvailable()) popupAlertDialog();

        Log.v(TAG, "onResume: End.");
    }

    private void major() {

        Log.v(TAG, "major: Begin.");

        init();

//        List<PackageInfo> packageList = packageHelper.listAllPackages();

        Log.v(TAG, "major: End.");

    }

    /**
     * Initialize instance members.
     */
    private void init() {
        permissionHelper = new PermissionHelper(getApplicationContext());
        packageHelper = new PackageHelper(getApplicationContext());
        builderAlertDialog = new AlertDialog.Builder(this);
        packagesToBeKilled = Arrays.asList(
                "com.google.android.youtube",
                "com.google.android.apps.youtube.music"
        );
    }

    /**
     * Display Alert Dialog to the user stating "Accessibility Permission Required"
     * Along with 2 Buttons:
     * 1. Allow -> Switch to Accessibility Permission Settings Activity.
     * 2. Quit -> Terminate the Task Killer application right away due to Permission Denied.
     */
    private void popupAlertDialog() {

        //  Initialize Alert Dialog Builder
        builderAlertDialog
                .setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title)
                .setCancelable(false)
                .setPositiveButton("Allow", (dialog, id) -> {
//                    finish();
                    Toast.makeText(getApplicationContext(), "Allow Accessibility Permission.", Toast.LENGTH_SHORT).show();
                    permissionHelper.promptForAccessibilityPermission();
                })
                .setNegativeButton("Quit", (dialog, id) -> {
                    //  Action for 'NO' Button
                    dialog.cancel();
//                    finish();
                    Toast.makeText(getApplicationContext(), "Accessibility Permission Required!\n" +
                            "Application Terminated..!!", Toast.LENGTH_LONG).show();
                    Log.v(TAG, "onClick: Accessibility Permission Required!\tApplication Terminated..!!");
                    MainActivity.this.finish();
                });

        AlertDialog alert = builderAlertDialog.create();
        alert.show();
    }

    public void onPowerKillBtnClickListener(View view) {
        Log.v(TAG, "init: Power Kill Btn. Clicked.");
        processPackagesToBeKilled(packagesToBeKilled);
    }

    private void processPackagesToBeKilled(List<String> packageList) {
        packageList.forEach(this::launchApplicationSettings);
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

    private void requestAccessibilityPermission() {
        if (permissionHelper.isAccessibilityPermissionAvailable())
            Log.v(TAG, "testAccessibilityPermission: Accessibility Permission Available.");
        else popupAlertDialog();
    }


}

