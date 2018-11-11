package com.example.simple_plugin;

import com.unity3d.player.UnityPlayer;

public class UnityMessageSender
{
    public static void Send(String json){
        UnityPlayer.UnitySendMessage(AppConst.UNITY_GAMEOBJECT, AppConst.UNITY_GAMEOBJECT_METHOD, json);
    }
}
