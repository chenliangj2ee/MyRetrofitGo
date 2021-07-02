package com.chenliang.act

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.chenliang.net.MyHttpEvent
import com.chenliang.net.log.BeanLog
import com.chenliang.net.log.FloatView
import gorden.rxbus2.RxBus
import gorden.rxbus2.Subscribe
import gorden.rxbus2.ThreadMode

open class MyBaseActivity : AppCompatActivity() {

    lateinit var httpEvent: MyHttpEvent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        httpEvent = MyHttpEvent(this)

    }

    override fun onResume() {
        super.onResume()
        httpEvent.onResume()
    }

    override fun onPause() {
        super.onPause()
        httpEvent.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        httpEvent.onDestroy()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        httpEvent.onTouchEvent(event)
        return super.onTouchEvent(event)
    }
}