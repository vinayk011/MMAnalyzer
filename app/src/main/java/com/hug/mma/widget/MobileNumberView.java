package com.hug.mma.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hug.mma.R;
import com.hug.mma.databinding.ViewCountryCodePickerBinding;
import com.hug.mma.model.CountryData;
import com.hug.mma.util.CountryCodeUtil;
import com.hug.mma.util.PhoneNumberFormatter;
import com.hug.mma.util.StringUtil;
import com.hug.mma.util.Trace;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

/**
 * Created by Raviteja on 25-07-2017 for HugFit.
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public class MobileNumberView extends LinearLayout {
    private AlertDialog countryDialog;
    private List<CountryData> displayList = new ArrayList<>();
    private List<CountryData> originalList = new ArrayList<>();
    private TextInputLayout hintLayout;
    private TextInputEditText countryCode;
    private TextInputEditText mobileNumber;
    private String number, iso;
    private NumberEventListener listener;

    public interface NumberEventListener {
        void onDone();
    }

    public MobileNumberView(Context context) {
        super(context);
        init(null);
    }

    public MobileNumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MobileNumberView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public TextInputEditText getMobileNumber() {
        return mobileNumber;
    }

    private void init(AttributeSet attrs) {
        TypedArray styles = getContext().obtainStyledAttributes(attrs, R.styleable.MobileNumberView);
        LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.view_mobile_number, this);
        displayList.addAll(CountryCodeUtil.getSupportedCountries());
        originalList.addAll(displayList);
        hintLayout = findViewById(R.id.hintLayout);
        countryCode = findViewById(R.id.countryCode);
        mobileNumber = findViewById(R.id.mobileNumber);
        ((TextInputEditText) findViewById(R.id.hint)).setKeyListener(null);
        countryCode.setKeyListener(null);
        countryCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countryDialog == null || !countryDialog.isShowing()) {
                    setError(null);
                    countryDialog = initDialog();
                    countryDialog.show();
                }
            }
        });
        setDefault();
        String hint = styles.getString(R.styleable.MobileNumberView_mn_hint);
        hintLayout.setHint(hint);
        styles.recycle();
    }

    public void setError(String error) {
        hintLayout.setError(error);
        hintLayout.setErrorEnabled(error != null);
    }

    private void setCountryCode(String code) {
        iso = code;
        countryCode.setText(code);
        setFormatter();
    }

    public String getNumber() {
        Trace.a();
        return iso + number;
    }

    public boolean isValid() {
        setError(null);
        if (StringUtil.isEmpty(iso)) {
            setError("Select country code");
            return false;
        } else if (CountryCodeUtil.getCountryIsoCode(iso) == null) {
            setError("Select valid country code");
            return false;
        } else if (StringUtil.isEmpty(mobileNumber.getText().toString())) {
            setError("Enter mobile number");
            return false;
        } else if (StringUtil.isEmpty(number) || !isNumberValid()) {
            setError("Enter valid mobile number");
            return false;
        }
        return true;
    }

    public boolean isNumberValid() {
        try {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(iso + number, "");
            return phoneUtil.isValidNumber(phoneNumber);
        } catch (NumberParseException ignored) {
        }
        return false;
    }

    public void setFormatter() {
        setWatcher();
        if (!TextUtils.isEmpty(number)) {
            mobileNumber.setText(number);
        } else {
            String mNumber = mobileNumber.getText().toString();
            mobileNumber.setText(mNumber);
        }
    }

    private TextWatcher textWatcher = null;

    private void setWatcher() {
        if (textWatcher != null) {
            mobileNumber.removeTextChangedListener(textWatcher);
        }
        textWatcher = null;
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                handler.removeMessages(MESSAGE_ID);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Message message = new Message();
                message.what = MESSAGE_ID;
                message.obj = s.toString();
                handler.sendMessageDelayed(message, 400);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        mobileNumber.addTextChangedListener(textWatcher);
    }

    private final int MESSAGE_ID = 18;
    private Handler handler = new Handler(Looper.myLooper()) {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            if (msg != null && msg.what == MESSAGE_ID) {
                String s = (String) msg.obj;
                if (StringUtil.checkLengthGreater(iso, 1) && StringUtil.checkLengthGreater(s, iso.length())) {
                    s = s.replaceAll("[\\D]", "");
                    PhoneNumberFormatter.Phone phone = PhoneNumberFormatter.getFormattedNumber(iso + String.valueOf(s));
                    mobileNumber.removeTextChangedListener(textWatcher);
                    String formattedNumber = StringUtil.fixString(phone.getFormatted());
                    if (formattedNumber.contains(iso) && formattedNumber.length() > (iso.length())) {
                        formattedNumber = formattedNumber.replaceAll(Pattern.quote(iso), "");
                    }
                    mobileNumber.setText(formattedNumber);
                    mobileNumber.setSelection(formattedNumber.length());
                    mobileNumber.addTextChangedListener(textWatcher);
                    number = phone.getNational();
                    if (listener != null) {
                        listener.onDone();
                    }
                } else {
                    number = "";
                    if (listener != null) {
                        listener.onDone();
                    }
                }
            }
        }
    };

    private void setDefault() {
        TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String countryTelephonyCode = CountryCodeUtil.getCountryTelephonyCode(telephonyManager.getSimCountryIso().toLowerCase());
        if (TextUtils.isEmpty(countryTelephonyCode)) {
            countryTelephonyCode = getContext().getString(R.string.plus);
        } else {
            countryTelephonyCode = getContext().getString(R.string.plus) + countryTelephonyCode;
        }
        setCountryCode(countryTelephonyCode);
    }

    private AlertDialog initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        final CountryListAdapter countryListAdapter = new CountryListAdapter(this.getContext(), displayList);
        ViewCountryCodePickerBinding viewCountryCodePickerBinding = DataBindingUtil.inflate(LayoutInflater.from(this.getContext()),
                R.layout.view_country_code_picker, this, false);
        viewCountryCodePickerBinding.searchCountryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null) {
                    String searchText = editable.toString();
                    displayList.clear();
                    if (!searchText.isEmpty()) {
                        CharSequence toLowerCase = searchText.toLowerCase();
                        for (CountryData mobileDataCountry : originalList) {
                            if (mobileDataCountry.getCountryFullName().toLowerCase().contains(toLowerCase)
                                    || mobileDataCountry.getCountryShortName().toLowerCase().contains(toLowerCase)) {
                                displayList.add(mobileDataCountry);
                            }
                        }
                    } else {
                        displayList.addAll(originalList);
                    }
                    countryListAdapter.notifyDataSetChanged();
                }
            }
        });
        viewCountryCodePickerBinding.countyList.setAdapter(countryListAdapter);
        viewCountryCodePickerBinding.cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countryDialog != null && countryDialog.isShowing()) {
                    countryDialog.dismiss();
                }
            }
        });
        builder.setView(viewCountryCodePickerBinding.getRoot());
        return builder.create();
    }

    public void setNumber(String mobileNumber) {
        try {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(mobileNumber, "");
            if (phoneNumber == null || String.valueOf(phoneNumber.getNationalNumber()).equals(number)) {
                return;
            }
            this.mobileNumber.setText(String.valueOf(phoneNumber.getNationalNumber()));
            this.mobileNumber.setSelection(this.mobileNumber.getText().length());
            setCountryCode(String.format(getContext().getString(R.string.country_code_format), String.valueOf(phoneNumber.getCountryCode())));
        } catch (NumberParseException ignored) {
        }
    }

    public void setNumberEventListener(NumberEventListener listener) {
        this.listener = listener;
    }

    private class CountryListAdapter extends BaseAdapter {
        private final List<CountryData> displayList;
        private Context context;

        CountryListAdapter(Context context, List<CountryData> countries) {
            this.context = context;
            this.displayList = countries;
        }

        public int getCount() {
            return this.displayList.size();
        }

        public CountryData getItem(int i) {
            return this.displayList.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.view_country_layout, null);
            }
            final CountryData item = getItem(i);
            final String code = String.format(getContext().getString(R.string.country_code_format), item.getCountryTelephonyCode());
            ((TextView) view.findViewById(R.id.country_row_item_full_name)).setText(item.getCountryFullName() + " (" + item.getCountryShortName() + ")");
            TextView textView = view.findViewById(R.id.country_row_item_telephony_code);
            textView.setVisibility(View.VISIBLE);
            textView.setText(code);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    setCountryCode(code);
                    if (countryDialog != null && countryDialog.isShowing()) {
                        countryDialog.dismiss();
                    }
                }
            });
            return view;
        }
    }
}