package com.example.simple_plugin.wxapi;

import android.app.Activity;
import android.os.Bundle;

import com.example.simple_plugin.AppConst;
import com.example.simple_plugin.UnityMessageSender;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.unity3d.player.UnityPlayer;

import org.json.JSONObject;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static IWXAPI m_wxapi;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 微信事件回调接口注册
        m_wxapi = WXAPIFactory.createWXAPI(this, AppConst.WEIXIN_APP_ID, false);
        m_wxapi.registerApp(AppConst.WEIXIN_APP_ID);
        UnityMessageSender.Send("onCreate excuted");
        try{
            m_wxapi.handleIntent(getIntent(),this);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                break;
            default:
                break;
        }
    }
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            // 发送成功
            case BaseResp.ErrCode.ERR_OK:
                // 获取code
                String code = ((SendAuth.Resp) resp).code;
                UnityMessageSender.Send(code);
                break;
        }
    }
}