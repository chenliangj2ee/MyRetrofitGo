package com.chenliang.act

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.chenliang.BaseResponse
import com.chenliang.R
import com.chenliang.model.BeanRemind
import com.chenliang.net.*
import com.chenliang.vm.TestViewModel

class MainActivity : MyBaseActivity() {


    lateinit var message: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        message = findViewById(R.id.message)
        var vm = initVM(TestViewModel::class.java)
        //获取Object
        var data1:BaseResponse<BeanRemind>?=null
        vm.test(this, "name1").obs(this) {
            it.c { message.text = "缓存数据${it.toJson()}";data1=it }
            it.y { message.text = "网络数据${it.toJson()}" }
            it.n { if(data1==null){ message.text = "异常数据${it.toJson()}" }}
        }

        //获取Array
        vm.tests(this, "name2").obs(this) {
            it.c { "缓存数据${it.toJson()}".log() }
            it.y { "网络数据${it.toJson()}".log() }
            it.n { "异常数据${it.toJson()}".log() }
        }
    }


}