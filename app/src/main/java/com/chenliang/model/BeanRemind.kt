package com.chenliang.model

import java.text.SimpleDateFormat

class BeanRemind {
    private var created: List<BeanTime>? = null
    private var thirty_min: List<BeanTime>? = null
    private var ass_pushed: List<BeanTime>? = null
    private var ass_clinic: List<BeanTime>? = null
    class BeanTime {
        var begin_time: Long = 0
        var end_time: Long = 0
        var tx_room_id: Long = 0
        var type: String? = null
        var clinic_id: String? = null
        val dateDes: String = ""
    }

}