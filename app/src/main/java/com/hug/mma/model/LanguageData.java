package com.hug.mma.model;

public class LanguageData {
    private String locale;
    private String name;

    public LanguageData(String locale, String name) {
        this.locale = locale;
        this.name = name;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
