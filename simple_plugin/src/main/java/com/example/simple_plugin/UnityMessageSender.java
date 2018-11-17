package com.example.simple_plugin;

import com.unity3d.player.UnityPlayer;

public class UnityMessageSender
{
    private static String GameObject;
    private static String Methodname;
    public static void setReceiver(String _GameObject, String _MethodName){
        GameObject = _GameObject;
        Methodname = _MethodName;
    }
    public static void Send(String json){
        UnityPlayer.UnitySendMessage(GameObject, Methodname , json);
    }
}
