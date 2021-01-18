package com.hug.mma.model;


import com.hug.mma.constants.EnumConstants;

/**
 * Created by Raviteja on 22-08-2017 for HugFit.
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public class SummaryScope {
    private final int DAY = 0;
    private final int WEEK = 1;
    private final int MONTH = 2;
    private int current;

    public SummaryScope() {
        current = DAY;
    }

    public EnumConstants.Scope getCurrent() {
        if (current == DAY) {
            return EnumConstants.Scope.DAY;
        } else if (current == WEEK) {
            return EnumConstants.Scope.WEEK;
        } else if (current == MONTH) {
            return EnumConstants.Scope.MONTH;
        }
        return EnumConstants.Scope.DAY;
    }

    public EnumConstants.Scope setNext() {
        if (current == MONTH) {
            current = DAY;
        } else if (current == DAY) {
            current = WEEK;
        } else if (current == WEEK) {
            current = MONTH;
        }
        return getCurrent();
    }
}
