package com.aiton.zjb.signal.model;

import java.io.Serializable;

public class PermissionListEntity implements Serializable {
    private int id;
    private String permissionName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
}