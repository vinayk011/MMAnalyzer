package com.hug.mma.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Raviteja on 30-08-2017 for HugFit
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public class FontManager {

    private static final String ROOT = "fonts/",
            FONT_AWESOME = ROOT + "fontawesome.ttf";

    public static Typeface getTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), FONT_AWESOME);
    }
}
