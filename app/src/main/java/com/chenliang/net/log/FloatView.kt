package com.chenliang.net.log

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import com.chenliang.R

@SuppressLint("AppCompatCustomView")
class FloatView(context: Context?) : Button(context) {
    var show=false;
    var add=false
    var logs=ArrayList<BeanLog>()
    var dialog=LogDialog()
    init {
        setBackgroundResource(R.drawable.float_button)
        text = "GO"
        Handler().postDelayed(Runnable { show(0f,0f) },500)
    }

      fun show(x:Float, y:Float) {
         if(!add){
             add=true
             show=false
             this.x=x-100
             this.y=y-300
             (context as Activity).addContentView(this, ViewGroup.LayoutParams(100,100))
             visibility =  View.GONE
             return
         }

          visibility = if(show){
              show=!show
              View.GONE
          }else{
              show=!show
              this.setOnClickListener {
                  Log.i("MyLog", "setOnClickListener")
                  dialog.show((context as FragmentActivity).supportFragmentManager,"")
              }
              View.VISIBLE

          }
          this.x=x-100
          this.y=y-300
    }


    fun addLog(log:BeanLog){
        logs.add(log)
        dialog.setData(logs)
    }

}

