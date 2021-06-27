package com.chenliang.act

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import gorden.rxbus2.RxBus
import gorden.rxbus2.Subscribe
import gorden.rxbus2.ThreadMode

open class MyBaseActivity : AppCompatActivity() {

    lateinit var bus: BusLoading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bus = BusLoading(this)
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

        init {
            RxBus.get().register(this)
        }

        var dialogs = HashMap<String, ProgressDialog>()

        @Subscribe(code = 31415926, threadMode = ThreadMode.MAIN)
        fun showLoading(id: String) {
            Log.i("MyLoading","showLoading.....$id")
            var dialog = ProgressDialog(act)
            dialog.setMessage("加载中")
            dialog.show()
            dialogs[id] = dialog

        }

        @Subscribe(code = 31415927, threadMode = ThreadMode.MAIN)
        fun closeLoading(id: String) {
            dialogs[id]?.dismiss()
            dialogs.remove(id)
            Log.i("MyLoading","closeLoading.....$id")
        }

        fun stop() {
            RxBus.get().unRegister(this)
            for (d in dialogs) {
                d.value.dismiss()
            }
        }
    }

}