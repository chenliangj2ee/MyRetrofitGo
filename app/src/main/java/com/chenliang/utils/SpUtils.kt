package com.chenliang.utils

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.chenliang.MyApplication
import com.google.gson.Gson
import java.lang.reflect.Type
import java.util.*

object SpUtils {
    var CACHENAME = "SPUtils-android"

    private fun putString(
        context: Context,
        key: String,
        value: String
    ) {
        Log.i("SPUtils", "put key:$key  value:$value")
        val sp =
            context.getSharedPreferences(CACHENAME, Context.MODE_PRIVATE)
        val edit = sp.edit()
        edit.putString(key, value)
        edit.commit()
    }

    private fun getString(context: Context, key: String): String? {
        val sp =
            context.getSharedPreferences(CACHENAME, Context.MODE_PRIVATE)
        val result = sp.getString(key, "")
        Log.i("SPUtils", "get key:$key  value:$result")
        return result
    }

    fun <T> getCache(
        key: String,
        clazz: Class<T>?
    ): T? {
        return getObject(MyApplication.con, key, clazz)
    }


    fun putCache(
        key: String?,
        bean: Any?
    ) {
        if (MyApplication.con == null || bean == null || key == null)
            return
        putString(MyApplication.con, key, Gson().toJson(bean))
    }


    private fun <T> getObject(
        context: Context,
        key: String,
        clazz: Class<T>?
    ): T? {
        if(context==null)
            return null
        val json = getString(context, key)
        return if (TextUtils.isEmpty(json)) {
            null
        } else try {
            Gson().fromJson<T>(json, clazz)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun <T> getObjects(
        context: Context,
        key: String,
        type: Type?
    ): ArrayList<T>? {
        val json = getString(context, key)
        return if (TextUtils.isEmpty(json)) {
            null
        } else try {
            Gson().fromJson<ArrayList<T>>(json, type)
        } catch (e: Exception) {
            null
        }
    }
}