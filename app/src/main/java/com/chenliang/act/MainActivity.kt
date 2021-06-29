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


    lateinit var message1: TextView
    lateinit var message2: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        message1 = findViewById(R.id.message1)
        message2 = findViewById(R.id.message2)

        var vm = initVM(TestViewModel::class.java)
        //获取Object
        var data1: BaseResponse<BeanRemind>? = null
        vm.test(this, "name1").obs(this) {
            it.c { message1.text = "缓存数据${it.toJson()}";data1 = it }
            it.y { message1.text = "网络数据${it.toJson()}" }
            it.n { if (data1 == null) message1.text = "异常数据${it.toJson()}"
            }
        }
        //获取Array

        vm.tests(this, "name1").obs(this) {
            it.c { message2.text = "缓存数据${it.toJson()}" }
            it.y { message2.text = "网络数据${it.toJson()}" }
            it.n { message2.text = "异常数据${it.toJson()}" }
        }
    }


}