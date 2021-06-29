package com.chenliang.net

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.chenliang.BaseResponse
import com.chenliang.InterfaceApi
import com.chenliang.MyApiFactory
import com.chenliang.annotation.MyRetrofitGo
import com.chenliang.annotation.MyRetrofitGoValue
import com.chenliang.model.BeanRemind
import com.chenliang.utils.SpUtils
import com.google.gson.Gson
import com.google.gson.stream.MalformedJsonException
import gorden.rxbus2.RxBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONException
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.http.GET
import retrofit2.http.POST
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
    funcResponse: (BaseResponse<T>) -> Unit,
) {


    viewModelScope.launch(Dispatchers.IO) {
        var path = ""
        var myRetrofitGoValue: MyRetrofitGoValue
        var responseBean = try {
            var cell = block()
            path = cell.request().url.toString()


            //是否启用缓存
            myRetrofitGoValue = getMyRetrofitGoValue(path)
            if (myRetrofitGoValue.cache) {
                initCache(myRetrofitGoValue, path!!, funcResponse, viewModelScope)
            }

            delay(1000)//模拟延迟,上线的时候，注释掉
            var res = cell.execute()
            if (res != null && res.isSuccessful) {
                res.body()
            } else {
                var bean = BaseResponse<T>()
                if (res.code() == 404) bean.errmsg = "找不到地址"
                bean
            }

        } catch (e: Exception) {
            apiException<T>(e)
        }

        viewModelScope.launch(Dispatchers.Main) { funcResponse(responseBean!!) }
        //把数据更新到缓存
        if (responseBean?.errno == 0) {
            SpUtils.putCache(path, responseBean);
        }

        delay(100)
        RxBus.get().send(31415927, path)
    }
}

/**
 * 获取MyRetrofitGo注解loading和cache
 */
fun getMyRetrofitGoValue(path: String): MyRetrofitGoValue {
    MyApiAnno.value.forEach {
        if (path.split("?")[0].endsWith(it.key)) {
            return it.value
        }
    }
    return MyRetrofitGoValue(loading = true, cache = true, hasCacheLoading = false)
}

/**对象
 * MutableLiveData<BaseEntity<T>简写方案
 */
fun <T> ViewModel.initData() = lazy { MutableLiveData<BaseResponse<T>>() }

/**数组
 * MutableLiveData<BaseEntity<ArrayList<T>>>简写方案
 */
fun <T> ViewModel.initDatas() = lazy { MutableLiveData<BaseResponse<ArrayList<T>>>() }


/**
 * 网络数据请求成功
 */
fun <T> BaseResponse<T>.y(func: () -> Unit) {
    if (this.errno == 0 && !this.cache) func()
}

/**
 * 缓存数据请求成功
 */
fun <T> BaseResponse<T>.c(func: () -> Unit) {
    if (this.errno == 0 && this.cache) func()
}

/**
 * 网络数据请求失败
 */
fun <T> BaseResponse<T>.n(func: () -> Unit) {
    if (this.errno != 0 && !this.cache) func()
}

/**
 * 观察者监听数据变化
 */
fun <T> MutableLiveData<T>.obs(owner: LifecycleOwner, func: (t: T) -> Unit) = this.apply {
    if (!this.hasObservers())
        this.observe(owner, Observer<T> { func(it) })
}


/**
 * 从缓存里获取数据,没缓存时，显示loadingDialog，并执行response方法
 */
fun <T> initCache(
    myRetrofitGoValue: MyRetrofitGoValue,
    path: String,
    func: (BaseResponse<T>) -> Unit,
    viewModelScope: CoroutineScope
) {

    var hasCache = false

    if (!path.isNullOrEmpty()) {
        var cacheResponse = SpUtils.getCache(path, BaseResponse<T>()::class.java)

        if (cacheResponse != null) {
            cacheResponse.cache = true
            viewModelScope.launch(Dispatchers.Main) { func(cacheResponse) }
            hasCache = true
        }

    }

    if (hasCache) {
        if (myRetrofitGoValue.hasCacheLoading) {
            RxBus.get().send(31415926, path)//显示dialog
        }
    } else if (myRetrofitGoValue.loading) {
        RxBus.get().send(31415926, path)//显示dialog
    }


}

/**
 * 网络异常处理
 */
private fun <T> apiException(e: Exception): BaseResponse<T> {
    e.printStackTrace()
    var bean = BaseResponse<T>()
    when (e) {
        is SocketTimeoutException -> bean.errmsg = "网络超时"
        is HttpException -> {
            when {
                e.code() == 403 -> bean.errmsg = "访问被拒绝"
                e.code() == 404 -> bean.errmsg = "找不到路径"
                e.code().toString().startsWith("4") -> bean.errmsg = "客户端异常"
                e.code().toString().startsWith("5") -> bean.errmsg = "服务器异常"
            }
        }
        is UnknownHostException -> bean.errmsg = "找不到服务器，请检查网络"
        is JSONException -> bean.errmsg = "数据解析异常，非法JSON"
        is MalformedJsonException -> bean.errmsg = "数据解析异常，非法JSON"
        is Exception -> bean.errmsg = "程序异常" + e.javaClass.name
    }
    return bean
}


/**
 *简写ViewModel创建
 */

fun <T : ViewModel> Any.initVM(fa: FragmentActivity, modelClass: Class<T>) =
    ViewModelProviders.of(fa).get(modelClass)

/**
 *简写ViewModel创建
 */

fun <T : ViewModel> FragmentActivity.initVM(modelClass: Class<T>) =
    ViewModelProviders.of(this).get(modelClass)

/**
 *简写ViewModel创建
 */

fun <T : ViewModel> Fragment.initVM(modelClass: Class<T>) =
    ViewModelProviders.of(this).get(modelClass)

fun String.log() {
    Log.i("MyLog", this)
}


fun Any.toJson(): String = Gson().toJson(this)

