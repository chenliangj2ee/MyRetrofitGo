package com.chenliang

import android.app.Application
import android.content.Context

class MyApplication : Application() {

    companion object {
        var con: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        MyApplication.con = applicationContext
    }



}