package com.hug.mma.charts;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.hug.mma.util.Trace;

public class BatchAxisValueFormatter implements IAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        value = (int) value;
        if (value < 0) {
            return "";
        }
        Trace.i("xaxis value: " + value);
        return "B" + String.valueOf((int) value);
    }
}