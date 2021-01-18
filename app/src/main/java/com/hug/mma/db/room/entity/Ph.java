package com.hug.mma.db.room.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ph {

    @SerializedName("min")
    @Expose
    private Integer min;
    @SerializedName("max")
    @Expose
    private Integer max;
    @SerializedName("avg")
    @Expose
    private Integer avg;
    @SerializedName("beyond")
    @Expose
    private Integer beyond;
    @SerializedName("withIn")
    @Expose
    private Integer withIn;

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getAvg() {
        return avg;
    }

    public void setAvg(Integer avg) {
        this.avg = avg;
    }

    public Integer getBeyond() {
        return beyond;
    }

    public void setBeyond(Integer beyond) {
        this.beyond = beyond;
    }

    public Integer getWithIn() {
        return withIn;
    }

    public void setWithIn(Integer withIn) {
        this.withIn = withIn;
    }

    @Override
    public String toString() {
        return "Ph{" +
                "min=" + min +
                ", max=" + max +
                ", avg=" + avg +
                ", beyond=" + beyond +
                ", withIn=" + withIn +
                '}';
    }
}