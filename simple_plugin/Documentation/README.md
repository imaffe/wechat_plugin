

## Unity 脚本调用 Android Plugin

#### 文件夹位置

- 将AndroidManifest.xml 放到 Assets->Plugins->Android目录下

- 将aar 放在 Unity Project的Assets->Plugins->Android目录下，用压缩软件打开aar，删除libs目录下的classes.jar就可以导入成功了

#### Android Lib中的接口函数

- public String weiLogin()

  调用此函数会发送Req到微信，用户拉起后会通过WXEntryActivity.OnResp 调用 UnityMessageSender.Send()函数，Send函数的作用是发送一个字符串到Unity某个GameObject上绑定的脚本中的某个方法

- public  String setReceiver(string g, string m)

  设置返回的code 将要传给Unity的目标： g是Unity GameObject，m是方法名称，该方法必须是一个类似于

  ``` c#
  void OnRecvCode(string message){...}
  ```

  的声明式，Android穿过来的code（或者消息）在message中，由此可以进行部分调用

#### C#脚本获得访问

``` c#
AndroidJavaClass jc = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
AndroidJavaObject jo = jc.GetStatic<AndroidJavaObject>("currentActivity");
## jo.Call<returnType>(name,arg1..arg2) 都是string的参数
## jo.CallStatic<returnType>(name,arg1,arg2...) 都是string的参数
## example:
string set_res = jo.Call<string>("setReceiver","textLabel","onRecvCode");
string login_res = jo.Call<string>("weiLogin");
```

## Android Plugin 代码结构

#### MainActivity的解释

MainActivity是Unity调用Android Lib的接口类，建议把所有open to Unity 的method都做成这个类中的public method. 其中的两个函数都已经在上一部分讲过了，讲一下OnCreate()

OnCreate 需要创建一个MainActivity的WXApi，（WXEntryActivity中也有一个，这是两个独立的api, 我们通过MainActivity的Api发送请求到微信指定的host, 而微信处理的结果则是放在WXEntryActivity回调类中进行处理

#### WXEntryActivity的解释，

WXEntryActivity的声明必须和code 一样，也就是继承Activity并且implement IWXAPIEvenHandler, 所有的注册都按照code的写法进行，就行了，（create 和 register 两个缺一不可），然后就是WXEntryActivity必须完成的两个函数，OnResp 和 OnReq, 在我们这里，只用完成OnResp就可以了，当我们接收到微信发送的code时，不经处理直接把code通过UnityMessageSender工具类发送给Unity上的某个Object，这样通讯就完成了。

## Client Server 交互模式

- client获得code，请求client的一个service 叫 kfsWechatGetAuthTokens，通过这个拿到一系列的tokenID，openID,unionID,然后再把三个东西发送给Server的 kfsLoginWechatCL service (以上两个service 再 kfsClientService.py里面)请求登陆，server拿这三个信息得到用户信息，存到数据库里面去。



