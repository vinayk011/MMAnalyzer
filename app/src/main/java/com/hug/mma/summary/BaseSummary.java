package com.hug.mma.summary;

import android.widget.FrameLayout;
import android.widget.TextView;

import com.hug.mma.constants.EnumConstants;

import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Raviteja on 23-08-2017 for HugFit.
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public interface BaseSummary<T> {
    int code = 2;

    String getTitle();

    int getTitleLogo();

    int getColor();

    void defaultSummary();

//    void callServer(EnumConstants.Scope scope, Date date);

    void callServer(String device, Date date);

    LiveData<List<T>> callDB(EnumConstants.Scope scope, String device, Date date);

    void setChart(FrameLayout frameLayout);

    void setRecyclerView(RecyclerView recyclerView, TextView textView);

    void setCardsRecyclerView(RecyclerView recyclerView);

    void updateSummary(List<T> list, EnumConstants.Scope scope, Date date);

    void updateSummary();

    void setBottomText(String text);

    void update(EnumConstants.Scope scope, String device, Date date);

    void updateViews();

}