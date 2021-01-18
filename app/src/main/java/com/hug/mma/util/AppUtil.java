package com.hug.mma.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.preference.PreferenceManager;

import com.hug.mma.activity.HomeActivity;
import com.hug.mma.activity.SyncActivity;
import com.hug.mma.preference.AppPreference;

import java.util.ArrayList;
import java.util.List;


public class AppUtil {

    public static void dashboard(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public static void signOut(Context context) {
        cleanAppData();
        dashboard(context);
    }

    private static void cleanAppData() {
        //DataHandler.getInstance().clearData();
        AppPreference.getInstance().clear();
        //PaperDB.getInstance().destroy();
    }

    public static void sync(Context context) {
        Intent intent = new Intent(context, SyncActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        ((Activity) context).finish();
    }


    public static List<Integer> getPowers(long n) {
        List<Integer> pwrs = new ArrayList<>();
        int power = 0;
        while (n != 0) {
            if ((n & 1) != 0) {
                pwrs.add(1 << power);
            }
            ++power;
            n >>>= 1;
        }
        return pwrs;
    }
}
