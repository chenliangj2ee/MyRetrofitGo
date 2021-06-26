package com.chenliang

import com.chenliang.model.BeanRemind
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

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