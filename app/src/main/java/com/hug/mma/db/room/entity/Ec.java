package com.hug.mma.db.room.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ec {

    @SerializedName("minEc")
    @Expose
    private Double minEc;
    @SerializedName("maxEc")
    @Expose
    private Double maxEc;
    @SerializedName("avgEc")
    @Expose
    private Double avgEc;
    @SerializedName("minDiff")
    @Expose
    private Double minDiff;
    @SerializedName("maxDiff")
    @Expose
    private Double maxDiff;
    @SerializedName("avgDiff")
    @Expose
    private Integer avgDiff;
    @SerializedName("normal")
    @Expose
    private Integer normal;
    @SerializedName("abnormal")
    @Expose
    private Integer abnormal;
    @SerializedName("mastitis")
    @Expose
    private Integer mastitis;

    public Double getMinEc() {
        return minEc;
    }

    public void setMinEc(Double minEc) {
        this.minEc = minEc;
    }

    public Double getMaxEc() {
        return maxEc;
    }

    public void setMaxEc(Double maxEc) {
        this.maxEc = maxEc;
    }

    public Double getAvgEc() {
        return avgEc;
    }

    public void setAvgEc(Double avgEc) {
        this.avgEc = avgEc;
    }

    public Double getMinDiff() {
        return minDiff;
    }

    public void setMinDiff(Double minDiff) {
        this.minDiff = minDiff;
    }

    public Double getMaxDiff() {
        return maxDiff;
    }

    public void setMaxDiff(Double maxDiff) {
        this.maxDiff = maxDiff;
    }

    public Integer getAvgDiff() {
        return avgDiff;
    }

    public void setAvgDiff(Integer avgDiff) {
        this.avgDiff = avgDiff;
    }

    public Integer getNormal() {
        return normal;
    }

    public void setNormal(Integer normal) {
        this.normal = normal;
    }

    public Integer getAbnormal() {
        return abnormal;
    }

    public void setAbnormal(Integer abnormal) {
        this.abnormal = abnormal;
    }

    public Integer getMastitis() {
        return mastitis;
    }

    public void setMastitis(Integer mastitis) {
        this.mastitis = mastitis;
    }

    @Override
    public String toString() {
        return "Ec{" +
                "minEc=" + minEc +
                ", maxEc=" + maxEc +
                ", avgEc=" + avgEc +
                ", minDiff=" + minDiff +
                ", maxDiff=" + maxDiff +
                ", avgDiff=" + avgDiff +
                ", normal=" + normal +
                ", abnormal=" + abnormal +
                ", mastitis=" + mastitis +
                '}';
    }
}