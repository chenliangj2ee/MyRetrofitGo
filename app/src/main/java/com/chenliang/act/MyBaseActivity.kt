package com.chenliang.act

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.chenliang.net.log.BeanLog
import com.chenliang.net.log.FloatView
import gorden.rxbus2.RxBus
import gorden.rxbus2.Subscribe
import gorden.rxbus2.ThreadMode

open class MyBaseActivity : AppCompatActivity() {

    lateinit var bus: BusLoading



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bus = BusLoading(this)

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        bus.stop()
    }


    /**
     * 统一加载进度
     */
    class BusLoading(act: Activity) {
        var act = act
        lateinit var floatButton:FloatView
        init {
            RxBus.get().register(this)
            floatButton= FloatView(act)
        }

        var dialogs = HashMap<String, ProgressDialog>()

        @Subscribe(code = 31415926, threadMode = ThreadMode.MAIN)
        fun showLoading(id: String) {
            Log.i("MyLoading", "showLoading.....$id")
            var dialog = ProgressDialog(act)
            dialog.setMessage("加载中")
            dialog.show()
            dialogs[id] = dialog

        }

        @Subscribe(code = 31415927, threadMode = ThreadMode.MAIN)
        fun closeLoading(id: String) {
            dialogs[id]?.dismiss()
            dialogs.remove(id)
            Log.i("MyLoading", "closeLoading.....$id")
        }


        @Subscribe(code = 31415928, threadMode = ThreadMode.MAIN)
        fun log(log: BeanLog) {
            floatButton.addLog(log)
            Log.i("MyLoading", "log   ${log.json} ")
        }

        fun stop() {
            RxBus.get().unRegister(this)
            for (d in dialogs) {
                d.value.dismiss()
            }
        }
    }


    var downTime = 0L;

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event!!.action == MotionEvent.ACTION_DOWN) {
            downTime = System.currentTimeMillis()

        } else if (event!!.action == MotionEvent.ACTION_MOVE) {
            var upTime = System.currentTimeMillis()
            if (upTime - downTime > 2000) {
                bus.floatButton.show(event.x,event.y)
                downTime = System.currentTimeMillis()
            }
        } else if (event!!.action == MotionEvent.ACTION_UP) {
            var upTime = System.currentTimeMillis()
            if (upTime - downTime > 2000) {
                bus.floatButton.show(event.x,event.y)
                downTime = System.currentTimeMillis()
            }
        }
        return super.onTouchEvent(event)
    }
}