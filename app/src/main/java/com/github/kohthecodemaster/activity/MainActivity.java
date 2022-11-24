package com.github.kohthecodemaster.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.kohthecodemaster.R;
import com.github.kohthecodemaster.misc.AppListAdapter;
import com.github.kohthecodemaster.pojo.TaskRowPojo;
import com.github.kohthecodemaster.service.TaskKillerAccessibilityService;
import com.github.kohthecodemaster.utils.PermissionHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "L0G-MainActivity";
    private AlertDialog.Builder alertDialogBuilder;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(TAG, "onCreate: Begin. " + this.hashCode());
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
        if (PermissionHelper.isAccessibilityPermissionNOTAvailable(getApplicationContext()))
            popupAlertDialog();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.v(TAG, "onNewIntent: Invoked.");
    }

    private void major() {

//        Log.v(TAG, "major: Begin.");

        init();

//        Log.v(TAG, "major: End.");

    }

    /**
     * Initialize instance members.
     */
    private void init() {

//        Log.v(TAG, "init: Begin.");

        initializeBuilderAlertDialog();

        TaskKillerAccessibilityService.tasksToBeKilled = new LinkedList<>();

        //  Populate list of installed applications along with their Logo in UI
        try {
            initializeRecyclerView();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

//        Log.v(TAG, "init: End.");

    }

    private void initializeRecyclerView() throws PackageManager.NameNotFoundException {

//        TaskRowPojo[] arrAppListData = obtainTaskRowPojosForInstalledApps();
        TaskRowPojo[] arrAppListData = new TaskRowPojo[2];
        arrAppListData[0] = new TaskRowPojo("Youtube", "com.google.android.youtube", getPackageManager().getApplicationIcon("com.google.android.youtube"));
        arrAppListData[1] = new TaskRowPojo("Youtube Music", "com.google.android.apps.youtube.music", getPackageManager().getApplicationIcon("com.google.android.apps.youtube.music"));
        updateSelectedAppsTextView("Selected 0 / " + arrAppListData.length);
        /*TaskRowPojo[] arrAppListData;
        try {
            arrAppListData = new TaskRowPojo[]{
                    new TaskRowPojo("Youtube", "com.google.android.youtube", getPackageManager().getApplicationIcon("com.google.android.youtube")),
                    new TaskRowPojo("Youtube Music", "com.google.android.apps.youtube.music", getPackageManager().getApplicationIcon("com.google.android.apps.youtube.music")),
            };
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "initializeRecyclerView(): Error - Failed to Initialize TaskRowPojo.");
            e.printStackTrace();
            return;
        }*/
        AppListAdapter adapter = new AppListAdapter(arrAppListData, this::updateSelectedAppsTextView);

        recyclerView = findViewById(R.id.idRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    private TaskRowPojo[] obtainTaskRowPojosForInstalledApps() {

        List<TaskRowPojo> taskRowPojoList = new ArrayList<>();
        List<ApplicationInfo> appInfoList = getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        appInfoList.stream()
                .filter(applicationInfo -> (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0 ||
                        (applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)
                .forEach(appInfo -> taskRowPojoList.add(TaskRowPojo.generatePojo(appInfo, getPackageManager())));

        return taskRowPojoList.toArray(new TaskRowPojo[0]);
    }

    public void updateSelectedAppsTextView(String strText) {
        TextView appsSelectedTextView = findViewById(R.id.idAppsSelectedTextView);
        appsSelectedTextView.setText(strText);
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

//        Log.v(TAG, "onPowerKillBtnClickListener: Begin.");

        //  Launch App Settings for the first App from tasksToBeKilled
        if (TaskKillerAccessibilityService.tasksToBeKilled != null && TaskKillerAccessibilityService.tasksToBeKilled.size() > 0)
            launchApplicationSettings(TaskKillerAccessibilityService.tasksToBeKilled.get(0).getPackageName());

//        Log.v(TAG, "onPowerKillBtnClickListener: End.");

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

    private void onAllowBtnDialogClick(DialogInterface dialog, int id) {
        Log.v(TAG, "Alert Dialog - setPositiveButton(): 'Allow' Btn. Clicked.");
        PermissionHelper.promptForAccessibilityPermission(this);
    }

    private void onQuitBtnDialogClick(DialogInterface dialog, int id) {
        String strMsg = "Accessibility Permission Required!\tApplication Terminated..!!";
        Log.v(TAG, "Alert Dialog - setNegativeButton(): 'Quit' Btn. Clicked.\n" + strMsg);
        Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();

        dialog.cancel();
        MainActivity.this.finish();
    }

}

