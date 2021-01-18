package com.hug.mma.adapters;


import com.hug.mma.widget.MobileNumberView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;

/**
 * Created by Raviteja on 25-07-2017 for HugFit.
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

@InverseBindingMethods({
        @InverseBindingMethod(type = MobileNumberView.class, attribute = "number_input", event = "number_inputAttrChanged"),
})
public class MobileNumberBinding {
    @InverseBindingAdapter(attribute = "number_input", event = "number_inputAttrChanged")
    public static String getMobileNumber(MobileNumberView mobileNumberView) {
        return mobileNumberView.getNumber();
    }

    @BindingAdapter("number_input")
    public static void setMobileNumber(MobileNumberView mobileNumberView, String number) {
        mobileNumberView.setNumber(number);
    }

    @BindingAdapter("number_error")
    public static void setMobileNumberError(MobileNumberView mobileNumberView, String error) {
        mobileNumberView.setError(error);
    }

    @BindingAdapter(value = {"number_inputAttrChanged"}, requireAll = false)
    public static void setMobileNumberListener(final MobileNumberView mobileNumberView, final InverseBindingListener number_inputAttrChanged) {
        mobileNumberView.setNumberEventListener(new MobileNumberView.NumberEventListener() {
            @Override
            public void onDone() {
                number_inputAttrChanged.onChange();
            }
        });
    }
}
