package com.example.wxapi;
import android.os.Bundle;

import com.example.wxapi.wxapi.WXEntryActivity;
import com.tencent.mm.opensdk.utils.Log;
import android.text.TextUtils;
import android.widget.Toast;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import okhttp3.Call;

public class MainActivity extends UnityPlayerActivity {
    private static final String WEIXIN_ACCESS_TOKEN_KEY = "wx_access_token_key";
    private static final String WEIXIN_OPENID_KEY = "wx_openid_key";
    private static final String WEIXIN_REFRESH_TOKEN_KEY = "wx_refresh_token_key";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public String printLinkSuccess(){
        return "link success";
    }
    public void weiLogin() {
        WXEntryActivity.loginWeixin(MainActivity.this, MyApplication.sApi, new WXEntryActivity.WeChatCode() {
            @Override
            public void getResponse(String code) {
                // 通过code获取授权口令access_token
                getAccessToken(code);
                Log.i("获取token成功",code.toString());
            }
        });
        Log.i("登陆成功","aaaaaaaaaaa");
    }

    /**
     * 微信登录获取授权口令
     */
    private void getAccessToken(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + AppConst.WEIXIN_APP_ID +
                "&secret=" + AppConst.WEIXIN_APP_SECRET +
                "&code=" + code +
                "&grant_type=authorization_code";
        // 网络请求获取access_token
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                // 判断是否获取成功，成功则去获取用户信息，否则提示失败
                processGetAccessTokenResult(response);
            }
        });

    }

    /**
     * 微信登录处理获取的授权信息结果
     *
     * @param response 授权信息结果
     */
    public void processGetAccessTokenResult(String response) {
        // 验证获取授权口令返回的信息是否成功
        if (validateSuccess(response)) {
            // 使用Gson解析返回的授权口令信息
            //  WXAccessTokenInfo tokenInfo = mGson.fromJson(response, WXAccessTokenInfo.class);
            // 保存信息到手机本地
            //  saveAccessInfotoLocation(tokenInfo);
            // 获取用户信息
            //  getUserInfo(tokenInfo.getAccess_token(), tokenInfo.getOpenid());
        } else {
            // 授权口令获取失败，解析返回错误信息
            //  WXErrorInfo wxErrorInfo = mGson.fromJson(response, WXErrorInfo.class);

        }
    }

    /**
     *微信登录获取tokenInfo的WEIXIN_OPENID_KEY，WEIXIN_ACCESS_TOKEN_KEY，WEIXIN_REFRESH_TOKEN_KEY保存到shareprephence中
     * @param tokenInfo
     */
    private void saveAccessInfotoLocation(WXAccessTokenInfo tokenInfo) {
        ShareUtils.saveValue(MyApplication.mContext,WEIXIN_OPENID_KEY,tokenInfo.getOpenid());
        ShareUtils.saveValue(MyApplication.mContext,WEIXIN_ACCESS_TOKEN_KEY,tokenInfo.getAccess_token());
        ShareUtils.saveValue(MyApplication.mContext,WEIXIN_REFRESH_TOKEN_KEY,tokenInfo.getRefresh_token());
    }

    /**
     * 验证是否成功
     *
     * @param response 返回消息
     * @return 是否成功
     */
    private boolean validateSuccess(String response) {
        String errFlag = "errmsg";
        return (errFlag.contains(response) && !"ok".equals(response))
                || (!"errcode".contains(response) && !errFlag.contains(response));
    }


    /**
     * 微信登录判断accesstoken是过期
     *
     * @param accessToken token
     * @param openid      授权用户唯一标识
     */
    private void isExpireAccessToken(final String accessToken, final String openid) {
        String url = "https://api.weixin.qq.com/sns/auth?" +
                "access_token=" + accessToken +
                "&openid=" + openid;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                if (validateSuccess(response)) {
                    // accessToken没有过期，获取用户信息
                    getUserInfo(accessToken, openid);
                    //把请求到的数据传递到unity
                    UnityPlayer.UnitySendMessage("与安卓交互的C#脚本挂载的物体名","函数名",response.toString());
                    Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();

                } else {
                    // 过期了，使用refresh_token来刷新accesstoken
                    refreshAccessToken();
                }
            }
        });

    }
    /**
     * 微信登录刷新获取新的access_token
     */
    private void refreshAccessToken() {
        // 从本地获取以存储的refresh_token
        final String refreshToken = (String) ShareUtils.getValue(this, WEIXIN_REFRESH_TOKEN_KEY,
                "");
        if (TextUtils.isEmpty(refreshToken)) {
            return;
        }
        // 拼装刷新access_token的url请求地址
        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?" +
                "appid=" + AppConst.WEIXIN_APP_ID +
                "&grant_type=refresh_token" +
                "&refresh_token=" + refreshToken;
        // 请求执行
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                System.out.println("刷新获取新的access_token信息失败！！！");
                // 重新请求授权
                weiLogin();
            }

            @Override
            public void onResponse(String response, int id) {
                // 判断是否获取成功，成功则去获取用户信息，否则提示失败
                processGetAccessTokenResult(response);
            }
        });

    }
    /**
     * 微信token验证成功后，联网获取用户信息
     * @param access_token
     * @param openid
     */
    private void getUserInfo(String access_token, String openid) {
        String url = "https://api.weixin.qq.com/sns/userinfo?" +
                "access_token=" + access_token +
                "&openid=" + openid;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                System.out.println("联网获取用户信息失败！！！");
            }

            @Override
            public void onResponse(String response, int id) {
                // 解析获取的用户信息
                // WXUserInfo userInfo = mGson.fromJson(response, WXUserInfo.class);
                System.out.println("获取用户信息String是：：：：：："+response);

            }
        });
    }
}
