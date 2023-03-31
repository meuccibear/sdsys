package com.adsys.plugin.websocketOnline;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import com.alibaba.fastjson.JSONArray;
import com.adsys.dao.redis.RedisDao;
import com.adsys.common.enums.logTypeEnum;
import com.adsys.dao.redis.impl.RedisDaoImpl;
import com.adsys.service.adEditor.program.impl.AdProgramService;
import com.adsys.service.system.customer.CustomerManager;
import com.adsys.service.system.device.ext.impl.DeviceExtService;
import com.adsys.service.system.device.impl.DeviceService;
import com.adsys.service.system.device.timepref.impl.DeviceTimePrefService;
import com.adsys.service.system.log.impl.LogService;
import com.adsys.service.system.schedule.impl.ScheduleService;
import com.adsys.util.AuthorityUtil;
import com.adsys.util.Const;
import com.adsys.util.Constants;
import com.adsys.util.DateUtil;
import com.adsys.util.Logger;
import com.adsys.util.MD5;
import com.adsys.util.PageData;
import com.adsys.util.PlayingMonCache;
import com.adsys.util.UuidUtil;


public class OnlineDeviceServer extends WebSocketServer {

    @Resource(name = "deviceService")
    private DeviceService deviceservice;

    @Resource(name = "deviceExtService")
    private DeviceExtService deviceextservice;

    @Resource(name = "deviceTimePrefService")
    private DeviceTimePrefService devicetimeprefservice;

    @Resource(name = "scheduleService")
    private ScheduleService scheduleService;

    @Resource(name = "adProgramService")
    private AdProgramService adProgramservice;

    @Resource(name = "logService")
    private LogService logservice;

    @Resource(name = "customerService")
    private CustomerManager customerService;

    @Resource(name = "redisDaoImpl")
    private RedisDao redisDaoImpl;

    protected Logger logger = Logger.getLogger(this.getClass());

    private static String serverAddr = "http://127.0.0.1:8080/ADSYS/";

    public OnlineDeviceServer(String addr, int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
        serverAddr = addr;
    }

    public OnlineDeviceServer(InetSocketAddress address) {
        super(address);
    }

    public static String getServerAddress() {
        return serverAddr;
    }

    /**
     * 触发连接事件
     */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        //this.sendToAll( "new connection: " + handshake.getResourceDescriptor() );
        System.out.println("===" + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    /**
     * 触发关闭事件
     */
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        userLeave(conn);
    }

    /**
     * 客户端发送消息到服务器时触发事件
     */
    @Override
    public void onMessage(WebSocket conn, String message) {
        PageData dev = null;
        Map<String, String> dev_redis = null;
        deviceservice = (DeviceService) ContextLoader.getCurrentWebApplicationContext().getBean("deviceService");
        deviceextservice = (DeviceExtService) ContextLoader.getCurrentWebApplicationContext().getBean("deviceExtService");
        scheduleService = (ScheduleService) ContextLoader.getCurrentWebApplicationContext().getBean("scheduleService");
        adProgramservice = (AdProgramService) ContextLoader.getCurrentWebApplicationContext().getBean("adProgramService");
        logservice = (LogService) ContextLoader.getCurrentWebApplicationContext().getBean("logService");
        customerService = (CustomerManager) ContextLoader.getCurrentWebApplicationContext().getBean("customerService");
        redisDaoImpl = (RedisDao) ContextLoader.getCurrentWebApplicationContext().getBean("redisDaoImpl");
        devicetimeprefservice = (DeviceTimePrefService) ContextLoader.getCurrentWebApplicationContext().getBean("deviceTimePrefService");

        message = message.toString();
        logger.info("req msg=" + message);
        Map cmdMap = (Map) JSONArray.parse(message);
        String action = cmdMap.get("action").toString();
        String did = cmdMap.get("did").toString();
        int rep_event = (int) cmdMap.get("req_event");
        Map reqMap = (Map) cmdMap.get("req");

        if (did != null && did != "") {
            onlineMaganger(1, did, conn);//加进连接池

            dev_redis = redisDaoImpl.getMap(did);
            logger.debug("dev_redis:" + JSON.toJSONString(dev_redis));
            if (dev_redis.size() == 0 || redisDaoImpl.checkTTL(did) <= 0) {
                PageData parma = new PageData();
                parma.put("did", did);
                try {
                    dev = deviceservice.findById(parma);
                    if (dev == null ) {
                        if(!"login".equals(action)){
                            resultResp(conn, action, rep_event, cmdMap.get("seq_id"), -1, "auth", null);
                            return;
                        }

                        parma.put("uuid", "849216c6abe94ff8951962313b334f4b");
                        parma.put("dtoken", "");
                        parma.put("gid", -1);

                        parma.put("dname", reqMap.get("dname"));
                        parma.put("daddr", reqMap.get("daddr"));
                        parma.put("deploydate", DateUtil.getTime());
                        parma.put("operdate", DateUtil.getTime());
                        deviceservice.save(parma);
                        dev_redis.put("did", parma.getString("did"));
                        dev_redis.put("uuid", parma.getString("uuid"));
                        dev_redis.put("dtoken", parma.getString("dtoken"));
                        dev_redis.put("dname", parma.getString("dname"));
                    } else {
                        dev_redis.put("did", dev.getString("did"));
                        dev_redis.put("uuid", dev.getString("uuid"));
                        dev_redis.put("dtoken", dev.getString("dtoken"));
                        dev_redis.put("dname", dev.getString("dname"));
                    }
                    redisDaoImpl.addMap(did, dev_redis);
                    redisDaoImpl.setExpire(did, 1800);//30min timeout
                } catch (Exception e) {
                    System.out.println(e);
                    resultResp(conn, action, rep_event, cmdMap.get("seq_id"), -1, "auth", null);
                    return;
                }
            }
        }

        if ("login".equals(action)) {
//            PageData cs_p = new PageData();
//            cs_p.put("uuid", dev_redis.get("uuid"));
//            PageData cs;
//            try {
//                cs = customerService.findById(cs_p);
//                if (cs != null) {
//                    if (cs.getString("expriedate") != null && cs.getString("expriedate") != "") {
//                        Date exprie = DateUtil.fomatDateTime(cs.getString("expriedate"));
//                        if (System.currentTimeMillis() > exprie.getTime()) {
//                            resultResp(conn, action, rep_event, cmdMap.get("seq_id"), -1, "fail", null);
//                            return;
//                        }
//                    }
//                }
//            } catch (Exception e1) {
//                e1.printStackTrace();
//            }

            PageData parma = new PageData();
            parma.put("did", did);
            parma.put("dstatus", "online");
            try {
                deviceservice.updateState(parma);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Map rep_data = new HashMap();
            if(dev_redis.containsKey("dtoken")){
                rep_data.put("token", dev_redis.get("dtoken"));
            }
            resultResp(conn, action, rep_event, cmdMap.get("seq_id"), 0, "ok", rep_data);
        } else if ("heartbeat".equals(action)) {
            PageData parma = new PageData();
            parma.put("did", did);
//            String token = "";
//            boolean islive = false;
//            if (reqMap != null) {
//                parma.put("did", did);
//                token = reqMap.get("token").toString();
//                islive = (boolean) reqMap.get("live");
//            }
            try {
//                if (dev_redis.get("dtoken").equals(token)) {
                    parma.put("dstatus", "online");
                    deviceservice.updateState(parma);

//                    if (islive) {
//                        parma.put("dlive", "start");
//                    } else {
//                        parma.put("dlive", "stop");
//                    }
                    deviceservice.updateBeat(parma);
                    resultResp(conn, action, rep_event, cmdMap.get("seq_id"), 0, "ok", null);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("register".equals(action)) {
            PageData parma = new PageData();
            String token = "";
            if (reqMap != null) {
                parma.put("did", did);
                token = reqMap.get("token").toString();
                parma.put("uuid", dev_redis.get("uuid"));
                parma.put("ssid", reqMap.get("ssid").toString());
                parma.put("mac", reqMap.get("mac").toString());
                parma.put("ipaddr", reqMap.get("ipaddr").toString());
                parma.put("ipdns", reqMap.get("ipdns").toString());
                parma.put("nettype", reqMap.get("iptype").toString());
                parma.put("gateway", reqMap.get("gateway").toString());
                parma.put("apn", reqMap.get("apn").toString());
                parma.put("space", reqMap.get("space"));
                parma.put("volume", reqMap.get("volume"));
                parma.put("disptype", reqMap.get("disptype"));
                parma.put("width", reqMap.get("screenwidth"));
                parma.put("height", reqMap.get("screenHeight"));
                parma.put("brightness", reqMap.get("brightness"));
                parma.put("sysvision", reqMap.get("sysver").toString());
            }
            try {
                if (dev_redis.get("dtoken").equals(token)) {
                    boolean isnew = false;
                    try {
                        PageData devext = deviceextservice.findById(parma);
                        if (devext != null) {
                            deviceextservice.edit(parma);
                        } else
                            isnew = true;
                    } catch (Exception e) {
                        isnew = true;
                    }
                    if (isnew) {
                        try {
                            deviceextservice.save(parma);
                            logger.info("save");
                        } catch (Exception e) {
                            resultResp(conn, action, rep_event, cmdMap.get("seq_id"), -1, "fail", null);
                            return;
                        }
                    }
                    /////////////////////////////LOG///////////
                    logservice.save(logTypeEnum.INFO.toString(), did + "设备注册到服务器", did, dev_redis.get("uuid"), did);
                    ///////////////////////////////////////////
                    resultResp(conn, action, rep_event, cmdMap.get("seq_id"), 0, "ok", null);
                } else
                    resultResp(conn, action, rep_event, cmdMap.get("seq_id"), -1, "fail", null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("gettasklist".equals(action)) {
            try {
                String uuid = "";
                JSONArray resjl = new JSONArray();
                PageData parma = new PageData();
                parma.put("did", did);
                parma.put("state", "push");
                List<PageData> taskl = scheduleService.pushItemList(parma);
                if (taskl != null) {
                    for (int i = 0; i < taskl.size(); i++) {
                        PageData task = taskl.get(i);
                        JSONObject res_j = new JSONObject();
                        uuid = task.getString("uuid");
                        res_j.element("did", did);
                        res_j.element("sid", task.getString("sid"));
                        res_j.element("resurl", OnlineDeviceServer.getServerAddress() + "adEditor/resources/file/" + task.getString("resid"));
                        res_j.element("respath", task.getString("resname"));
                        res_j.element("playtype", 1);
                        res_j.element("type", "resourse");
                        resjl.add(res_j);
                    }

                    if (resjl.size() > 0) {
                        OnlinePushCommand.pushMessage(did, "pushResList", resjl.toString());
                        /////////////////////////////LOG///////////
                        logservice.save(logTypeEnum.INFO.toString(), "设备请求下载列表", did, uuid, did);
                        ///////////////////////////////////////////
                    }
                }
                resultResp(conn, action, rep_event, cmdMap.get("seq_id"), 0, "ok", null);
            } catch (Exception e) {
                e.printStackTrace();
                resultResp(conn, action, rep_event, cmdMap.get("seq_id"), -1, "fail", null);
            }
        } else if ("filefeedback".equals(action)) {
            PageData parma = new PageData();
            if (reqMap != null) {
                parma.put("did", did);
                parma.put("sid", reqMap.get("sid").toString());
                parma.put("resid", reqMap.get("resid").toString());
                parma.put("state", reqMap.get("result").toString());
                String type_f = reqMap.get("type").toString();
                try {
                    if ("resourse".equals(type_f)) {
                        scheduleService.editPushState(parma);
                        if (scheduleService.pushItemCount(parma) <= 0) {
                            PageData shec = scheduleService.findById(parma);
                            if (shec != null) {
                                PageData scheitem = scheduleService.findItemById(parma);
                                JSONArray resjl = new JSONArray();
                                JSONObject res_j = new JSONObject();
                                res_j.element("did", parma.getString("did"));
                                res_j.element("sid", parma.getString("sid"));
                                res_j.element("resurl", serverAddr + "adEditor/programs/file/" + shec.getString("pid"));
                                res_j.element("respath", shec.getString("pid") + ".zip");
                                res_j.element("playtype", (int) shec.get("ptype"));
                                res_j.element("seqid", (long) scheitem.get("seqid"));
                                res_j.element("type", "program");
                                if ((int) shec.get("ptype") == 2) {
                                    String[] setime = shec.getString("schedule").split(";");
                                    if (setime.length == 2) {
                                        res_j.element("starttime", setime[0]);
                                        res_j.element("endtime", setime[1]);
                                    }
                                }
                                resjl.add(res_j);
                                if (resjl.size() > 0) {
                                    OnlinePushCommand.pushMessage(did, "pushResList", resjl.toString());
                                }
                            }
                        }
                    } else if ("file".equals(type_f)) {
                        parma.put("resid", MD5.md5(reqMap.get("resid").toString()));
                        scheduleService.editPushState(parma);
                        parma.put("state", reqMap.get("result").toString());
                        scheduleService.editScheduleListState(parma);
                        //////////////////////////LOG///////////
                        logservice.save(logTypeEnum.INFO.toString(), "成功发布文件:" + parma.getString("resid") + "到设备(" + did + ")", did, dev_redis.get("uuid"), did);
                        ///////////////////////////////////////////
                    } else if ("program".equals(type_f)) {
                        if ("finished".equals(reqMap.get("result").toString())) {
                            try {
                                PageData olderp = new PageData();
                                olderp.put("did", did);
                                List<PageData> olderl = scheduleService.listAllForOlder(olderp);
                                if (olderl != null && olderl.size() > 0) {
                                    for (int p = 0; p < olderl.size(); p++) {
                                        PageData older = olderl.get(p);
                                        scheduleService.deletePushItem(older);
                                        scheduleService.deleteItem(older);
                                    }
                                }

                                olderp = new PageData();
                                olderp.put("did", did);
                                olderp.put("stype", "program");
                                olderp.put("pid", reqMap.get("resid").toString());
                                olderp.put("seqid", new BigDecimal(reqMap.get("seqid").toString()).longValue());
                                olderl = scheduleService.listByDid(olderp);
                                if (olderl != null && olderl.size() > 0) {
                                    for (int p = 0; p < olderl.size(); p++) {
                                        PageData older = olderl.get(p);
                                        scheduleService.deletePushItem(older);
                                        scheduleService.deleteItem(older);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            parma.put("state", "pushed");
                        }
                        scheduleService.editScheduleListState(parma);
                        //////////////////////////LOG///////////
                        logservice.save(logTypeEnum.INFO.toString(), "成功发布节目单:" + parma.getString("sid") + "到设备(" + did + ")", did, dev_redis.get("uuid"), did);
                        ///////////////////////////////////////////
                    }
                    resultResp(conn, action, rep_event, cmdMap.get("seq_id"), 0, "ok", null);
                } catch (Exception e) {
                    logger.info("--hermen--" + e.getMessage());
                    resultResp(conn, action, rep_event, cmdMap.get("seq_id"), -1, "fail", null);
                }
            } else
                resultResp(conn, action, rep_event, cmdMap.get("seq_id"), -1, "fail", null);
        } else if ("playfeedback".equals(action)) {
            PageData parma = new PageData();
            if (reqMap != null) {
                parma.put("id", reqMap.get("pid").toString());
                try {
                    PageData program = adProgramservice.findById(parma);
                    if (program != null) {
                        program.put("updatedate", DateUtil.getTime());
                        PlayingMonCache.setDevPlayMon(did, program);

                        PageData ctP = new PageData();
                        ctP.put("uuid", dev_redis.get("uuid"));
                        ctP.put("did", did);
                        ctP.put("pid", reqMap.get("pid").toString());
                        PageData couter = adProgramservice.findCouterById(ctP);
                        if (couter != null) {
                            adProgramservice.updateCouter(ctP);
                        } else
                            adProgramservice.saveCouter(ctP);
                    } else
                        PlayingMonCache.setDevPlayMon(did, null);

                    if (reqMap.get("plist") != null) {
                        String[] plist = reqMap.get("plist").toString().split(";");
                        parma.clear();
                        parma.put("did", did);
                        parma.put("stype", "program");
                        List<PageData> plistByDid = scheduleService.listByDid(parma);
                        for (int p = 0; p < plistByDid.size(); p++) {
                            int found = 0;
                            PageData pp = plistByDid.get(p);
                            for (int i = 0; i < plist.length; i++) {
                                if (plist[i].equals(pp.getString("pid"))) {
                                    found = 1;
                                    break;
                                }
                            }

                            if (found == 0 && "pushed".equals(pp.getString("state"))) {
                                parma.clear();
                                parma.put("did", did);
                                parma.put("sid", pp.getString("sid"));
                                parma.put("state", "deleted");
                                scheduleService.editScheduleListState(parma);
                            }
                        }
                    }
                    resultResp(conn, action, rep_event, cmdMap.get("seq_id"), 0, "ok", null);
                } catch (Exception e) {
                    e.printStackTrace();
                    resultResp(conn, action, rep_event, cmdMap.get("seq_id"), -1, "fail", null);
                }
            }
        } else if ("setlocation".equals(action)) {

            try {
                PageData parma = new PageData();
                if (reqMap != null) {
                    parma.put("did", did);
                    parma.put("lng", reqMap.get("lng").toString());
                    parma.put("lat", reqMap.get("lat").toString());
                    deviceservice.updateLocation(parma);
                }
                resultResp(conn, action, rep_event, cmdMap.get("seq_id"), 0, "ok", null);
            } catch (Exception e) {
                e.printStackTrace();
                resultResp(conn, action, rep_event, cmdMap.get("seq_id"), -1, "fail", null);
            }
        } else if ("volumefeedback".equals(action)) {

            try {
                PageData parma = new PageData();
                parma.put("did", did);
                PageData devext = deviceextservice.findById(parma);
                if (devext != null && reqMap != null) {
                    devext.put("volume", reqMap.get("volume"));
                    deviceextservice.edit(devext);
                }
                resultResp(conn, action, rep_event, cmdMap.get("seq_id"), 0, "ok", null);
            } catch (Exception e) {
                e.printStackTrace();
                resultResp(conn, action, rep_event, cmdMap.get("seq_id"), -1, "fail", null);
            }
        } else if ("brightnessfeedback".equals(action)) {

            try {
                PageData parma = new PageData();
                parma.put("did", did);
                PageData devext = deviceextservice.findById(parma);
                if (devext != null && reqMap != null) {
                    devext.put("brightness", reqMap.get("brightness"));
                    deviceextservice.edit(devext);
                }
                resultResp(conn, action, rep_event, cmdMap.get("seq_id"), 0, "ok", null);
            } catch (Exception e) {
                e.printStackTrace();
                resultResp(conn, action, rep_event, cmdMap.get("seq_id"), -1, "fail", null);
            }
        } else if ("timepreffeedback".equals(action)) {

            try {
                PageData parma = new PageData();
                int week = (int) reqMap.get("week");
                parma.put("uuid", dev_redis.get("uuid"));
                parma.put("did", did);
                parma.put("week", week);
                long weeknum = devicetimeprefservice.getWeekCount(parma);
                if (weeknum < 5) {
                    if (weeknum == 0 && week == 0) {
                        parma.put("week", "");
                        devicetimeprefservice.deleteByDid(parma);
                        logger.info("timepref-delall");
                    } else if (week > 0) {
                        parma.put("week", 0);
                        weeknum = devicetimeprefservice.getWeekCount(parma);
                        if (weeknum > 0) {
                            devicetimeprefservice.deleteByDid(parma);
                            logger.info("timepref-del000");
                        }
                    }
                    parma.put("week", week);
                    parma.put("id", reqMap.get("id").toString());
                    parma.put("timestart", reqMap.get("timestart").toString());
                    parma.put("timeend", reqMap.get("timeend").toString());
                    devicetimeprefservice.save(parma);
                    logger.info("timepref-save:" + week + ":" + reqMap.get("id").toString());
                    resultResp(conn, action, rep_event, cmdMap.get("seq_id"), 0, "ok", null);
                }
                resultResp(conn, action, rep_event, cmdMap.get("seq_id"), -1, "fail", null);
            } catch (Exception e) {
                e.printStackTrace();
                resultResp(conn, action, rep_event, cmdMap.get("seq_id"), -1, "fail", null);
            }
        } else if ("deltimepreffeedback".equals(action)) {

            try {
                PageData parma = new PageData();
                parma.put("uuid", dev_redis.get("uuid"));
                parma.put("did", did);
                parma.put("id", reqMap.get("id").toString());
                devicetimeprefservice.delete(parma);
                resultResp(conn, action, rep_event, cmdMap.get("seq_id"), -1, "fail", null);
            } catch (Exception e) {
                e.printStackTrace();
                resultResp(conn, action, rep_event, cmdMap.get("seq_id"), -1, "fail", null);
            }
        }
    }

    public void onFragment(WebSocket conn, Framedata fragment) {
    }

    /**
     * 触发异常事件
     */
    @Override
    public void onError(WebSocket conn, Exception ex) {
        //ex.printStackTrace();
        if (conn != null) {
            //some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    /**
     * 用户加入处理
     *
     * @param user
     */
    public void userjoin(String user, WebSocket conn) {
        onlineMaganger(1, user, conn);
    }

    /**
     * 返回结果封装
     *
     * @param conn
     */
    public void resultResp(WebSocket conn, String action, int rep_evnet, Object seq_id, Integer code, String msg, Map data) {
        JSONObject result = new JSONObject();
        JSONObject resp = new JSONObject();
        JSONObject dataj = new JSONObject();
        if (data != null) {
            Iterator iter = data.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                dataj.element(entry.getKey().toString(), entry.getValue());
            }
            resp.element("data", dataj);
        }
        resp.element("code", code);
        resp.element("msg", msg);

        result.element("action", action);
        result.element("resp_event", 10);//10是请求返回，20是服务器主动推送
        result.element("seq_id", seq_id);
        result.element("resp", resp);
        System.out.println("result=" + result.toString());
        OnlineDeviceServerPool.sendMessageToUser(conn, result.toString());
    }

    /**
     * 强制某用户下线
     *
     * @param user
     */
    public void goOut(String user) {
        this.goOut(OnlineDeviceServerPool.getWebSocketByUser(user), "thegoout");
    }

    /**
     * 强制用户下线
     *
     * @param conn
     */
    public void goOut(WebSocket conn, String type) {
        JSONObject result = new JSONObject();
        result.element("type", type);
        result.element("msg", "goOut");
        OnlineDeviceServerPool.sendMessageToUser(conn, result.toString());
    }

    /**
     * 用户下线处理
     *
     * @param user
     */
    public void userLeave(WebSocket conn) {
        onlineMaganger(2, null, conn);
    }

    /**
     * 获取在线总数
     *
     * @param user
     */
    public void getUserCount(WebSocket conn) {
        JSONObject result = new JSONObject();
        result.element("type", "count");
        result.element("msg", OnlineDeviceServerPool.getUserCount());
        OnlineDeviceServerPool.sendMessageToUser(conn, result.toString());
    }

    /**
     * 获取在线用户列表
     *
     * @param user
     */
    public void getUserList() {
        WebSocket conn = OnlineDeviceServerPool.getFhadmin();
        if (null == conn) {
            return;
        }
        JSONObject result = new JSONObject();
        result.element("type", "userlist");
        result.element("list", OnlineDeviceServerPool.getOnlineUser());
        OnlineDeviceServerPool.sendMessageToUser(conn, result.toString());
    }

    /**
     * 用户上下线管理
     *
     * @param type 1：上线；2：下线
     * @param user
     * @param conn
     */
    public synchronized void onlineMaganger(int type, String user, WebSocket conn) {
        if (type == 1) {
            WebSocket conn_old = OnlineDeviceServerPool.getWebSocketByUser(user);
            if (null == conn_old) {        //判断用户是否在其它终端登录
                OnlineDeviceServerPool.addUser(user, conn);                    //向连接池添加当前的连接对象
            } else {
                if (!conn_old.equals(conn)) {
                    System.out.println("onlineMaganger del old and add new");
                    OnlineDeviceServerPool.removeUser(conn_old);    //将旧的信息删除
                    OnlineDeviceServerPool.addUser(user, conn);
                }
            }
        } else {
            OnlineDeviceServerPool.removeUser(conn);                            //在连接池中移除连接
            this.getUserList();
        }
    }


    public static void main(String[] args) throws InterruptedException, IOException {
		/*WebSocketImpl.DEBUG = false;
		int port = 8887; //端口
		OnlineDeviceServer s = new OnlineDeviceServer(port);
		s.start();
		//System.out.println( "服务器的端口" + s.getPort() );*/
        String url = "http://23.23.3.3:8080/sdfsd/adsfdsf/vfvdsdsadsvd";
        System.out.println(url.substring(url.lastIndexOf("/") + 1));
    }
}

