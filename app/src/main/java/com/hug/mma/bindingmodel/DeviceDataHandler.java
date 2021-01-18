package com.hug.mma.bindingmodel;

import com.hug.mma.db.room.entity.Device;
import com.hug.mma.util.StringUtil;

import androidx.databinding.ObservableField;

public class DeviceDataHandler {
    public final ObservableField<String> deviceId = new ObservableField<>();
    public final ObservableField<String> deviceName = new ObservableField<>();

    public final ObservableField<String> deviceIdError = new ObservableField<>();
    public final ObservableField<String> deviceNameError = new ObservableField<>();


    public ObservableField<String> getDeviceId() {
        deviceIdError.set(null);
        return deviceId;
    }

    public ObservableField<String> getDeviceName() {
        deviceNameError.set(null);
        return deviceName;
    }

    public boolean isValid() {
        deviceIdError.set(null);
        deviceNameError.set(null);
        if (StringUtil.checkLengthGreater(deviceId.get(), 0) && StringUtil.checkLengthGreater(deviceName.get(), 0)) {
            return true;
        } else {
            if (StringUtil.isEmpty(deviceId.get())) {
                deviceIdError.set("Enter Device Id");
            }
            if (StringUtil.isEmpty(deviceName.get())) {
                deviceNameError.set("Enter Device name");
            }
        }
        return false;
    }

    public Device getDevice() {
        if (isValid()) {
            return new Device(deviceId.get(), deviceName.get());
        } else {
            return null;
        }
    }

    public void setDevice(Device device) {
        if (device != null) {
            deviceId.set(device.getDevice());
            deviceName.set(device.getName());
        }
    }
}
