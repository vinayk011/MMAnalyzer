package com.hug.mma.adapters;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hug.mma.R;
import com.hug.mma.constants.EnumConstants;
import com.hug.mma.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.databinding.BindingAdapter;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;
import static com.hug.mma.constants.AppConstants.BAD;
import static com.hug.mma.constants.AppConstants.GOOD;

public class BindingAdapters {

    @BindingAdapter("isGone")
    public static void bindIsGone(View view, boolean isGone) {
        view.setVisibility(isGone ? View.GONE : View.VISIBLE);
    }

    @BindingAdapter("img_src")
    public static void setSrc(ImageView imageView, String quality){
        if (GOOD.equals(quality)) {
            imageView.setImageResource(R.drawable.ic_cow_green);
        } else if (BAD.equals(quality)) {
            imageView.setImageResource(R.drawable.ic_cow_red);
        } else {
            imageView.setImageResource(R.drawable.ic_cow_yellow);
        }
    }

    @BindingAdapter("type")
    public static void setType(TextView textView, String type) {
        String text = textView.getResources().getString(R.string.type) + ": ";
        SpannableString span1 = new SpannableString(text);
        span1.setSpan(new AbsoluteSizeSpan((int) textView.getContext().getResources().getDimension(R.dimen._10sdp))
                , 0, text.length(), SPAN_INCLUSIVE_INCLUSIVE);
        SpannableString span2 = new SpannableString(type);
        span2.setSpan(new AbsoluteSizeSpan((int) textView.getContext().getResources().getDimension(R.dimen._13sdp))
                , 0, type.length(), SPAN_INCLUSIVE_INCLUSIVE);
        CharSequence finalText = TextUtils.concat(span2);
        textView.setText(finalText);
    }

    @BindingAdapter({"start_time", "end_time"})
    public static void setTime(TextView textView, long start, long end) {
        String text = textView.getResources().getString(R.string.time) + ": ";
        SpannableString span1 = new SpannableString(text);
        span1.setSpan(new AbsoluteSizeSpan((int) textView.getContext().getResources().getDimension(R.dimen._10sdp))
                , 0, text.length(), SPAN_INCLUSIVE_INCLUSIVE);
        String startTime = DateUtil.convertToHHMMAFormat(start);
        SpannableString span2 = new SpannableString(startTime);
        span2.setSpan(new AbsoluteSizeSpan((int) textView.getContext().getResources().getDimension(R.dimen._13sdp))
                , 0, startTime.length(), SPAN_INCLUSIVE_INCLUSIVE);
        String endTime = DateUtil.convertToHHMMAFormat(end);
        SpannableString span3 = new SpannableString(endTime);
        span3.setSpan(new AbsoluteSizeSpan((int) textView.getContext().getResources().getDimension(R.dimen._13sdp))
                , 0, startTime.length(), SPAN_INCLUSIVE_INCLUSIVE);

        CharSequence finalText = TextUtils.concat( span2, " - ", span3);
        textView.setText(finalText);

    }

    @BindingAdapter("seq")
    public static void setSeq(TextView textView, int seq) {
        String text = textView.getResources().getString(R.string.sequence) + ": ";
        SpannableString span1 = new SpannableString(text);
        span1.setSpan(new AbsoluteSizeSpan((int) textView.getContext().getResources().getDimension(R.dimen._10sdp))
                , 0, text.length(), SPAN_INCLUSIVE_INCLUSIVE);
        SpannableString span2 = new SpannableString(String.valueOf(seq));
        span2.setSpan(new AbsoluteSizeSpan((int) textView.getContext().getResources().getDimension(R.dimen._13sdp))
                , 0, String.valueOf(seq).length(), SPAN_INCLUSIVE_INCLUSIVE);
        CharSequence finalText = TextUtils.concat(span2);
        textView.setText(finalText);
    }

    @BindingAdapter({"summary_date", "summary_scope"})
    public static void setSummaryDate(TextView textView, Date date, EnumConstants.Scope scope) {
        Context context = textView.getContext();
        if (date == null) {
            textView.setText(R.string.no_records_found);
            return;
        }
        if (scope == EnumConstants.Scope.DAY) {
            Calendar current = Calendar.getInstance();
            current.setTime(date);
            if (DateUtil.isToday(current)) {
                textView.setText(context.getString(R.string.today));
                return;
            }
            Calendar yesterday = Calendar.getInstance();
            yesterday.add(Calendar.DAY_OF_YEAR, -1);
            if (DateUtil.isSameDay(current, yesterday)) {
                textView.setText(context.getString(R.string.yeserday));
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
            textView.setText(sdf.format(date));
        } else if (scope == EnumConstants.Scope.WEEK) {
            if (DateUtil.isSameDay(date, DateUtil.weekStartDate(new Date())) ||
                    DateUtil.isSameDay(date, DateUtil.weekEndDate(new Date())) ||
                    (DateUtil.isAfterDay(date, DateUtil.weekStartDate(new Date())) &&
                            DateUtil.isBeforeDay(date, DateUtil.weekEndDate(new Date())))) {
                textView.setText(context.getString(R.string.this_week));
                return;
            }
            Calendar lastWeek = Calendar.getInstance();
            lastWeek.add(Calendar.DAY_OF_YEAR, -7);
            if (DateUtil.isSameDay(date, DateUtil.weekStartDate(lastWeek.getTime())) ||
                    DateUtil.isSameDay(date, DateUtil.weekEndDate(lastWeek.getTime())) ||
                    (DateUtil.isAfterDay(date, DateUtil.weekStartDate(lastWeek.getTime())) &&
                            DateUtil.isBeforeDay(date, DateUtil.weekEndDate(lastWeek.getTime())))) {
                textView.setText(context.getString(R.string.last_week));
                return;
            }
            SimpleDateFormat sdfStart = new SimpleDateFormat("d MMM", Locale.getDefault());
            SimpleDateFormat sdfEnd = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
            textView.setText(sdfStart.format(DateUtil.weekStartDate(date)) + " - " + sdfEnd.format(DateUtil.weekEndDate(date)));
        } else if (scope == EnumConstants.Scope.MONTH) {
            Calendar current = Calendar.getInstance();
            current.setTime(date);
            if (DateUtil.isCurrentMonth(current)) {
                textView.setText(context.getString(R.string.this_month));
                return;
            }
            Calendar yesterday = Calendar.getInstance();
            yesterday.add(Calendar.MONTH, -1);
            if (DateUtil.isSameMonth(current, yesterday)) {
                textView.setText(context.getString(R.string.last_month));
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
            textView.setText(sdf.format(date));
        }
    }
}
