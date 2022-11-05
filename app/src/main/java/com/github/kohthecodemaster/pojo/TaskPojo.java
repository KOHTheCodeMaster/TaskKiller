package com.github.kohthecodemaster.pojo;

import androidx.annotation.NonNull;

public class TaskPojo {

    private String packageName;
    private boolean isForceStopBtnClicked;
    private boolean isOKBtnClicked;

    public TaskPojo(String packageName) {
        this.packageName = packageName;
    }

    @NonNull
    @Override
    public String toString() {
        return "TaskPojo ->" +
                "\npackageName - " + packageName +
                "\nisForceStopBtnClicked - " + isForceStopBtnClicked +
                "\nisOKBtnClicked - " + isOKBtnClicked;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isForceStopBtnClicked() {
        return isForceStopBtnClicked;
    }

    public void setForceStopBtnClicked(boolean forceStopBtnClicked) {
        isForceStopBtnClicked = forceStopBtnClicked;
    }

    public boolean isOKBtnClicked() {
        return isOKBtnClicked;
    }

    public void setOKBtnClicked(boolean OKBtnClicked) {
        isOKBtnClicked = OKBtnClicked;
    }
}
