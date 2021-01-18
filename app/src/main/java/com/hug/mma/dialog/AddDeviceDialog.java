package com.hug.mma.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;

import com.hug.mma.R;
import com.hug.mma.bindingmodel.DeviceDataHandler;
import com.hug.mma.databinding.DialogAddDeviceBinding;
import com.hug.mma.db.room.entity.Device;
import com.hug.mma.listener.AddDeviceListener;
import com.hug.mma.listener.DialogListener;

import androidx.databinding.DataBindingUtil;

public class AddDeviceDialog extends BaseDialog<DialogAddDeviceBinding> {
    private Context context;
    private boolean newDevice;
    private AddDeviceListener deviceListener;
    private Device device;
    private DeviceDataHandler deviceDataHandler = new DeviceDataHandler();

    public AddDeviceDialog(Context context, boolean newDevice, Device device, AddDeviceListener listener) {
        super(context);
        this.context = context;
        this.newDevice = newDevice;
        this.deviceListener = listener;
        this.device = device;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_add_device, null, false);
        setContentView(binding.getRoot());
        binding.setTitle(newDevice ? context.getString(R.string.add_device) : context.getString(R.string.edit_device));
        binding.setCallback(dialogListener);
        deviceDataHandler.setDevice(device);
        binding.setDevice(deviceDataHandler);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dialogListener = null;
        binding = null;
    }

    private DialogListener dialogListener = new DialogListener() {
        @Override
        public void ok() {
            if (deviceDataHandler.isValid()) {
                deviceListener.addDevice(deviceDataHandler.getDevice());
                dismiss();
            }
        }

        @Override
        public void cancel() {
            dismiss();
        }
    };
}
