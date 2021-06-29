package com.chenliang.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import com.chenliang.net.API
import com.chenliang.net.go

class TestViewModel : ViewModel() {

    fun test(con: Context, name: String) = go { API.getData("tom", "23") }

    fun tests(con: Context, name: String) = go { API.getDatas("tom", "23") }

}