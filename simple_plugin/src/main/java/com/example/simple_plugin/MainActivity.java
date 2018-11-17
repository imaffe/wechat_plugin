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
    public String weiLogin() {

        String resp = WXEntryActivity.wxLogin();
        return resp;
    }

    public String registerAPI(){
        String resp = WXEntryActivity.registerApp();
        return resp;
    }
}
