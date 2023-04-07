package com.adsys.plugin.websocketOnline;

import com.adsys.util.PageData;
import org.java_websocket.WebSocket;

import net.sf.json.JSONObject;


public class OnlinePushCommand {
	public static boolean pushMessage(String did, String command, String data)
	{
		WebSocket dev = OnlineDeviceServerPool.getWebSocketByUser(did);
		if(dev != null){
			JSONObject requestCmd = new JSONObject();
			JSONObject resp = new JSONObject();

			resp.element("msg_version", "v1");
			resp.element("command", command);
			resp.element("data", data);
			requestCmd.element("action", "notifyAnnounceMsg");
			requestCmd.element("resp_event", 20);//10是请求返回，20是服务器主动推送
			requestCmd.element("seq_id", String.valueOf(System.currentTimeMillis()));
			requestCmd.element("resp", resp);
			OnlineDeviceServerPool.sendMessageToUser(dev, requestCmd.toString());
			return true;
		}

		return false;
	}



	public static boolean pushMessageToApp(String did, String action, JSONObject data)
	{
		WebSocket dev = OnlineDeviceServerPool.getWebSocketByUser(did);
		if(dev != null){
			JSONObject requestCmd = new JSONObject();
			requestCmd.element("action", action);
			requestCmd.element("resp_event", 20);//10是请求返回，20是服务器主动推送
			requestCmd.element("seq_id", String.valueOf(System.currentTimeMillis()));
			requestCmd.element("resp", data);

			OnlineDeviceServerPool.sendMessageToUser(dev, requestCmd.toString());
			return true;
		}

		return false;
	}
}
