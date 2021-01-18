package com.hug.mma.widget;

/**
 * Created by Raviteja on 19-07-2017 for HugFit
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hug.mma.R;


public class OtpPin4View extends LinearLayout {

    private Pinview pinView;
    private TextInputEditText inputPassword;
    private TextInputLayout inputLayoutPassword;
    private MaterialButton showHideButton;

    public Pinview getPinView() {
        return pinView;
    }

    public TextInputEditText getInputPassword() {
        return inputPassword;
    }

    public TextInputLayout getInputLayoutPassword() {
        return inputLayoutPassword;
    }

    public OtpPin4View(Context context) {
        super(context);
        init(null);
    }

    public OtpPin4View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public OtpPin4View(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray styles = getContext().obtainStyledAttributes(attrs, R.styleable.OtpView);
        LayoutInflater mInflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.view_otppin4, this);
        pinView = findViewById(R.id.pin_view);
        inputPassword = findViewById(R.id.input_password);
        inputLayoutPassword = findViewById(R.id.input_layout_password);
        showHideButton = findViewById(R.id.show_hide);
        inputPassword.setKeyListener(null);
        pinView.setLocalPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {

            }

            @Override
            public void onDataEntering(boolean entered) {
                if (entered) {
                    if (pinView.getValue().length() > 0) {
                        inputPassword.setText(" ");
                    } else {
                        inputPassword.getText().clear();
                    }
                } else {
                    inputPassword.getText().clear();
                }
            }
        });
        String hint = styles.getString(R.styleable.OtpView_otp_hint);
        inputLayoutPassword.setHint(hint);
        showHide();
        styles.recycle();
    }

    private void showHide() {
        showHideButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MaterialButton) v).getText().equals(getContext().getString(R.string.show))) {
                    showHideButton.setText(getContext().getString(R.string.hide));
                    pinView.setPassword(false);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showHideButton.setText(getContext().getString(R.string.show));
                            pinView.setPassword(true);
                        }
                    }, 1000);
                } else if (((MaterialButton) v).getText().equals(getContext().getString(R.string.hide))) {
                    showHideButton.setText(getContext().getString(R.string.show));
                    pinView.setPassword(true);
                }
            }
        });
    }

    /**
     * Get an instance of the present otp
     */
    private String makeOTP() {
        return pinView.getValue();
    }

    /**
     * Checks if all four fields have been filled
     *
     * @return length of OTP
     */
    public boolean hasValidOTP() {
        return makeOTP().length() == 4;
    }

    /**
     * Returns the present otp entered by the user
     *
     * @return OTP
     */
    public String getOTP() {
        return makeOTP();
    }

    /**
     * Used to set the OTP. More of cosmetic value than functional value
     *
     * @param otp Send the four digit otp
     */
    public void setOTP(String otp) {
        if (otp == null || pinView.getValue().equals(otp)) {
            return;
        }
        pinView.setValue(otp);
    }
}