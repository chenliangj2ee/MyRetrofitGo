package com.chenliang

import com.chenliang.model.BeanRemind
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query


/**
 * 说明：接口返回类型必须为：Call<BaseResponse<T>
 */
interface InterfaceApi {


    @POST("home/remind")
    fun getData(
        @Query("username") username: String,
        @Query("age") age: String,
    ): Call<BaseResponse<BeanRemind>>


    @POST("home/remind2")
    fun getDatas(
        @Query("username") username: String,
        @Query("age") age: String,
    ): Call<BaseResponse<ArrayList<BeanRemind>>>

}