package com.hug.mma.adapters;


import com.hug.mma.widget.OtpPin4View;
import com.hug.mma.widget.Pinview;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;

/**
 * Created by Raviteja on 30-08-2017 for HugFit
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

@InverseBindingMethods({
        @InverseBindingMethod(type = OtpPin4View.class, attribute = "opt_input", event = "otp_inputAttrChanged"),
})
public class OtpPin4BindingAdapter {

    @InverseBindingAdapter(attribute = "otp_input", event = "otp_inputAttrChanged")
    public static String getOtp(OtpPin4View otpPin4View) {
        return otpPin4View.getOTP();
    }

    @BindingAdapter("otp_input")
    public static void setOtp(OtpPin4View otpPin4View, String otp) {
        otpPin4View.setOTP(otp);
    }

    @BindingAdapter({"otp_error", "otp_error_enable"})
    public static void setOtpError(OtpPin4View otpPin4View, String error, boolean enable) {
        otpPin4View.getInputLayoutPassword().setError(error);
        otpPin4View.getInputLayoutPassword().setErrorEnabled(enable);
    }

    @BindingAdapter(value = {"otp_inputAttrChanged"}, requireAll = false)
    public static void setOtpListener(final OtpPin4View otpPin4View, final InverseBindingListener otp_inputAttrChanged) {
        otpPin4View.getPinView().setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                otp_inputAttrChanged.onChange();
            }

            @Override
            public void onDataEntering(boolean entered) {
                otp_inputAttrChanged.onChange();
            }
        });
    }
}
