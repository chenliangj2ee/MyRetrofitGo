package com.chenliang.vm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chenliang.BaseResponse
import com.chenliang.model.BeanRemind
import com.chenliang.net.*

class TestViewModel : ViewModel() {

    /**
     * data为Object时，使用initData
     */
    var data1 = initData<BeanRemind>()
    /**
     * data为Array时，initDatas
     */
    var data2 = initDatas<BeanRemind>()

    /**
     * 获取Object数据测试，必须返回 MutableLiveData<BaseResponse<T>>类型
     */
    fun test(con: Context, name: String): MutableLiveData<BaseResponse<BeanRemind>> {
        go({ API.getData("tom", "23") }) { data1.value = it }
        return data1
    }

    /**
     * 获取Array数据测试，必须返回 MutableLiveData<BaseResponse<ArrayList<T>>>类型
     */
    fun tests(con: Context, name: String): MutableLiveData<BaseResponse<ArrayList<BeanRemind>>> {
        go({ API.getDatas("tom", "23") }) { data2.value = it }
        return data2
    }


}