package com.hug.mma.model;

import java.util.Locale;

/**
 * Created by Raviteja on 22-08-2017 for HugFit.
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public class Summary {
    private String title;
    private String data;
    private String text;
    private int color;
    private long start;
    private long end;

    public Summary(String title, String text, int color) {
        this.title = title;
        this.text = text;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(int data) {
        this.data = null;
        if(data >= 0) {
            this.data = String.valueOf(data);
        }
    }

    public void setData(double data) {
        this.data = null;
        if(data > 0) {
            this.data = String.format(Locale.getDefault(), "%.2f", data);
        }else if(data == 0){
            this.data = String.valueOf(0);
        }
    }

    public void setData(long data) {
        this.data = null;
        if(data >= 0) {
            this.data = String.valueOf(data);
        }
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
