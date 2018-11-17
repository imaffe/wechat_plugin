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
    public String setReceiver(String g, String m){
        UnityMessageSender.setReceiver(g,m);
        return "set Receiver Success";
    }

    public String weiLogin(String GameObject, String MethodName){
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
}
