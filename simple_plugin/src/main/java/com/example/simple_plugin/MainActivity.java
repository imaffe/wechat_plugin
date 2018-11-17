package com.example.simple_plugin;

import android.os.Bundle;

import com.example.simple_plugin.wxapi.TestWxapi;
import com.example.simple_plugin.wxapi.WXEntryActivity;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mm.opensdk.utils.Log;
import com.unity3d.player.UnityPlayerActivity;

public class MainActivity extends UnityPlayerActivity {

    private static IWXAPI m_wxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_wxapi = WXAPIFactory.createWXAPI(this, AppConst.WEIXIN_APP_ID);
        m_wxapi.registerApp(AppConst.WEIXIN_APP_ID);
    }
    public String test(){return "simple link success";}

    public static String weiLogin(){
        if(m_wxapi == null) {
            UnityMessageSender.Send("message send test");
            return "no wxapi created";
        }
        if (!m_wxapi.isWXAppInstalled()) {
            return "not installed";
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "app_wechat";
        UnityMessageSender.Send("unity message send success");
        m_wxapi.sendReq(req);
        return "return value success";
    }

    public static String  registerApp(){
        if(null != m_wxapi){
            m_wxapi.registerApp(AppConst.WEIXIN_APP_ID);
            return "register success";
        }
        else return "register failed";
    }
}
