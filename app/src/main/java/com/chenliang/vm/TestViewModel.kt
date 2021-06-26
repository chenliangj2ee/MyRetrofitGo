package com.chenliang.vm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chenliang.BaseResponse
import com.chenliang.model.BeanRemind
import com.chenliang.net.*

class TestViewModel : ViewModel() {

    var data1 = initData<BeanRemind>()
    var data2 = initDatas<BeanRemind>()
    fun test(con: Context, name: String): MutableLiveData<BaseResponse<BeanRemind>> {
        go({ API.getData("tom", "23") }) { data1.value = it }
        return data1
    }


    fun tests(con: Context, name: String): MutableLiveData<BaseResponse<ArrayList<BeanRemind>>> {
        go({ API.getDatas("tom", "23") }) { data2.value = it }
        return data2
    }


}