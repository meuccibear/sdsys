package com.adsys.util;

import com.alibaba.fastjson.JSONObject;

public abstract  class Constants {
    /**
     * 请求返回MSG
     */
    public static final  String REQUEST_PARAM_ERROR="请求参数不完整";
    public static final  String REQUEST_OK="请求成功";
    public static final  String REQUEST_FAILL="请求失败";
    public static final  String PERMISSION_FAILL="无访问权限";

    /**
     * 请求返回码
     * 00 请求失败
     * 01 请求成功
     * 02 返回控制
     * 03 请求参数不完整
     * 04 用户名或密码错误
     * 05 系统错误
     *
     */
    public static final  Integer REQUEST_00=00;
    public static final  Integer REQUEST_01=01;
    public static final  Integer REQUEST_02=02;
    public static final  Integer REQUEST_03=03;
    public static final  Integer REQUEST_04=04;
    public static final  Integer REQUEST_05=05;
    public static final  Integer REQUEST_404=404;


    public static final  Integer REQUEST_SOCKET_OK = 200;//连接成功
    public static final  Integer REQUEST_SOCKET_FAIL = 500;//APPKEY验证失败
    public static final  Integer REQUEST_SOCKET_NOT_FOUND = 400;//APPKEY验证成功，但不存在设备列表里
    public static final  Integer REQUEST_SOCKET_AUTH = 300;//TOKEN验证失败

    public static final JSONObject APP_OPERATE = JSONObject.parseObject("{\"tiktok\":[ { \"key\": \"gotolive\", \"name\": \"进入直播间\", \"para\": [ {name: \"roomid\", key: \"roomid\"} ] }, { \"key\": \"continuelike\", \"name\": \"连续点赞\", \"para\": [ {name: \"数量\", key: \"count\"} ] }, { \"key\": \"automode\", \"name\": \"自动浏览模式\", \"para\": [ {name: \"mode\", key: \"mode\", value: ['open', 'close']} ] }, { \"key\": \"like\", \"name\": \"点赞\" }, { \"key\": \"upswip\", \"name\": \"上滑\" }, { \"key\": \"downswip\", \"name\": \"下滑\", }, { \"key\": \"livefollow\", \"name\": \"关注\" }, { \"key\": \"pointclick\", \"name\": \"点击指定坐标\", \"para\": [ {name: \"xp\", key: \"xp\"}, {name: \"yp\", key: \"yp\"} ] } ]}");//TOKEN验证失败

}
