

## Unity 脚本调用 Android Plugin

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

#### WXEntryActivity的解释

