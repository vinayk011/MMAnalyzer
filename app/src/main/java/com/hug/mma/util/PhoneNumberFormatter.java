package com.hug.mma.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

/**
 * Created by Raviteja on 25-07-2017 for HugFit.
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public class PhoneNumberFormatter {

    private static PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    public static Phone getFormattedNumber(String number) {
        Phone phone = new Phone();
        try {
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(number, "");
            phone.setFormatted(phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
            phone.setNational(String.valueOf(numberProto.getNationalNumber()));
            phone.setCountryCode(String.valueOf(numberProto.getCountryCode()));
        } catch (NumberParseException e) {
            e.printStackTrace();
            phone.setFormatted(number);
            phone.setNational(number);
        }
        Trace.i(phone.toString());
        return phone;
    }

    public static class Phone {
        String formatted;
        String national;
        String countryCode;

        public String getFormatted() {
            return formatted;
        }

        public void setFormatted(String formatted) {
            this.formatted = formatted;
        }

        public String getNational() {
            return national;
        }

        public void setNational(String national) {
            this.national = national;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Phone{");
            sb.append("formatted='").append(formatted).append('\'');
            sb.append(", national='").append(national).append('\'');
            sb.append(", countryCode='").append(countryCode).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}
