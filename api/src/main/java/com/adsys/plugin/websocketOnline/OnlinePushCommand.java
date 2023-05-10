package com.adsys.plugin.websocketOnline;

import com.adsys.util.PageData;
import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;

import net.sf.json.JSONObject;


public class OnlinePushCommand {

    private final static Logger logger = Logger.getLogger(OnlinePushCommand.class);


    public static boolean pushMessage(String did, String command, String data) {

        JSONObject resp = new JSONObject();

        resp.element("msg_version", "v1");
        resp.element("command", command);
        resp.element("data", data);

        return pushMessageToApp(did, command, resp);
    }


    public static boolean pushMessageToApp(String did, String action, JSONObject data) {
        WebSocket dev = OnlineDeviceServerPool.getWebSocketByUser(did);
        JSONObject requestCmd = new JSONObject();
        requestCmd.element("action", action);
        // 10是请求返回，20是服务器主动推送
        requestCmd.element("resp_event", 20);
        requestCmd.element("seq_id", String.valueOf(System.currentTimeMillis()));
        requestCmd.element("resp", data);
        return OnlineDeviceServerPool.sendMessageToUser(dev, requestCmd.toString());
    }
}
