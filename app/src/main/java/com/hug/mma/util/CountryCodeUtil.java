package com.hug.mma.util;

import android.text.TextUtils;

import com.hug.mma.M2AApplication;
import com.hug.mma.model.CountryData;
import com.hug.mma.model.LanguageData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Raviteja on 25-07-2017 for HugFit.
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public class CountryCodeUtil {

    private static final String KEY_COUNTRIES = "countries";
    private static final String KEY_LANGUAGES = "languages";
    private static final String KEY_NAME = "fullCountryName";
    private static final String KEY_SHORTNAME = "shortCountryName";
    private static final String KEY_LOCALE = "locale";
    private static final String KEY_TELEPHONY_CODE = "countryTelephonyCode";
    private static final String KEY_LANGUAGE = "language";
    private static final String COUNTRIES_JSON_FILE = "Countries.json";
    private static final String CHARSET_NAME = "UTF-8";

    private static List<CountryData> supportedCountries;
    private static List<LanguageData> supportedLanguages;

    static {
        supportedCountries = readMobileDataCountriesJSONFile();
        supportedLanguages = readLanguagesJSONFile();
    }


    private static List<CountryData> readMobileDataCountriesJSONFile() {
        List<CountryData> countryList = new ArrayList<>();
        try {
            JSONArray jSONArray = new JSONObject(loadJSONFromAsset("Countries.json")).getJSONArray(KEY_COUNTRIES);
            int i = 0;
            while (jSONArray != null && i < jSONArray.length()) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                countryList.add(new CountryData(jSONObject.getString(KEY_NAME), jSONObject.getString(KEY_SHORTNAME), jSONObject.getString(KEY_LOCALE), jSONObject.getString(KEY_TELEPHONY_CODE)));
                i++;
            }
        } catch (Exception e) {
            Trace.e("Error parsing Countries.json");
        }
        Trace.i(" " + Arrays.toString(countryList.toArray()));
        return countryList;
    }

    private static List<LanguageData> readLanguagesJSONFile() {
        List<LanguageData> languageList = new ArrayList<>();
        try {
            JSONArray jSONArray = new JSONObject(loadJSONFromAsset("Languages.json")).getJSONArray(KEY_LANGUAGES);
            int i = 0;
            while (jSONArray != null && i < jSONArray.length()) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                languageList.add(new LanguageData(jSONObject.getString(KEY_LOCALE), jSONObject.getString(KEY_LANGUAGE)));
                i++;
            }
        } catch (Exception e) {
            Trace.e("Error parsing Countries.json");
        }
        Trace.i(" " + Arrays.toString(languageList.toArray()));
        return languageList;
    }


    public static List<CountryData> getSupportedCountries() {
        return supportedCountries;
    }

    public static List<LanguageData> getSupportedLanguages() {
        return supportedLanguages;
    }

    public static String getCountryTelephonyCode(String countryIso) {
        countryIso = countryIso.toLowerCase();
        String countryTelephonyCode = null;
        if (!TextUtils.isEmpty(countryIso)) {
            for (CountryData country : supportedCountries) {
                if (countryIso.equals(country.getLocale().toLowerCase())) {
                    countryTelephonyCode = country.getCountryTelephonyCode();
                    break;
                }
            }
        }
        return countryTelephonyCode;
    }

    public static String getCountryIsoCode(String countryTelephonyCode) {
        String countryIso = null;
        if (!TextUtils.isEmpty(countryTelephonyCode)) {
            for (CountryData country : supportedCountries) {
                if (countryTelephonyCode.equals("+" + country.getCountryTelephonyCode())) {
                    countryIso = country.getLocale();
                    break;
                }
            }
        }
        return countryIso;
    }

    public static String getLanguageByCode(String locale) {
        String language = null;
        if (!TextUtils.isEmpty(locale)) {
            for (LanguageData data : supportedLanguages) {
                if (locale.equals(data.getLocale())) {
                    language = data.getName();
                    break;
                }
            }
        }
        return language;
    }

    private static String loadJSONFromAsset(String fileName) {
        try {
            InputStream open = M2AApplication.getInstance().getAssets().open(fileName);
            byte[] bArr = new byte[open.available()];
            //noinspection ResultOfMethodCallIgnored
            open.read(bArr);
            open.close();
            return new String(bArr, "UTF-8");
        } catch (IOException e) {
            return null;
        }
    }
}
