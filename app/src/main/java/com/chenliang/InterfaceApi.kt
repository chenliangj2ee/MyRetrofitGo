package com.chenliang

import com.chenliang.annotation.MyRetrofitGo
import com.chenliang.model.BeanRemind
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

typealias Data<T> = Call<BaseResponse<T>>
typealias Datas<T> = Call<BaseResponse<ArrayList<T>>>
/**
 * 说明：接口返回类型必须为：Call<BaseResponse<T>
 */
interface InterfaceApi {


    @MyRetrofitGo(loading = true, cache = true, hasCacheLoading = false)
    @POST("home/remind")
    fun getData(
        @Query("username") username: String,
        @Query("age") age: String,
    ): Data<BeanRemind>

    @MyRetrofitGo(tag = "获取用户",loading = false, cache = false)
    @GET("home/getUser")
    fun getDatas(
        @Query("username") username: String,
        @Query("age") age: String,
    ): Datas<BeanRemind>

}