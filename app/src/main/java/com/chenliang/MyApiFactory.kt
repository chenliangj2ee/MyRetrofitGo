package com.chenliang

import com.chenliang.net.initAPI
import com.chenliang.net.log


object MyApiFactory {
    var base = "http://www.baidu.com"
    var api: InterfaceApi? = null


    init {
        api=initAPI(base,InterfaceApi::class.java)
    }

}
