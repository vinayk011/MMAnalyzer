package com.hug.mma.bindingmodel;


import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hug.mma.util.EncryptionUtil;
import com.hug.mma.util.StringUtil;
import com.hug.mma.viewmodel.request.SignInRequest;

import androidx.databinding.ObservableField;

/**
 * Created by Raviteja on 24-07-2017 for HugFit
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public class UserDataErrorHolder {

    public final ObservableField<String> mobile =
            new ObservableField<>();
    public final ObservableField<String> pin =
            new ObservableField<>();
    public final ObservableField<String> mobileError =
            new ObservableField<>();
    public final ObservableField<String> pinError =
            new ObservableField<>();

    public ObservableField<String> getMobile() {
        mobileError.set(null);
        return mobile;
    }

    public ObservableField<String> getPin() {
        pinError.set(null);
        return pin;
    }

    public boolean isValid() {
        pinError.set(null);
        mobileError.set(null);
        if (StringUtil.checkLengthGreater(mobile.get(), 10) && StringUtil.checkLength(pin.get(), 4) && isPhoneValid()) {
            return true;
        } else {
            if (StringUtil.isEmpty(mobile.get())) {
                mobileError.set("Enter mobile number.");
            } else if (!isPhoneValid()) {
                mobileError.set("Enter valid mobile number.");
            }
            if (StringUtil.isEmpty(pin.get())) {
                pinError.set("Enter PIN.");
            } else if (!StringUtil.checkLength(pin.get(), 4)) {
                pinError.set("Enter valid PIN.");
            }
        }
        return false;
    }

    private boolean isPhoneValid() {
        try {
            PhoneNumberUtil mPhoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber mNumber = mPhoneUtil.parse(mobile.get(), "");
            return mPhoneUtil.isValidNumber(mNumber);
        } catch (Exception ignored) {
        }
        return false;
    }

    public String getEncryptedPin() {
        return EncryptionUtil.encrypt(pin.get());
    }

    public SignInRequest getData() {
        if (isValid()) {
            return new SignInRequest(mobile.get(), pin.get()/*EncryptionUtil.encrypt(pin.get())*/);
        } else {
            return new SignInRequest();
        }
    }
}
