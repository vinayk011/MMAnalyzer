package com.hug.mma.constants;

/**
 * Created by Raviteja on 08-08-2017 for HugFit.
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public class EnumConstants {
    public enum Gender {
        FEMALE, MALE
    }

    public enum Scope {
        DAY, WEEK, MONTH
    }

    public enum MeasuringSystem {
        METRIC, IMPERIAL
    }

    public enum LifeStyleType {
        SEDENTARY,
        ACTIVE,
        VERYACTIVE
    }

    public enum WEEK {
        MONDAY(4, "Mon"), TUESDAY(8, "Tue"), WEDNESDAY(16, "Wed"), THURSDAY(32, "Thur"), FRIDAY(64, "Fri"),
        SATURDAY(128, "Sat"), SUNDAY(2, "Sun");

        public final int value;
        public final String name;

        WEEK(int i, String name) {
            this.value = i;
            this.name = name;
        }
    }
}