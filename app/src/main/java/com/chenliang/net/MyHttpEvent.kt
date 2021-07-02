package com.chenliang.net

import android.app.Activity
import android.app.ProgressDialog
import android.os.Handler
import android.view.MotionEvent
import com.chenliang.net.log.BeanLog
import com.chenliang.net.log.FloatView
import gorden.rxbus2.RxBus
import gorden.rxbus2.Subscribe
import gorden.rxbus2.ThreadMode

class MyHttpEvent(act: Activity) {

    var act = act
    var log = true
    var floatButton: FloatView
    var dialogs = HashMap<String, ProgressDialog>()


    init {
        RxBus.get().register(this)
        floatButton = FloatView(act)
    }

    /**
     * 接受展示loading event
     */
    @Subscribe(code = 31415926, threadMode = ThreadMode.MAIN)
    fun eventShowLoading(id: String) {
        var dialog = ProgressDialog(act)
        dialog.setMessage("加载中")
        dialog.show()
        dialogs[id] = dialog

    }

    /**
     * 接受关闭loading event
     */
    @Subscribe(code = 31415927, threadMode = ThreadMode.MAIN)
    fun eventCloseLoading(id: String) {
        dialogs[id]?.dismiss()
        dialogs.remove(id)
    }


    /**
     * 接受log event
     */
    @Subscribe(code = 31415928, threadMode = ThreadMode.MAIN)
    fun eventHttpLog(bean: BeanLog) {
        if (this.log)
            floatButton.addLog(bean)

    }

    /**
     * 注销event，关闭所有loading Dialog
     */
    fun onDestroy() {
        RxBus.get().unRegister(this)
        for (d in dialogs) {
            d.value.dismiss()
        }
    }

    /**
     * 开始接受log
     */
    fun onResume() {
        log = true
    }

    /**
     * 停止接受log
     */
    fun onPause() {
        log = false
    }

    /**
     * 长按Activity，显示网络调试日志
     */
    var downTime = 0L;
    var down = false;
    fun onTouchEvent(event: MotionEvent?) {
        if (event!!.action == MotionEvent.ACTION_DOWN) {
            down = true
            downTime = System.currentTimeMillis()

            Handler().postDelayed(Runnable {
                if (down && System.currentTimeMillis() - downTime > 1000) {
                    this.floatButton.show(event.x, event.y)
                }
            }, 1200)

        } else if (event!!.action == MotionEvent.ACTION_UP) {
            down = false
        }
    }
}