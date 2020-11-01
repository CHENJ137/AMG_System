package com.example.amg_system.domain;

import android.graphics.drawable.Drawable;

/**
 * Single task info
 */
public class TaskInfo {
    private Drawable icon;
    private String name;
    private String packname;

    private long meninfosize;

    private boolean isUser;

    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackname() {
        return packname;
    }

    public void setPackname(String packname) {
        this.packname = packname;
    }

    public long getMeninfosize() {
        return meninfosize;
    }

    public void setMeninfosize(long meninfosize) {
        this.meninfosize = meninfosize;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }
}
