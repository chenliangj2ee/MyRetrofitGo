# MyRetrofitGo
## 一、史上最精简的【带有二级缓存】的【网络请求】封装，Kotlin语言实现，Retrofit2 结合OkHttp3网络层，ViewModel技术，使用Kotlin协程加载网络数据；并对网络层添加2级缓存功能——内存缓存和文件缓存；同时针对ApiService接口通过注解的形式来配置是否显示loadingDiaog、是否启用缓存功能；开发者可长按Activity后随时查看当前页面的所有网络请求LOG信息，减轻开发工作，且增加用户体验，堪称史上最简洁的代码，实现你想要的功能；该项目，是以上功能实现的一个方案，具体在项目中的封装使用，请参考：https://github.com/chenliangj2ee/MVVM-Component


## 二、缓存逻辑：【开发者无需关心内部实现】
    1、无缓存：  调用接口->无内存缓存->无文件缓存-> 加载网络数据->网络数据更新UI->更新网络数据到内存和文件缓存
    2、有文件缓存：调用接口 -> 无内存缓存 ->有文件缓存  ->更新到内存缓存 -> 缓存数据更新UI-> 加载网络数据 -> 网络数据更新UI-> 更新网络数据到内存和文件缓存
    3、有内存缓存： 调用接口-> 有内存缓存-> 缓存数据更新UI-> 加载网络数据 -> 网络数据更新UI -> 更新网络数据到内存和文件缓存

## 三、接口声明：
```
/**
 * 说明：接口返回类型必须为：Call<BaseResponse<T>>,可以简写为Data<T>或者Datas<T>
 *@MyRetrofitGo注解说明：
 *  mTag：给接口定义个名字：方便http view查看，默认显示 @POST("home/remind")里的url
 *  mLoading：是否显示loadingDialog，默认true
 *  mCache：是否启用缓存功能，默认true
 *  mHasCacheLoading：存在缓存数据时，是否显示loading，默认false
 *  mFailToast：是否启用接口失败情况下，自动弹toast提示；
 *  mSuccessCode：接口成功后，发送的Event事件的code，比如：在详情页删除数据，前一页列表页，需要刷新列表；可以监听该code事件
 *@接口方法返回说明【BeanRemind为定义的数据模型】:
 *  如果接口返回数据类型为对象：则返回Data<BeanRemind>
 *  如果接口返回数据类型为数组：则返回Datas<BeanRemind>
 */
interface InterfaceApi {

    @MyRetrofitGo(mTag = "获取提醒", mLoading = true,mCache = true ,mHasCacheLoading = false,mFailToast = true,mSuccessCode = 1001)
     @POST("home/remind")
     fun getData(
         @Query("username") username: String,
         @Query("age") age: String,
     ): Data<BeanRemind>
 
     @MyRetrofitGo(loading = false, cache = false)
     @GET("home/getUser")
     fun getDatas(
         @Query("username") username: String,
         @Query("age") age: String,
     ): Datas<BeanRemind>

}
```

## 四、ViewModel定义

```
 /**
 * 继承BaseViewModel，接口调用固定写法：fun youFunction(参数...) = go { APIService接口调用 }
 */
class TestViewModel : BaseViewModel() {

    fun test(name: String) = go { API.getData(name, "23") }

    fun tests(name: String) = go { API.getDatas(name, "23") }
 
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
## 六、数据回调方法说明：【 c：cache简写；y：yes简写；n：no简写；调用顺序可随意】

```
it.c {  } : 缓存数据加载成功回调；如果不使用缓存，可delete该行代码；如果数据分页，当page==1时，才使用缓存数据；
it.y {  } : 网络数据加载成功回调；如果不处理成功，可delete该行代码；如果数据分页，当page==1时，清空list里缓存数据，再添加网络数据到list；
it.n {  } : 网络数据加载失败回调；如果不处理失败，可delete该行代码；如果数据加载失败，且缓存数据存在，根据需要写失败逻辑；

以上代码只是以下代码的简写【省略了else，只为让代码看起来更优雅，更性感，3个If块只会同时执行一个】：

if(it.errno==0&&it.cache){
    //缓存
}
if(it.errno==0&&!it.cache){
    //成功
}
if(it.errno!=0&&!it.cache){
    //失败
}

```

### 七、效果
![Video_20210626_034427_427](https://user-images.githubusercontent.com/4067327/123506188-91d41a80-d695-11eb-96aa-183b7d49325d.gif) ![Video_20210627_121527_755](https://user-images.githubusercontent.com/4067327/123532635-95bd7680-d741-11eb-9c58-7e89069f31e0.gif)

### 八、随时查看http请求Log【Activity长按1秒，可查看当前Activity的所有网络请求log】
![Video_20210702_092144_23](https://user-images.githubusercontent.com/4067327/124281158-074f5780-db7c-11eb-972e-e68f5ff48d51.gif)



# 请资助我一个棒棒糖吧，在此感谢：

<img width="406" alt="微信图片_20210609173434" src="https://user-images.githubusercontent.com/4067327/121332592-989b2780-c94a-11eb-9543-a4e00db3b759.png"> 

