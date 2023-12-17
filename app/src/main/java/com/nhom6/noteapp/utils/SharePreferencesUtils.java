package com.nhom6.noteapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.nhom6.noteapp.Constance;

public class SharePreferencesUtils {
    private static SharedPreferences mSharePref;

    public static void init(Context context) {
        if (mSharePref == null) {
            mSharePref = context.getSharedPreferences(Constance.NOTE_APP, Context.MODE_PRIVATE);
        }
    }

    public static void putString(String key, String value) {
        SharedPreferences.Editor edit = mSharePref.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static String getString(String key, String defaultValue) {
        return mSharePref.getString(key, defaultValue);
    }

    public static void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor edit = mSharePref.edit();
        if (value != null) {
            edit.putBoolean(key, value);
        }
        edit.apply();
    }

    public static Boolean getBoolean(String key, Boolean defaultValue) {
        return mSharePref.getBoolean(key, defaultValue);
    }

    public static boolean isRated(Context context) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return pre.getBoolean("rated", false);
    }

    public static void forceRated(Context context) {
        SharedPreferences pre = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putBoolean("rated", true);
        editor.apply();
    }
}
