package com.hug.mma.permission;

/**
 * Created by Raviteja on 24-07-2017 for ehr
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public interface PermissionCallback {
    void onPermissionResult(boolean granted, boolean neverAsk);
}