# MyRetrofitGo
## 一、Kotlin语言实现Retrofit2 结合OkHttp3网络层，ViewModel技术，使用Kotlin协程，加载网络数据，并添加缓存功能，堪称史上最简洁的代码，实现你想要的功能；
## 二、缓存逻辑：
##            接口调用》存在缓存》刷新UI》请求网络》刷新UI》更新到缓存
## 三、接口声明：
```
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
```

## 四、ViewModel定义

```
 /**
 * 接口调用固定模式： go({  接口调用代码  }) {  value设置代码 }
 */
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
```

## 五、Activity中调用：
```

        var vm = initVM(TestViewModel::class.java)

        //获取Object 
        vm.test(this, "name1").obs(this) {
            //it.c{获取到缓存} it.y{网络数据加载成功},it.n{网络数据加载失败} 无顺序要求
            it.c { "缓存数据${it.toJson()}".log() }//如果使用缓存，可delete该行代码
            it.y { "网络数据${it.toJson()}".log() }
            it.n { "异常数据${it.toJson()}".log() }
        }

        //获取Array，
        vm.tests(this, "name2").obs(this) {
         //it.c{获取到缓存} it.y{网络数据加载成功},it.n{网络数据加载失败} 无顺序要求
            it.c { "缓存数据${it.toJson()}".log() }//如果使用缓存，可delete该行代码
            it.y { "网络数据${it.toJson()}".log() }
            it.n { "异常数据${it.toJson()}".log() }
        }
```
