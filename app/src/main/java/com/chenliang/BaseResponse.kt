package com.chenliang

import java.io.Serializable

class BaseResponse<T> : Serializable {
    var cache = false
    var errno: Int = -1
    var errmsg: String = "程序异常！"
    var total: Int = -1
    var data: T? = null

}
