package com.chenliang.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chenliang.BaseResponse

open class BaseViewModel : ViewModel() {

    var dataMap = HashMap<String, MutableLiveData<BaseResponse<Any>>>()

}