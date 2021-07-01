package com.chenliang.annotation

/**
 * loading:是否显示loadingDialog：true显示，false，不显示，默认true
 * cache：是否启用缓存，true启用，false不启用，默认启用
 * hasCacheLoading:存在缓存数据的情况下，是否还显示loading
 */
@Target(AnnotationTarget.FUNCTION)
annotation class MyRetrofitGo(
    val loading: Boolean = true,
    val cache: Boolean = true,
    val hasCacheLoading: Boolean = false,
    val tag: String = ""
)