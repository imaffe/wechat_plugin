package com.example.simple_plugin;

import android.os.Bundle;

import com.example.simple_plugin.wxapi.TestWxapi;
import com.example.simple_plugin.wxapi.WXEntryActivity;
import com.tencent.mm.opensdk.utils.Log;
import com.unity3d.player.UnityPlayerActivity;

public class MainActivity extends UnityPlayerActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public String test(){return "simple link success";}
    public void weiLogin() {
        WXEntryActivity.registerApp();
        WXEntryActivity.wxLogin();
        //TestWxapi.testWxapi();
        //Log.i("登陆成功","aaaaaaaaaaa");
    }
}
