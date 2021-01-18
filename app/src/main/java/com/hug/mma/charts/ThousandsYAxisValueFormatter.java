package com.hug.mma.charts;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.Locale;

public class ThousandsYAxisValueFormatter implements IAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int new_value = Math.round(value);
        if (new_value != 0 && new_value % 1000 == 0) {
            return String.format(Locale.getDefault(), "%dk", new_value / 1000);
        } else if (new_value > 1000) {
            if (new_value % 1000 < 100) {
                return String.format(Locale.getDefault(), "%dk", new_value / 1000);
            } else {
                return String.format(Locale.getDefault(), "%.1fk", ((float) new_value) / 1000);
            }
        }
        return String.valueOf((int) value);
    }
}