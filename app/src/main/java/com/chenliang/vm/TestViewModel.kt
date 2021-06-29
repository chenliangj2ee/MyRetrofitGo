package com.chenliang.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import com.chenliang.net.API
import com.chenliang.net.go

class TestViewModel : ViewModel() {

    /**
     * 获取Object数据测试，必须返回 MutableLiveData<BaseResponse<T>>类型
     */
    fun test(con: Context, name: String) = go { API.getData("tom", "23") }

    /**
     * 获取Array数据测试，必须返回 MutableLiveData<BaseResponse<ArrayList<T>>>类型
     */
    fun tests(con: Context, name: String) = go { API.getDatas("tom", "23") }

}