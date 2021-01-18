package com.hug.mma.util;

/**
 * Created by Raviteja on 23-08-2017 for HugFit.
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public class IntUtil {

    public static int getValueAsInt(long value) {
        if ( value < (long) Integer.MAX_VALUE ) {
            return (int) value;
        }
        return 0;
    }

    public static float get90Per(float value) {
        return (value * 90) / 100;
    }

    public static int getAsPer(int value, int divideBy) {
        if (divideBy == 0) {
            return 0;
        }
        int finalValue = (int) (((float) value * 100) / divideBy);
        return finalValue >= 100 ? 100 : finalValue;
    }

    public static float get110Per(float value) {
        return (value * 110) / 100;
    }
}