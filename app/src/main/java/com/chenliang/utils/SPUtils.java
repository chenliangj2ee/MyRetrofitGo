package com.chenliang.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class SPUtils {
    public static String CACHENAME = "SPUtils-android";

    public static void putString(Context context, String key, String value) {

        Log.i("SPUtils", "put key:" + key + "  value:" + value);
        SharedPreferences sp = context.getSharedPreferences(CACHENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(CACHENAME, Context.MODE_PRIVATE);
        String result = sp.getString(key, "");
        Log.i("SPUtils", "get key:" + key + "  value:" + result);
        return result;
    }

    public static void putBoolean(Context context, String key, boolean value) {

        Log.i("SPUtils", "put key:" + key + "  value:" + value);
        SharedPreferences sp = context.getSharedPreferences(CACHENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(CACHENAME, Context.MODE_PRIVATE);
        boolean result = sp.getBoolean(key, false);
        Log.i("SPUtils", "get key:" + key + "  value:" + result);
        return result;
    }


    /**
     * 获取首页医生统计数据
     * key医生getToken
     *
     * @param con
     * @return
     */
    public static <T> T getCache(Context con, String key, Class<T> clazz) {
        return getObject(con, key, clazz);
    }

    /**
     * 获取首页医生统计数据
     * key医生getToken
     *
     * @param con
     * @param beans
     */
    public static void putCache(Context con, Object bean, String key) {
        SPUtils.putString(con, key, new Gson().toJson(bean));
    }


    // 通过类名字去获取一个对象
    public static <T> T getObject(Context context, String key, Class<T> clazz) {
        String json = getString(context, key);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    // 通过Type去获取一个泛型对象
    public static <T> ArrayList<T> getObjects(Context context, String key, Type type) {
        String json = getString(context, key);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, type);
        } catch (Exception e) {
            return null;
        }
    }
}
