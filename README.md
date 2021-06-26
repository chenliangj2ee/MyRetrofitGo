# MyRetrofitGo
## 一、Kotlin语言实现Retrofit2 结合OkHttp3网络层，ViewModel技术，使用Kotlin协程，加载网络数据，并添加缓存功能，减轻开发工作，且增加用户体验，堪称史上最简洁的代码，实现你想要的功能；


## 二、缓存逻辑：
####           1、无缓存：  调用接口 -> 无缓存 -> 加载网络数据   ->  网络数据更新UI -> 更新网络数据到缓存
####           2、有缓存：  调用接口 -> 有缓存 -> 缓存数据更新UI ->  加载网络数据   -> 网络数据更新UI  -> 更新网络数据到缓存


## 三、接口声明：
```
/**
 * 说明：接口返回类型必须为：Call<BaseResponse<T>>
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
 * 接口调用固定写法： go({  接口调用  }) {  xxx.value=it }
 */
class TestViewModel : ViewModel() {

    var data1 = initData<BeanRemind>()  // data为Object时，使用initData
   
    var data2 = initDatas<BeanRemind>()  // data为Array时，initDatas

    //获取Object数据测试，必须返回 MutableLiveData<BaseResponse<T>>类型
    fun test(con: Context, name: String): MutableLiveData<BaseResponse<BeanRemind>> {
        go({ API.getData("tom", "23") }) { data1.value = it }
        return data1
    }

    //获取Array数据测试，必须返回 MutableLiveData<BaseResponse<ArrayList<T>>>类型
    fun tests(con: Context, name: String): MutableLiveData<BaseResponse<ArrayList<BeanRemind>>> {
        go({ API.getDatas("tom", "23") }) { data2.value = it }
        return data2
    }
}
```

## 五、Activity中调用：
```
        var vm = initVM(TestViewModel::class.java)

        //获取Object数据
        vm.test(this, "name1").obs(this) {
            it.c { "缓存数据${it.toJson()}".log() } 
            it.y { "网络数据${it.toJson()}".log() } 
            it.n { "异常数据${it.toJson()}".log() } 
        }

        //获取Array数据
        vm.tests(this, "name2").obs(this) {
            it.c { "缓存数据${it.toJson()}".log() } 
            it.y { "网络数据${it.toJson()}".log() } 
            it.n { "异常数据${it.toJson()}".log() } 
        }
        
```
#####    数据回调使用说明：it.c{ } , it.y{ } , it.n{ } 三个代码块，调用顺序可随意，只要你高兴，【c,y,n分别对应cache，yes no的意思，你懂的】
#####     it.c { "缓存数据${it.toJson()}".log() }//获取到缓存数据，如果不使用缓存，可delete该行代码，如果数据分页，当page==1时，才执行该段代码
#####     it.y { "网络数据${it.toJson()}".log() }//网络加载数据成功,如果数据分页，当page==1时，清空缓存数据，再添加最新数据
#####     it.n { "异常数据${it.toJson()}".log() }//网络加载数据失败，如果不处理失败，可delete该行代码

### 效果
![Video_20210626_034427_427](https://user-images.githubusercontent.com/4067327/123506188-91d41a80-d695-11eb-96aa-183b7d49325d.gif)

# 请资助我一个棒棒糖吧，在此感谢：

<img width="406" alt="微信图片_20210609173434" src="https://user-images.githubusercontent.com/4067327/121332592-989b2780-c94a-11eb-9543-a4e00db3b759.png">

