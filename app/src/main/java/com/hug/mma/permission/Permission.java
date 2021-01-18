package com.hug.mma.permission;

import android.Manifest;

/**
 * Created by Raviteja on 19-07-2017 for ehr
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public enum Permission {


    FINE_LOCATION(Manifest.permission.ACCESS_FINE_LOCATION),
    COARSE_LOCATION(Manifest.permission.ACCESS_COARSE_LOCATION);

    String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public static Permission stringToPermission(String stringPermission) {
        for (Permission permission : Permission.values()) {
            if (stringPermission.equalsIgnoreCase(permission.toString()))
                return permission;
        }

        return null;
    }

    @Override
    public String toString() {
        return permission;
    }
}