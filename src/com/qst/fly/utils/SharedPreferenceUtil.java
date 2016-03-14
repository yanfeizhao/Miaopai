package com.qst.fly.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zujinhao on 15/9/8.
 */
public class SharedPreferenceUtil {
    private static String sharedPreferenceName = "default";

    public static void setSharedPreferenceName(String sharedPreferenceName){
        SharedPreferenceUtil.sharedPreferenceName = sharedPreferenceName;
    }

    public static String getString(Context context,String tag) {
        SharedPreferences pref = context.getSharedPreferences(sharedPreferenceName, context.MODE_PRIVATE);
        return pref.getString(tag,"");
    }
    public static boolean getBoolean(Context context,String tag,boolean defValue) {
        SharedPreferences pref = context.getSharedPreferences(sharedPreferenceName, context.MODE_PRIVATE);
        return pref.getBoolean(tag, defValue);
    }
    public static float getFloat(Context context,String tag) {
        SharedPreferences pref = context.getSharedPreferences(sharedPreferenceName, context.MODE_PRIVATE);
        return pref.getFloat(tag, 0.0f);
    }
    public static int getInt(Context context,String tag) {
        SharedPreferences pref = context.getSharedPreferences(sharedPreferenceName, context.MODE_PRIVATE);
        return pref.getInt(tag, 0);
    }
    public static long getLong(Context context,String tag) {
        SharedPreferences pref = context.getSharedPreferences(sharedPreferenceName, context.MODE_PRIVATE);
        return pref.getLong(tag, 0);
    }

    public static <T> void addInSharePreference(Context context,String tag,T value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(sharedPreferenceName,Context.MODE_PRIVATE).edit();
        if (value instanceof  String){
            editor.putString(tag,(String)value);
        }
        if (value instanceof  Boolean){
            editor.putBoolean(tag, (Boolean) value);
        }
        if (value instanceof  Integer){
            editor.putInt(tag, (Integer) value);
        }
        if (value instanceof  Long){
            editor.putLong(tag, (Long) value);
        }
        if (value instanceof  Float){
            editor.putFloat(tag, (Float) value);
        }
        editor.commit();
    }
}
