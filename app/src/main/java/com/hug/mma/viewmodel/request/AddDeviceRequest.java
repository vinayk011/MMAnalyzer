package com.hug.mma.viewmodel.request;

import com.hug.mma.db.room.entity.Device;

import java.util.List;

public class AddDeviceRequest {
    private List<Device> devices;

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }
}
