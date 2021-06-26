package com.chenliang.net

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.chenliang.BaseResponse
import com.chenliang.InterfaceApi
import com.chenliang.MyApiFactory
import com.chenliang.MyApplication
import com.chenliang.utils.SPUtils
import com.google.gson.Gson
import gorden.rxbus2.RxBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONException
import retrofit2.Call
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 接口ApiService
 */
var Any.API: InterfaceApi
    set(value) {}
    get() = MyApiFactory.api!!

/**
 * 创建API
 */
fun <T> Any.initAPI(url: String, cla: Class<T>): T = MyNetWork.initRetrofit(url).create(cla)


fun <T> ViewModel.go(
    block: (CoroutineScope).() -> Call<BaseResponse<T>>,
    funResponse: (BaseResponse<T>) -> Unit,
) {

    viewModelScope.launch(Dispatchers.IO) {

        var dialogId = (Math.random() * 10000).toInt().toString()
        var path = ""
        var bean = try {
            var cell = block()
            path = cell.request().url.toString()
            //缓存处理
            initCache(path!!, funResponse, viewModelScope)
            RxBus.get().send(100, dialogId)
            delay(2000)
            var res = cell.execute()
            if (res != null && res.isSuccessful) {
                res.body()
            } else {
                var bean = BaseResponse<T>()
                if (res.code() == 404) {
                    bean.errmsg = "找不到地址"
                }
                bean
            }

        } catch (e: Exception) {
            apiException<T>(e)
        }

        viewModelScope.launch(Dispatchers.Main) {
            funResponse(bean!!)
        }
        if (bean != null && bean.errno == 0) {
            SPUtils.putString(MyApplication.con, path, bean.toJson());
        }
        RxBus.get().send(101, dialogId)
    }
}


/**对象
 * MutableLiveData<BaseEntity<T>简写方案
 */
fun <T> ViewModel.initData() = MutableLiveData<BaseResponse<T>>()

/**数组
 * MutableLiveData<BaseEntity<ArrayList<T>>>简写方案
 */
fun <T> ViewModel.initDatas() = MutableLiveData<BaseResponse<ArrayList<T>>>()


/**
 * 网络数据请求成功
 */
fun <T> BaseResponse<T>.y(func: () -> Unit) {
    if (this.errno == 0 && !this.cache) {
        func()
    }
}

/**
 * 缓存数据请求成功
 */
fun <T> BaseResponse<T>.c(func: () -> Unit) {
    if (this.errno == 0 && this.cache) {
        func()
    }
}

/**
 * 网络数据请求失败
 */
fun <T> BaseResponse<T>.n(func: () -> Unit) {
    if (this.errno != 0 && !this.cache) {
        func()
    }
}

/**
 * 观察者监听数据变化
 */
fun <T> MutableLiveData<T>.obs(owner: LifecycleOwner, func: (t: T) -> Unit) = this.apply {
    this.observe(owner, Observer<T> {
        func(it)
    })
}


/**
 * 从缓存里获取数据，并执行response方法
 */
fun <T> initCache(path: String, func: (BaseResponse<T>) -> Unit, viewModelScope: CoroutineScope) {
    var func = func
    var bean = SPUtils.getCache(MyApplication.con, path, BaseResponse<T>()::class.java)
    if (bean != null) {
        bean.cache = true
        viewModelScope.launch(Dispatchers.Main) {
            func(bean)
        }

    }
}

/**
 * 网络异常处理
 */
private fun <T> apiException(e: Exception): BaseResponse<T> {
    var bean = BaseResponse<T>()
    when (e) {
        is SocketTimeoutException -> bean.errmsg = "网络超时"
        is HttpException -> {
            when {
                e.code() == 403 -> {
                    bean.errmsg = "访问被拒绝"
                }
                e.code() == 404 -> {
                    bean.errmsg = "找不到路径"
                }
                e.code().toString().startsWith("4") -> {
                    bean.errmsg = "客户端异常"
                }
                e.code().toString().startsWith("5") -> {
                    bean.errmsg = "服务器异常"
                }
            }

        }
        is UnknownHostException -> bean.errmsg = "找不到服务器，请检查网络"
        is JSONException -> bean.errmsg = "数据解析异常，非法JSON"
        is Exception -> bean.errmsg = "程序异常" + e.javaClass.name
    }
    return bean
}


/**
 *简写ViewModel创建
 */

fun <T : ViewModel> Any.initVM(fa: FragmentActivity, modelClass: Class<T>): T {
    return ViewModelProviders.of(fa).get(modelClass)
}

/**
 *简写ViewModel创建
 */

fun <T : ViewModel> FragmentActivity.initVM(modelClass: Class<T>): T {
    return ViewModelProviders.of(this).get(modelClass)
}

/**
 *简写ViewModel创建
 */

fun <T : ViewModel> Fragment.initVM(modelClass: Class<T>): T {
    return ViewModelProviders.of(this).get(modelClass)
}

fun String.log() {
    Log.i("MyLog", this)
}


fun Any.toJson(): String = this.apply {
    return Gson().toJson(this)
} as String
