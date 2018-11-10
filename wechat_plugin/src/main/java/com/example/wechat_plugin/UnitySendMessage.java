package com.example.wechat_plugin;

import com.unity3d.player.UnityPlayer;

public class UnitySendMessage {
    public static void Send(String json){
        UnityPlayer.UnitySendMessage(AppConst.UNITY_GAMEOBJECT, AppConst.UNITY_GAMEOBJECT_METHOD, json);
    }
}
