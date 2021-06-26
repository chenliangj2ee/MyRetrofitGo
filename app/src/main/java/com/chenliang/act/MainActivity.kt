package com.chenliang.act

import android.os.Bundle
import android.widget.Button
import com.chenliang.R
import com.chenliang.net.*
import com.chenliang.vm.TestViewModel

class MainActivity : MyBaseActivity() {


    lateinit var message: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        message=findViewById(R.id.message)

        var vm = initVM(TestViewModel::class.java)

        //加载对象
        vm.test(this, "name1").obs(this) {
            it.c { "缓存数据${it.toJson()}".log();message.text="缓存数据${it.toJson()}"}
            it.y { "网络数据${it.toJson()}".log();message.text="网络数据${it.toJson()}"}
            it.n { "异常数据${it.toJson()}".log() }
        }

        //url异常测试
        vm.tests(this, "name2").obs(this) {
            it.c { "缓存数据${it.toJson()}".log() }
            it.y { "网络数据${it.toJson()}".log() }
            it.n { "异常数据${it.toJson()}".log() }
        }
    }


}