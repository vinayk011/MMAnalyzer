package com.hug.mma.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Raviteja on 26-07-2017 for HugFit.
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public class GsonUtil {
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .create();

    public static Gson getGson() {
        return gson;
    }
}
