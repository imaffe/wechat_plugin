package com.example.affe.kfc_wx;

public final class WXAccessTokenInfo {
    private String access_token;
    private String open_id;
    private String refresh_token;
    public String getAccess_token(){
        return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }
    public String getOpenid(){
        return open_id;
    }
}
