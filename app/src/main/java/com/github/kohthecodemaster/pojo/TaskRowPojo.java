package com.github.kohthecodemaster.pojo;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class TaskRowPojo {

    private String title;
    private String packageName;
    private Drawable imgIcon;
    private boolean selected;

    public TaskRowPojo(String title, String packageName, Drawable imgIcon) {
        this.title = title;
        this.packageName = packageName;
        this.imgIcon = imgIcon;
    }

    public static TaskRowPojo generatePojo(PackageInfo packageInfo, PackageManager packageManager) {
        return new TaskRowPojo(packageInfo.applicationInfo.loadLabel(packageManager).toString(),
                packageInfo.applicationInfo.packageName,
                packageInfo.applicationInfo.loadIcon(packageManager));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getImgIcon() {
        return imgIcon;
    }

    public void setImgIcon(Drawable imgIcon) {
        this.imgIcon = imgIcon;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}