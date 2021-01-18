package com.hug.mma.db.room.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DeviceStats {

    @NonNull
    @PrimaryKey
    @SerializedName("batch")
    @Expose
    private String batch;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("totalSamples")
    @Expose
    private Integer totalSamples;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("quality")
    @Expose
    private String quality;
    @SerializedName("start")
    @Expose
    private long startTime;
    @SerializedName("end")
    @Expose
    private long endTime;
    @SerializedName("ec")
    @Expose
    private Ec ec;
    @SerializedName("ph")
    @Expose
    private Ph ph;
    private Date recorded;
    private String device;
    private int seq;
    private boolean changed;

    public DeviceStats(String batch,String type, String quality,
                       long startTime, long endTime) {
        super();
        this.batch = batch;
        this.type = type;
        this.quality = quality;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getTotalSamples() {
        return totalSamples;
    }

    public void setTotalSamples(Integer totalSamples) {
        this.totalSamples = totalSamples;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuality() {
        return quality;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public Ec getEc() {
        return ec;
    }

    public void setEc(Ec ec) {
        this.ec = ec;
    }

    public Ph getPh() {
        return ph;
    }

    public void setPh(Ph ph) {
        this.ph = ph;
    }

    public Date getRecorded() {
        return recorded;
    }

    public void setRecorded(Date recorded) {
        this.recorded = recorded;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Override
    public String toString() {
        return "DeviceStats{" +
                "batch='" + batch + '\'' +
                ", quantity=" + quantity +
                ", totalSamples=" + totalSamples +
                ", type='" + type + '\'' +
                ", quality='" + quality + '\'' +
                ", ec=" + ec +
                ", ph=" + ph +
                ", recorded=" + recorded +
                ", device='" + device + '\'' +
                ", changed=" + changed +
                '}';
    }
}
