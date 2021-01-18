package com.hug.mma.binding;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.hug.mma.R;
import com.hug.mma.util.FontManager;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

/**
 * Created by Raviteja on 30-08-2017 for HugFit
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public class FontAwesomeBinding {
    @BindingAdapter("font_text")
    public static void setText(Button button, String text) {
        button.setText(text);
        button.setTypeface(FontManager.getTypeface(button.getContext()));
    }

    @BindingAdapter("font_text")
    public static void setText(TextView textView, String text) {
        textView.setText(text);
        textView.setTypeface(FontManager.getTypeface(textView.getContext()));
    }

    @BindingAdapter({"font_icon", "font_color"})
    public static void setIcon(Button button, String text, int color) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(button.getContext().getResources().getDimension(R.dimen._12ssp));
        paint.setColor(color);
        paint.setTypeface(FontManager.getTypeface(button.getContext()));
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        Drawable drawable = new BitmapDrawable(button.getContext().getResources(), image);
        button.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
    }

    @BindingAdapter({"font_icon", "font_size"})
    public static void setIcon(ImageView imageView, String text, float size) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(size);
        paint.setColor(ContextCompat.getColor(imageView.getContext(), R.color.colorPrimaryText));
        paint.setTypeface(FontManager.getTypeface(imageView.getContext()));
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        Drawable drawable = new BitmapDrawable(imageView.getContext().getResources(), image);
        imageView.setImageDrawable(drawable);
    }

//    @BindingAdapter("font_icon")
//    public static void setIcon(FloatingActionButton button, String text) {
//        Paint paint = new Paint(ANTI_ALIAS_FLAG);
//        paint.setTextSize(button.getContext().getResources().getDimension(R.dimen._15ssp));
//        paint.setColor(ContextCompat.getColor(button.getContext(), R.color.white));
//        paint.setTypeface(FontManager.getTypeface(button.getContext()));
//        paint.setTextAlign(Paint.Align.LEFT);
//        float baseline = -paint.ascent(); // ascent() is negative
//        int width = (int) (paint.measureText(text) + 0.5f); // round
//        int height = (int) (baseline + paint.descent() + 0.5f);
//        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(image);
//        canvas.drawText(text, 0, baseline, paint);
//        Drawable drawable = new BitmapDrawable(button.getContext().getResources(), image);
//        button.setImageDrawable(drawable);
//    }
}
