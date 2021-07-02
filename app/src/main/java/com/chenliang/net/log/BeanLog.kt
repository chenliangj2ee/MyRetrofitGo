package com.chenliang.net.log

import com.google.gson.GsonBuilder
import gorden.rxbus2.RxBus

class BeanLog {
    var tag=""
    var url = ""
    var json = ""

    fun send(tag:String,path:String,json:Any){
        this.tag = if(tag==""){
            "接口：${path.split("?")[0].split("/").last()}"
        }else{
            "接口：$tag"
        }
        this.url=path
        this.json= GsonBuilder() .setPrettyPrinting()  .create().toJson(json)
        RxBus.get().send(31415928, this)
    }
}