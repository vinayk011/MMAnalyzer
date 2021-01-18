package com.hug.mma.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.hug.mma.R;
import com.hug.mma.charts.BatchAxisValueFormatter;
import com.hug.mma.charts.ThousandsYAxisValueFormatter;
import com.hug.mma.constants.AppConstants;
import com.hug.mma.constants.EnumConstants;
import com.hug.mma.databinding.ViewBarChartBinding;
import com.hug.mma.db.room.entity.DeviceStats;
import com.hug.mma.listener.ValueSelectedListener;
import com.hug.mma.util.IntUtil;
import com.hug.mma.util.Trace;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import static com.hug.mma.constants.AppConstants.BAD;
import static com.hug.mma.constants.AppConstants.GOOD;

public class DeviceStatsChart extends LinearLayout {

    private ViewBarChartBinding viewBarChartBinding;
    private BarData d = new BarData();
    private final float chartPadding = 0.5f;
    private final static int yAxisLabelCount = 7;

    private float chartAxisLimit = 30f;
    private float chartAxisCorrection = 20f;
    private int startIndex = 0;
    private int endIndex;
    private int xAxisLabelCount;
    private ValueSelectedListener valueSelectedListener;

    private int goal;
    private EnumConstants.Scope scope;
    private Date date;

    public DeviceStatsChart(Context context) {
        super(context);
        init(context);
    }

    public DeviceStatsChart(Context context, ValueSelectedListener listener) {
        super(context);
        init(context);
        this.valueSelectedListener = listener;
    }

    public DeviceStatsChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public DeviceStatsChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        viewBarChartBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_bar_chart, this, true);
    }

    public void setHorizontalLegend(String text) {
        viewBarChartBinding.horizontalLegend.setText(text);
    }

    public void setVerticalLegend(String text) {
        viewBarChartBinding.verticalLegend.setText(text);
    }

    public void setLineChartProperties() {
        viewBarChartBinding.barChart.getDescription().setText("");
        viewBarChartBinding.barChart.setScaleEnabled(true);
        viewBarChartBinding.barChart.setDrawGridBackground(false);
        viewBarChartBinding.barChart.setPinchZoom(false);
        viewBarChartBinding.barChart.setDoubleTapToZoomEnabled(false);
        viewBarChartBinding.barChart.setTouchEnabled(true);
        viewBarChartBinding.barChart.setNoDataText("No data available.");
        viewBarChartBinding.barChart.setHighlightPerTapEnabled(true);
        viewBarChartBinding.barChart.setNoDataTextColor(ContextCompat.getColor(getContext(), R.color.colorLogoYellow));
        viewBarChartBinding.barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Trace.i("");
                valueSelectedListener.onValueSelected((int) e.getX());
            }

            @Override
            public void onNothingSelected() {
                valueSelectedListener.onValueSelected(-1);
            }
        });
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public void changeScope(EnumConstants.Scope scope, Date date) {
        if (this.date != date || this.scope != scope) {
            d.clearValues();
        }
        if (date != null && this.date != date) {
            this.date = date;
        }
        if (this.scope != scope) {
            this.scope = scope;
        }
        viewBarChartBinding.barChart.clear();
        setChartValues();
        setChartXAxisValues();
        setChartYAxisValues();
    }

    private void setChartValues() {
        switch (scope) {
            case DAY:
                startIndex = 0;
                endIndex = 23;
                xAxisLabelCount = 12;
                chartAxisCorrection = 200;
                break;
            case WEEK:
                startIndex = 0;
                endIndex = 6;
                xAxisLabelCount = 7;
                chartAxisCorrection = 1000;
                break;
            case MONTH:
                startIndex = 1;
                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.SUNDAY);
                calendar.setTime(date);
                endIndex = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                xAxisLabelCount = endIndex / 2;
                chartAxisCorrection = 1000;
                break;
        }

    }

    private void setChartXAxisValues() {
        XAxis xAxis = viewBarChartBinding.barChart.getXAxis();
        IAxisValueFormatter xAxisFormatter = (value, axis) -> String.valueOf((int) value);

        switch (scope) {
            case DAY:
                xAxisFormatter = new BatchAxisValueFormatter();
                break;
            /*case WEEK:
                xAxisFormatter = new WeekAxisValueFormatter();
                break;
            case MONTH:
                xAxisFormatter = new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return DateUtil.getMonthAxisLabelValue(endIndex, value);
                    }
                };
                break;*/

        }
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setDrawLabels(true);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(8f);
        xAxis.setAxisMinimum(startIndex - chartPadding);
        xAxis.setAxisMaximum(endIndex + chartPadding);
        if (EnumConstants.Scope.MONTH != scope) {
            xAxis.setLabelCount(xAxisLabelCount, false);
        } else {
            xAxis.setLabelCount(endIndex);
        }
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    private void setChartYAxisValues() {
        IAxisValueFormatter yAxisFormatter = new ThousandsYAxisValueFormatter();
        YAxis yAxis = viewBarChartBinding.barChart.getAxisLeft();
        yAxis.setTextColor(Color.WHITE);
        yAxis.setAxisMaximum(chartAxisLimit);
        yAxis.setAxisMinimum(chartPadding);
        yAxis.setLabelCount(yAxisLabelCount, false);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawAxisLine(false);
        yAxis.setAxisLineColor(Color.WHITE);
        yAxis.setTextColor(Color.WHITE);
        yAxis.setTextSize(8f);
        yAxis.setGridColor(Color.WHITE);
        yAxis.setValueFormatter(yAxisFormatter);
        YAxis axisRight = viewBarChartBinding.barChart.getAxisRight();
        axisRight.setDrawGridLines(false);
        axisRight.setDrawAxisLine(false);
        axisRight.setEnabled(false);
    }

    public boolean isValid(EnumConstants.Scope scope, Date date) {
        return scope == this.scope && date == this.date;
    }

    public void insertData(List<DeviceStats> deviceStatsList) {
        d.clearValues();
        if (deviceStatsList != null && deviceStatsList.size() > 0) {
            d.setHighlightEnabled(false);
            d.setDrawValues(false);
            ArrayList<BarEntry> goodEntries = new ArrayList<>();
            ArrayList<BarEntry> badEntries = new ArrayList<>();
            ArrayList<BarEntry> mastitisEntries = new ArrayList<>();
            //Todo max sould be 0, kept 10 for temporary
            int max = 10;
            int index = 0;
            endIndex = deviceStatsList.size() - 1;
            if (EnumConstants.Scope.DAY == this.scope) {
                d.setBarWidth(0.5f);
                for (DeviceStats stats : deviceStatsList) {
                    try {
                        if (stats.getQuantity() > max) {
                            max = stats.getQuantity();
                        }
                        if (stats.getQuality().equals(GOOD)) {
                            goodEntries.add(new BarEntry(index, stats.getQuantity() > 0 ? stats.getQuantity() : 10));
                        } else if (stats.getQuality().equals(BAD)) {
                            badEntries.add(new BarEntry(index, stats.getQuantity() > 0 ? stats.getQuantity() : 10));
                        } else {
                            mastitisEntries.add(new BarEntry(index, stats.getQuantity() > 0 ? stats.getQuantity() : 10));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    index++;
                }
            }
            XAxis xAxis = viewBarChartBinding.barChart.getXAxis();
            xAxis.setLabelCount(endIndex <= 5 ? endIndex : 5, false);
            xAxis.setAxisMinimum(startIndex - chartPadding);
            xAxis.setAxisMaximum(endIndex + chartPadding);
            xAxis.setGranularityEnabled(true );
            viewBarChartBinding.barChart.setVisibleXRange(5, 5);
            viewBarChartBinding.barChart.setDragEnabled(true);

            YAxis leftAxis = viewBarChartBinding.barChart.getAxisLeft();
            if (max > IntUtil.get90Per(chartAxisLimit)) {
                leftAxis.setAxisMaximum(max + chartAxisCorrection);
            } else {
                leftAxis.setAxisMaximum(chartAxisLimit);
            }

            BarDataSet goodSet = new BarDataSet(goodEntries, "Good");
            goodSet.setHighlightEnabled(true);
            goodSet.setDrawValues(false);
            goodSet.setHighLightColor(ContextCompat.getColor(getContext(), R.color.colorLogoGreenSelected));
            goodSet.setColor(ContextCompat.getColor(getContext(), R.color.colorLogoGreen));
            goodSet.setAxisDependency(YAxis.AxisDependency.LEFT);

            BarDataSet badSet = new BarDataSet(badEntries, "Bad");
            badSet.setHighlightEnabled(true);
            badSet.setDrawValues(false);
            badSet.setHighLightColor(ContextCompat.getColor(getContext(), R.color.colorLogoRedSelected));
            badSet.setColor(ContextCompat.getColor(getContext(), R.color.colorLogoRed));
            badSet.setAxisDependency(YAxis.AxisDependency.LEFT);

            BarDataSet mastitisSet = new BarDataSet(mastitisEntries, "Mastitis");
            mastitisSet.setHighlightEnabled(true);
            mastitisSet.setDrawValues(false);
            mastitisSet.setHighLightColor(ContextCompat.getColor(getContext(), R.color.colorLogoYellowSelected));
            mastitisSet.setColor(ContextCompat.getColor(getContext(), R.color.colorLogoYellow));
            mastitisSet.setAxisDependency(YAxis.AxisDependency.LEFT);

            d.addDataSet(goodSet);
            d.addDataSet(badSet);
            d.addDataSet(mastitisSet);
            d.setBarWidth(0.5f);
            viewBarChartBinding.barChart.invalidate();
            viewBarChartBinding.barChart.setData(d);
            viewBarChartBinding.barChart.getLegend().setEnabled(true);
            viewBarChartBinding.barChart.getLegend().setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryText));
            viewBarChartBinding.barChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            viewBarChartBinding.barChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        } else {
            viewBarChartBinding.barChart.invalidate();
            viewBarChartBinding.barChart.setData(null);
            viewBarChartBinding.barChart.getLegend().setEnabled(false);
        }
    }
}
