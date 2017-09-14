package com.humming.pjmember.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Elvira on 2017/5/31.
 */

public class SharePrefUtil {

    public static void putBoolean(String fileName, String key, boolean value, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String fileName, String key, boolean defValue, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static void putString(String fileName, String key, String value, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(String fileName, String key, String defValue, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static void putInt(String fileName, String key, int value, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static int getInt(String fileName, String key, int defValue, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    public static void remove(String fileName, String key, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }
}
