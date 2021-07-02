# MyRetrofitGo
## 一、史上最精简的【带有缓存】的【网络数据加载】封装，Kotlin语言实现Retrofit2 结合OkHttp3网络层，ViewModel技术，使用Kotlin协程，加载网络数据，并添加缓存功能,，同时针对ApiService接口添加注解配置，来配置是否显示loadingDiaog、是否启用缓存功能，并且长按Activity可随时查看当前页面的所有网络请求LOG信息，减轻开发工作，且增加用户体验，堪称史上最简洁的代码，实现你想要的功能；


## 二、缓存逻辑：
####           1、无缓存：  调用接口 -> 无缓存 -> 加载网络数据   ->  网络数据更新UI -> 更新网络数据到缓存
####           2、有缓存：  调用接口 -> 有缓存 -> 缓存数据更新UI ->  加载网络数据   -> 网络数据更新UI  -> 更新网络数据到缓存


## 三、接口声明：
```
/**
 * 说明：接口返回类型必须为：Call<BaseResponse<T>>,可以简写为Data<T>或者Datas<T>
 *@MyRetrofitGo注解说明：
 *  tag：给接口定义个名字：方便http view查看，默认显示 @POST("home/remind")里的url
 *  loading：是否显示loadingDialog，默认true
 *  cache：是否启用缓存功能，默认true
 *  hasCacheLoading：存在缓存数据时，是否显示loading，默认false
 *@接口方法返回说明【BeanRemind为定义的数据模型】:
 *  如果接口返回数据类型为对象：则返回Data<BeanRemind>
 *  如果接口返回数据类型为数组：则返回Datas<BeanRemind>
 */
interface InterfaceApi {

     @MyRetrofitGo(tag = "获取提醒",loading = true, cache = true, hasCacheLoading = false)
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

