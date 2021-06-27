package com.chenliang

import com.chenliang.net.initAPI
import com.chenliang.net.log


object MyApiFactory {
    var base = "http://api.alpha.xiaoliuyisheng.cn/app/doctor/"
    var api: InterfaceApi? = null


    init {
        "network init ....".log()
        api=initAPI(base,InterfaceApi::class.java)
    }

}
