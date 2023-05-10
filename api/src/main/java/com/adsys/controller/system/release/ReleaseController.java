package com.adsys.controller.system.release;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.adsys.dao.redis.RedisDao;
import com.adsys.service.system.account.AccountService;
import com.adsys.service.system.app.AppSendMessageManager;
import com.alibaba.fastjson.TypeReference;
import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.adsys.common.enums.logTypeEnum;
import com.adsys.common.enums.permissionEnum;
import com.adsys.controller.base.BaseController;
import com.adsys.entity.Page;
import com.adsys.plugin.websocketOnline.OnlineDeviceServer;
import com.adsys.plugin.websocketOnline.OnlineDeviceServerPool;
import com.adsys.plugin.websocketOnline.OnlinePushCommand;
import com.adsys.service.adEditor.program.impl.AdProgramItemService;
import com.adsys.service.adEditor.program.impl.AdProgramService;
import com.adsys.service.adEditor.resource.impl.AdResourceService;
import com.adsys.service.files.FilesManager;
import com.adsys.service.system.device.impl.DeviceService;
import com.adsys.service.system.group.GroupManager;
import com.adsys.service.system.log.impl.LogService;
import com.adsys.service.system.schedule.impl.ScheduleService;
import com.adsys.util.AuthorityUtil;
import com.adsys.util.Constants;
import com.adsys.util.DateUtil;
import com.adsys.util.MD5;
import com.adsys.util.PageData;
import com.adsys.util.PlayingMonCache;
import com.adsys.util.json.JsonResponse;
import com.alibaba.fastjson.JSON;

import net.sf.json.JSONObject;


@Controller
@RequestMapping(value = "/release")
public class ReleaseController extends BaseController {

    @Resource(name = "groupService")
    private GroupManager groupsservice;

    @Resource(name = "deviceService")
    private DeviceService deviceservice;

    @Resource(name = "scheduleService")
    private ScheduleService scheduleService;

    @Resource(name = "adResourceService")
    private AdResourceService adResourceService;

    @Resource(name = "adProgramService")
    private AdProgramService adProgramService;

    @Resource(name = "adProgramItemService")
    private AdProgramItemService adProgramItemService;

    @Resource(name = "filesService")
    private FilesManager filesService;

    @Resource(name = "logService")
    private LogService logservice;

    @Resource(name = "appSendMessageService")
    private AppSendMessageManager appSendMessageManager;

    @Resource
    private AccountService accountService;

    @Resource(name = "redisDaoImpl")
    private RedisDao redisDaoImpl;

    /**
     * 新增接口
     *
     * @return
     */
    @RequestMapping(value = "/program", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse releaseProgram() {
        try {
            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_PUSH);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            PageData pd = this.getPageData();
            if (null != pd && pd.size() > 0) {
                logger.info("pd:" + JSON.toJSONString(pd));

                String uuid = AuthorityUtil.getRequesterUUID(getRequest());


                List<String> devlist = new ArrayList<String>();
                if (pd.get("gid") == null || "".equals(pd.getString("gid"))) {
                    pd.remove("gid");
                    String[] dvl = pd.getString("did").split(",");
                    for (int i = 0; i < dvl.length; i++) {
                        devlist.add(dvl[i]);
                    }
                } else {
                    logger.info("gid:");
                    PageData gp = new PageData();
                    gp.put("uuid", uuid);
                    gp.put("gpid", pd.getString("gid"));
                    List<PageData> gl = null;
                    try {
                        gl = groupsservice.listAll(gp);
                    } catch (Exception ex) {
                        gl = new ArrayList<PageData>();
                    }
                    gp.put("gid", Integer.valueOf(pd.getString("gid")));
                    //gl.add(gp);
                    for (int g = 0; g < gl.size(); g++) {
                        PageData g_i = gl.get(g);
                        gp.clear();
                        gp.put("uuid", uuid);
                        gp.put("gid", (int) g_i.get("gid"));

                        List<PageData> dl = null;
                        try {
                            dl = deviceservice.listAll(gp);
                        } catch (Exception ex) {
                            dl = new ArrayList<PageData>();
                        }
                        for (int d = 0; d < dl.size(); d++) {
                            PageData d_i = dl.get(d);
                            devlist.add(d_i.getString("did"));
                        }
                    }
                }


                List<String> failDids = new ArrayList<>();

                if (devlist != null && devlist.size() > 0) {


                    String app = pd.getString("app");
                    String action = pd.getString("action");

                    JSONObject dataP = new JSONObject();
                    dataP.element("app", app);
                    List<PageData> accounts = null;

                    boolean samebatch = true;

                    switch (action) {
                        case "gotolive":
                            dataP.element("roomid", pd.getString("roomid"));
                            break;
                        case "pointclick":
                            dataP.element("xp", pd.getString("xp"));
                            dataP.element("yp", pd.getString("yp"));
                            break;
                        case "automode":
                            dataP.element("mode", pd.getString("mode"));
                            break;
                        case "continuelike":
                            dataP.element("count", pd.getString("count"));
                            break;
                        case "tklogin":
                            samebatch = false;
                            PageData getAccountsPD = new PageData();
//                            getAccountsPD.put("rowbounds", devlist.size() * 3);
                            getAccountsPD.put("uuid", uuid);
                            getAccountsPD.put("accounttype", app);
                            accounts = accountService.getAccounts(getAccountsPD);
                            break;
                        case "clipboard":
                            dataP.element("text", pd.getString("text"));
                            break;
                        case "install":
                            dataP.element("url", pd.getString("url"));
                            break;
                        case "vpn":
                            dataP.element("cmd", pd.getString("cmd"));
                            Integer lid = 0;
                            if(null != pd.getString("lid")){
                                lid = Integer.valueOf(pd.getString("lid"));
                            }
                            dataP.element("lid", lid);
                            break;
                        case "languages":
                            String lang = pd.getString("lang");
                            if(lang.equals("Other")){
                                lang = pd.getString("OtherLanguageName");
                            }
                            dataP.element("lang", lang);
                            break;
                        case "timezone":
                            dataP.element("region", pd.getString("region"));
                            break;
                        case "poweroff":
                            dataP.element("cmd", pd.getString("cmd"));
                            dataP.element("boot", pd.getString("boot"));
                            dataP.element("down", pd.getString("down"));
                            break;
                    }

                    if (samebatch) {
                        failDids = sendMessages(action, devlist, dataP);
                    } else {
                        String did = null;

                        PageData appSendMessageData = new PageData();
                        appSendMessageData.put("task_id", this.get32UUID());

                        int accountShiftingIndex = 0;
                        int accountIndex = 0;
                        String key = null;
                        for (int ds = 0; ds < devlist.size(); ds++) {
                            did = devlist.get(ds);
                            if ("tklogin".equals(action)) {
                                accountIndex = ds + accountShiftingIndex;
                                // 1. 发送账号 redis限制
                                // 2.
                                if (accountIndex < accounts.size()) {
                                    key = String.format("%s%s_%s", Constants.REDIS_USE_ACCOUNT, app, accounts.get(accountIndex).get("account"));
                                    String isUseAccount = redisDaoImpl.get(key);
                                    logger.info("redis:" + isUseAccount);
                                    if (null == isUseAccount) {
                                        logger.info("accounts:" + JSON.toJSONString(accounts));
                                        dataP.element("username", accounts.get(accountIndex).get("account"));
                                        dataP.element("password", accounts.get(accountIndex).get("password"));
                                        if (sendMessage(appSendMessageData, action, did, dataP, true)) {
                                            redisDaoImpl.addString(key, did);
                                            // 15分钟 900
                                            redisDaoImpl.setExpire(key, 60 * 15);
                                        }
                                        dataP.remove("account");
                                        dataP.remove("password");
                                    } else {
//                                        logger.info("chongfa");
                                        ++accountShiftingIndex;
                                        --ds;
                                    }
                                } else {
                                    logger.info(String.format("indexchaole:%s", did));
                                    sendMessage(appSendMessageData, action, did, dataP, false);
                                    failDids.add(did);
                                }
                            } else {
                                sendMessage(appSendMessageData, action, did, dataP, true);
                                failDids.add(did);
                            }

                        }

                    }
                }

                PageData rst = new PageData();
                logger.info("failDids:"+JSON.toJSONString(failDids));
                List<String> deviceNameByGid = deviceservice.findDeviceNameByDid(failDids);
                if(null != deviceNameByGid){
                    rst.put("failDeviceNames", deviceNameByGid);
                }
                return ajaxSuccess(rst, Constants.REQUEST_01, Constants.REQUEST_OK);
            } else {
                return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
//            logger.info("add schedule error !!!");
//            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    boolean sendMessage(PageData appSendMessageData, String action, String did, JSONObject dataP, boolean sendB) throws Exception {
        appSendMessageData.put("action", action);
        appSendMessageData.put("sid", this.get32UUID());
        appSendMessageData.put("dids", did);
        boolean status = false;
        appSendMessageData.put("para", dataP.toString());
        if (sendB) {
            status = OnlinePushCommand.pushMessageToApp(did, action, dataP);
        }
        if (!status) {
            appSendMessageData.put("fail_did", did);
        }
        appSendMessageData.put("addDate", DateUtil.getTime());
        appSendMessageManager.save(appSendMessageData);
        return status;
    }

    List<String> sendMessages(String action, List<String> devlist, JSONObject dataP) throws Exception {
        List<String> failDidList = new ArrayList<>();
        String did = "";

        for (int ds = 0; ds < devlist.size(); ds++) {
            did = devlist.get(ds);
            if (!OnlinePushCommand.pushMessageToApp(did, action, dataP)) {
                failDidList.add(did);
            }
        }

        PageData appSendMessageData = new PageData();
        appSendMessageData.put("sid", this.get32UUID());
        appSendMessageData.put("task_id", this.get32UUID());
        appSendMessageData.put("action", action);
        appSendMessageData.put("para", dataP.toString());

        String[] dids = com.alibaba.fastjson.JSONObject.parseObject(JSON.toJSONString(devlist), new TypeReference<String[]>() {
        });
        String[] failDids = com.alibaba.fastjson.JSONObject.parseObject(JSON.toJSONString(failDidList), new TypeReference<String[]>() {
        });
        appSendMessageData.put("dids", String.join(",", dids));
        appSendMessageData.put("fail_did", failDids.length > 0 ? String.join(",", failDids) : null);
        appSendMessageData.put("addDate", DateUtil.getTime());
        appSendMessageManager.save(appSendMessageData);
        return failDidList;
    }

    /**
     * 审核接口
     *
     * @return
     */
    @RequestMapping(value = "/program/review/{id}", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse reviewProgram(@PathVariable("id") String id) {
        try {
            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_PUSH);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            PageData pd = this.getPageData();
            if (null != pd && pd.size() > 0) {
                PageData schedule = new PageData();
                pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                pd.put("sid", id);
                pd.put("stype", "program");
                PageData sche = scheduleService.findById(pd);
                if (sche != null && sche.getString("sstate").equals("new")) {
                    PageData pamra = new PageData();
                    pamra.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                    pamra.put("pid", sche.getString("pid"));
                    List<PageData> program_l = adProgramItemService.listAllByPid(pamra);
                    List<PageData> sche_l = scheduleService.itemListAll(pd);
                    for (int s = 0; s < sche_l.size(); s++) {
                        PageData s_i = sche_l.get(s);
                        JSONArray resjl = new JSONArray();
                        for (int p = 0; p < program_l.size(); p++) {
                            PageData p_i = program_l.get(p);

                            String res = p_i.getString("res");
                            if (res != null && !"".equals(res)) {
                                String[] resarray = res.split(";");
                                String[] targetarray = p_i.getString("targetpath").split(";");
                                for (int r = 0; r < resarray.length; r++) {
                                    PageData push_i = new PageData();
                                    String respath = resarray[r].substring(resarray[r].indexOf("upload"));
                                    push_i.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                                    push_i.put("sid", s_i.getString("sid"));
                                    push_i.put("did", s_i.getString("did"));
                                    push_i.put("resname", targetarray[r]);
                                    push_i.put("resid", MD5.md5(respath));
                                    push_i.put("state", "push");
                                    push_i.put("updatedate", DateUtil.getTime());

                                    scheduleService.savePushItem(push_i);

                                    JSONObject res_j = new JSONObject();
                                    res_j.element("did", s_i.getString("did"));
                                    res_j.element("sid", s_i.getString("sid"));
                                    res_j.element("resurl", OnlineDeviceServer.getServerAddress() + "adEditor/resources/file/" + push_i.getString("resid"));
                                    res_j.element("respath", push_i.getString("resname"));
                                    res_j.element("playtype", 1);
                                    res_j.element("type", "resourse");
                                    resjl.put(res_j);
                                }
                            }
                        }
                        if (resjl.length() > 0) {
                            OnlinePushCommand.pushMessage(s_i.getString("did"), "pushResList", resjl.toString());
                        } else {
                            JSONArray respl = new JSONArray();
                            JSONObject res_p = new JSONObject();
                            res_p.element("did", s_i.getString("did"));
                            res_p.element("sid", s_i.getString("sid"));
                            res_p.element("resurl", OnlineDeviceServer.getServerAddress() + "adEditor/programs/file/" + sche.getString("pid"));
                            res_p.element("respath", sche.getString("pid") + ".zip");
                            res_p.element("playtype", (int) sche.get("ptype"));
                            res_p.element("seqid", (long) s_i.get("seqid"));
                            res_p.element("type", "program");
                            if ((int) sche.get("ptype") == 2) {
                                String[] setime = sche.getString("schedule").split(";");
                                if (setime.length == 2) {
                                    res_p.element("starttime", setime[0]);
                                    res_p.element("endtime", setime[1]);
                                }
                            }
                            respl.put(res_p);
                            OnlinePushCommand.pushMessage(s_i.getString("did"), "pushResList", respl.toString());
                        }
                        /////////////////////////////LOG///////////
                        logservice.save(logTypeEnum.INFO.toString(), "审核节目单并推送到设备", AuthorityUtil.getRequesterUserName(getRequest()), AuthorityUtil.getRequesterUUID(getRequest()), s_i.getString("did"));
                        ///////////////////////////////////////////
                    }
                    sche.put("sstate", "reviewed");
                    scheduleService.reView(sche);
                    return ajaxSuccess(Constants.REQUEST_01, Constants.REQUEST_OK);
                } else
                    return ajaxFailure(Constants.REQUEST_03, "节目单不存在或已审核");


            } else {
                return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
            }

        } catch (Exception ex) {
            logger.info("add schedule error !!!");
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * 发布接口
     *
     * @return
     */
    @RequestMapping(value = "/program/push/{id}", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse pushProgram(@PathVariable("id") String id) {
        try {
            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_PUSH);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            PageData pd = this.getPageData();
            if (null != pd && pd.size() > 0) {

                PageData schedule = new PageData();
                pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                pd.put("sid", id);
                pd.put("stype", "program");
                PageData sche = scheduleService.findById(pd);
                if (sche != null && sche.getString("sstate").equals("reviewed")) {
                    PageData pamra = new PageData();
                    pamra.put("pid", sche.getString("pid"));
                    pamra.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                    List<PageData> program_l = adProgramItemService.listAllByPid(pamra);
                    List<PageData> sche_l = scheduleService.itemListAll(pd);
                    for (int s = 0; s < sche_l.size(); s++) {
                        PageData s_i = sche_l.get(s);
                        JSONArray resjl = new JSONArray();
                        PageData del_p = new PageData();
                        del_p.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                        del_p.put("sid", s_i.getString("sid"));
                        del_p.put("did", s_i.getString("did"));
                        scheduleService.deletePushItem(del_p);//删除旧的项目清单

                        for (int p = 0; p < program_l.size(); p++) {
                            PageData p_i = program_l.get(p);

                            String res = p_i.getString("res");
                            if (res != null && !"".equals(res)) {
                                String[] resarray = res.split(";");
                                String[] targetarray = p_i.getString("targetpath").split(";");

                                for (int r = 0; r < resarray.length; r++) {
                                    PageData push_i = new PageData();
                                    String respath = resarray[r].substring(resarray[r].indexOf("upload"));
                                    push_i.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                                    push_i.put("sid", s_i.getString("sid"));
                                    push_i.put("did", s_i.getString("did"));
                                    push_i.put("resname", targetarray[r]);
                                    push_i.put("resid", MD5.md5(respath));
                                    push_i.put("state", "push");
                                    push_i.put("updatedate", DateUtil.getTime());

                                    scheduleService.savePushItem(push_i);

                                    JSONObject res_j = new JSONObject();
                                    res_j.element("did", s_i.getString("did"));
                                    res_j.element("sid", s_i.getString("sid"));
                                    res_j.element("resurl", OnlineDeviceServer.getServerAddress() + "adEditor/resources/file/" + push_i.getString("resid"));
                                    res_j.element("respath", push_i.getString("resname"));
                                    res_j.element("playtype", 1);
                                    res_j.element("type", "resourse");
                                    resjl.put(res_j);
                                }
                            }
                        }

                        PageData pdSs = new PageData();
                        pdSs.put("sid", s_i.getString("sid"));
                        pdSs.put("did", s_i.getString("did"));
                        pdSs.put("state", "pushing");
                        scheduleService.editScheduleListState(pdSs);//将节目单状态改为pushing

                        if (resjl.length() > 0) {
                            OnlinePushCommand.pushMessage(s_i.getString("did"), "pushResList", resjl.toString());

                        } else {
                            JSONArray respl = new JSONArray();
                            JSONObject res_p = new JSONObject();
                            res_p.element("did", s_i.getString("did"));
                            res_p.element("sid", s_i.getString("sid"));
                            res_p.element("resurl", OnlineDeviceServer.getServerAddress() + "adEditor/programs/file/" + sche.getString("pid"));
                            res_p.element("respath", sche.getString("pid") + ".zip");
                            res_p.element("playtype", (int) sche.get("ptype"));
                            res_p.element("type", "program");
                            respl.put(res_p);
                            OnlinePushCommand.pushMessage(s_i.getString("did"), "pushResList", respl.toString());

                        }
                        /////////////////////////////LOG///////////
                        logservice.save(logTypeEnum.INFO.toString(), "推送节目单到设备", AuthorityUtil.getRequesterUserName(getRequest()), AuthorityUtil.getRequesterUUID(getRequest()), s_i.getString("did"));
                        ///////////////////////////////////////////
                    }

                    return ajaxSuccess(Constants.REQUEST_01, Constants.REQUEST_OK);
                } else
                    return ajaxFailure(Constants.REQUEST_03, "节目单不存在或未审核");


            } else {
                return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
            }

        } catch (Exception ex) {
            logger.info("add schedule error !!!");
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }


    /**
     * 查询列表
     */
    @RequestMapping(value = "/programs", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse programList() {
        try {
            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_PUSH);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            PageData pd = this.getPageData();
            Page page = new Page();
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            page.setPd(pd);
            page.setOffset(pd.getString("offset") == null ? 0 : Integer.valueOf(pd.getString("offset")));
            List<PageData> result = deviceservice.list(page);
            for (int i = 0; i < result.size(); i++) {
                PageData pd_i = result.get(i);

                PageData extP = new PageData();//获取节目信息
                extP.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                extP.put("did", pd_i.getString("did"));
                extP.put("stype", "program");
                if (pd.get("sstate") != null && !pd.getString("sstate").equals(""))
                    extP.put("state", pd.getString("sstate"));
                List<PageData> plist = scheduleService.listByDid(extP);

                if (null != plist) {

                    PageData py = PlayingMonCache.getDevPlayMon(pd_i.getString("did"));

                    for (int d = 0; d < plist.size(); d++) {
                        PageData program = plist.get(d);

                        if (py != null) {
                            if (program.getString("pid").equals(py.getString("id"))) {
                                program.put("state", "playing");
                            }
                        }

                        PageData parma = new PageData();
                        parma.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                        parma.put("id", program.getString("pid"));
                        PageData p_i = adProgramService.findById(parma);
                        if (p_i != null) {
                            program.put("pname", p_i.getString("name"));
                        }

                        plist.set(d, program);
                    }
                    pd_i.put("plist", plist);
                } else
                    pd_i.put("plist", new ArrayList());

                //判断心跳是否超时，如果超时将状态设为离线
//                int mins = DateUtil.getDiffMin(DateUtil.formatSdfTimes((Timestamp) pd_i.get("beatdate")), DateUtil.getTime());
//
//                if (mins > 1 && "online".equals(pd_i.getString("dstatus"))) {
//                    pd_i.put("dstatus", "offline");
//                    deviceservice.updateState(pd_i);
//                }

                result.set(i, pd_i);
            }

            return ajaxSuccessPage("release", result, page, Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * 查询设备下面的播放列表
     */
    @RequestMapping(value = "/programs/dev/{id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse pListByDid(@PathVariable("id") String id) {
        try {

            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Device_READ);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);


            PageData pd = this.getPageData();
            pd.put("did", id);
            pd.put("stype", "program");
            List<PageData> result = scheduleService.listByDid(pd);
            return ajaxSuccess(result, Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * 查询指定设备，节目单的文件列表推送情况
     */
    @RequestMapping(value = "/programs/item", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse allList() {
        try {

            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_PUSH);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);


            PageData pd = this.getPageData();
            pd.put("did", pd.getString("did"));
            pd.put("sid", pd.getString("sid"));
            List<PageData> result = scheduleService.pushItemList(pd);
            for (int i = 0; i < result.size(); i++) {
                PageData d_i = result.get(i);
                PageData parma = new PageData();
                parma.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                parma.put("id", d_i.getString("resid"));
                PageData res_i = adResourceService.findById(parma);
                if (res_i != null) {
                    d_i.put("fname", res_i.getString("resName"));
                }
                result.set(i, d_i);
            }
            return ajaxSuccess(result, Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * 删除发布
     */
    @RequestMapping(value = "/program/{id}", method = RequestMethod.DELETE, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse deleteById(@PathVariable("id") String id) {
        try {

            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_PUSH);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            if (null == id) {
                return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
            }

            PageData pd = new PageData();
            pd.put("sid", id);
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));

            PageData sche = scheduleService.findById(pd);
            if (sche != null) {

                if ("new".equals(sche.getString("sstate"))) {
                    scheduleService.deleteItem(pd);
                    scheduleService.delete(pd);
                    return ajaxSuccess(Constants.REQUEST_01, Constants.REQUEST_OK);
                } else {
                    List<PageData> listitem = scheduleService.itemListAll(pd);
                    for (int i = 0; i < listitem.size(); i++) {
                        PageData item = listitem.get(i);

                        if (OnlineDeviceServerPool.getWebSocketByUser(item.getString("did")) != null) {
                            OnlinePushCommand.pushMessage(item.getString("did"), "delProgram", sche.getString("pid"));

                            pd.put("did", item.getString("did"));
                            scheduleService.deletePushItem(pd);
                            scheduleService.deleteItem(pd);
                        }
                    }

                    listitem.clear();
                    listitem = scheduleService.itemListAll(pd);
                    if (listitem == null || listitem.size() == 0) {
                        scheduleService.delete(pd);
                        return ajaxSuccess(Constants.REQUEST_01, Constants.REQUEST_OK);
                    } else
                        return ajaxSuccess(Constants.REQUEST_05, "有部分离线设备的节目未能删除");
                }
            } else
                return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);

        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * 删除发布子ITEM
     */
    @RequestMapping(value = "/program/item/{sid}/{did}", method = RequestMethod.DELETE, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse deleteItemBydId(@PathVariable("sid") String sid, @PathVariable("did") String did) {
        try {

            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_PUSH);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            if (null == sid || null == did) {
                return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
            }


            PageData pd = new PageData();
            pd.put("sid", sid);
            pd.put("did", did);
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            PageData scheitem = scheduleService.findItemById(pd);
            PageData sche = scheduleService.findById(pd);
            if (sche != null && scheitem != null) {

                if (OnlineDeviceServerPool.getWebSocketByUser(did) == null) {
                    if (!scheitem.getString("state").equals("pushing"))
                        return ajaxFailure(Constants.REQUEST_05, "已发布的节目必须设备在线才能删除！");
                }

                OnlinePushCommand.pushMessage(did, "delProgram", sche.getString("pid"));
                scheduleService.deletePushItem(pd);
                scheduleService.deleteItem(pd);

                /////////////////////////////LOG///////////
                logservice.save(logTypeEnum.INFO.toString(), "删除推送本设备的发布单" + sid, AuthorityUtil.getRequesterUserName(getRequest()), AuthorityUtil.getRequesterUUID(getRequest()), pd.getString("did"));
                ///////////////////////////////////////////
                return ajaxSuccess(Constants.REQUEST_01, Constants.REQUEST_OK);
            } else
                return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////
    ///
    /// for file realase
    ///
    ///////////////////////////////////////////////////////////////////////////////////////////

    /**
     * file新增接口
     *
     * @return
     */
    @RequestMapping(value = "/file", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse releaseFile() {
        try {
            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_PUSH);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            PageData pd = this.getPageData();
            if (null != pd && pd.size() > 0) {
                String sid = this.get32UUID();
                PageData schedule = new PageData();
                List<String> devlist = new ArrayList<String>();
                pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                pd.put("sid", sid);
                pd.put("stype", "file");
                pd.put("sstate", "new");
                pd.put("addDate", DateUtil.getTime());
                if (pd.get("gid") == null || "".equals(pd.getString("gid"))) {
                    pd.remove("gid");
                    String[] dvl = pd.getString("did").split(",");
                    for (int i = 0; i < dvl.length; i++) {
                        devlist.add(dvl[i]);
                    }
                } else {
                    PageData gp = new PageData();
                    gp.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                    gp.put("gpid", pd.getString("gid"));
                    List<PageData> gl = null;
                    try {
                        gl = groupsservice.listAll(gp);
                    } catch (Exception ex) {
                        gl = new ArrayList<PageData>();
                    }
                    gp.put("gid", Integer.valueOf(pd.getString("gid")));
                    //gl.add(gp);

                    for (int g = 0; g < gl.size(); g++) {
                        PageData g_i = gl.get(g);
                        gp.clear();
                        gp.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                        gp.put("gid", (int) g_i.get("gid"));

                        List<PageData> dl = null;
                        try {
                            dl = deviceservice.listAll(gp);
                        } catch (Exception ex) {
                            dl = new ArrayList<PageData>();
                        }

                        for (int d = 0; d < dl.size(); d++) {
                            PageData d_i = dl.get(d);
                            devlist.add(d_i.getString("did"));
                        }
                    }
                }
                scheduleService.save(pd);
                pd.put("id", this.get32UUID());
                pd.put("schedule", "none");
                for (int ds = 0; ds < devlist.size(); ds++) {
                    pd.put("did", devlist.get(ds));
                    pd.put("state", "pushing");
                    scheduleService.saveItem(pd);
                    /////////////////////////////LOG///////////
                    logservice.save(logTypeEnum.INFO.toString(), "发布文件，等待审核", AuthorityUtil.getRequesterUserName(getRequest()), AuthorityUtil.getRequesterUUID(getRequest()), pd.getString("did"));
                    ///////////////////////////////////////////
                }

                return ajaxSuccess(Constants.REQUEST_01, Constants.REQUEST_OK);
            } else {
                return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
            }

        } catch (Exception ex) {
            logger.info("add schedule error !!!");
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * file审核接口
     *
     * @return
     */
    @RequestMapping(value = "/file/review/{id}", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse reviewFile(@PathVariable("id") String id) {
        try {
            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_REVIEW);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            PageData pd = this.getPageData();
            if (null != pd && pd.size() > 0) {

                PageData schedule = new PageData();
                pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                pd.put("sid", id);
                pd.put("stype", "file");
                PageData sche = scheduleService.findById(pd);
                if (sche != null && sche.getString("sstate").equals("new")) {
                    PageData pamra = new PageData();
                    pamra.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                    pamra.put("fid", sche.getString("pid"));//针对文件发布，PID存储的实际是FID，根据stype去判断是节目还是文件推送
                    PageData file_l = filesService.findById(pamra);
                    List<PageData> sche_l = scheduleService.itemListAll(pd);
                    for (int s = 0; s < sche_l.size(); s++) {
                        PageData s_i = sche_l.get(s);
                        JSONArray resjl = new JSONArray();
                        if (file_l != null) {

                            String res = file_l.getString("fpath");
                            if (res != null && !"".equals(res)) {
                                PageData push_i = new PageData();
                                push_i.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                                push_i.put("sid", s_i.getString("sid"));
                                push_i.put("did", s_i.getString("did"));
                                push_i.put("resname", file_l.getString("fpath"));
                                push_i.put("resid", file_l.getString("fid"));
                                push_i.put("state", "push");
                                push_i.put("updatedate", DateUtil.getTime());

                                scheduleService.savePushItem(push_i);

                                JSONObject res_j = new JSONObject();
                                res_j.element("did", s_i.getString("did"));
                                res_j.element("sid", s_i.getString("sid"));
                                res_j.element("resurl", push_i.getString("resname"));
                                res_j.element("respath", file_l.getString("fname") + ".torrent");
                                res_j.element("type", "file");
                                resjl.put(res_j);
                            }
                        }

                        if (resjl.length() > 0) {
                            OnlinePushCommand.pushMessage(s_i.getString("did"), "pushFileList", resjl.toString());
                            /////////////////////////////LOG///////////
                            logservice.save(logTypeEnum.INFO.toString(), "审核文件并推送到设备", AuthorityUtil.getRequesterUserName(getRequest()), AuthorityUtil.getRequesterUUID(getRequest()), s_i.getString("did"));
                            ///////////////////////////////////////////
                        }
                    }
                    sche.put("sstate", "reviewed");
                    scheduleService.reView(sche);
                    return ajaxSuccess(Constants.REQUEST_01, Constants.REQUEST_OK);
                } else
                    return ajaxFailure(Constants.REQUEST_03, "节目单不存在或已审核");


            } else {
                return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
            }

        } catch (Exception ex) {
            logger.info("add schedule error !!!");
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * file推送接口
     *
     * @return
     */
    @RequestMapping(value = "/file/push/{id}", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse pushFile(@PathVariable("id") String id) {
        try {
            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_PUSH);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            PageData pd = this.getPageData();
            if (null != pd && pd.size() > 0) {

                PageData schedule = new PageData();
                pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                pd.put("sid", id);
                pd.put("stype", "file");
                PageData sche = scheduleService.findById(pd);
                if (sche != null && sche.getString("sstate").equals("reviewed")) {
                    PageData pamra = new PageData();
                    pamra.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                    pamra.put("fid", sche.getString("pid"));//针对文件发布，PID存储的实际是FID，根据stype去判断是节目还是文件推送
                    PageData file_l = filesService.findById(pamra);
                    List<PageData> sche_l = scheduleService.itemListAll(pd);
                    for (int s = 0; s < sche_l.size(); s++) {
                        PageData s_i = sche_l.get(s);
                        JSONArray resjl = new JSONArray();
                        if (file_l != null) {
                            String res = file_l.getString("fpath");
                            if (res != null && !"".equals(res)) {
                                PageData push_i = new PageData();
                                push_i.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                                push_i.put("sid", s_i.getString("sid"));
                                push_i.put("did", s_i.getString("did"));
                                push_i.put("resname", file_l.getString("fpath"));
                                push_i.put("resid", file_l.getString("fid"));
                                push_i.put("state", "push");
                                push_i.put("updatedate", DateUtil.getTime());

                                PageData it = scheduleService.findPushItemById(push_i);
                                if (it != null)
                                    scheduleService.editPushState(push_i);
                                else
                                    scheduleService.savePushItem(push_i);

                                JSONObject res_j = new JSONObject();
                                res_j.element("did", s_i.getString("did"));
                                res_j.element("sid", s_i.getString("sid"));
                                res_j.element("resurl", push_i.getString("resname"));
                                res_j.element("respath", file_l.getString("fname") + ".torrent");
                                res_j.element("type", "file");
                                resjl.put(res_j);
                            }
                        }

                        if (resjl.length() > 0) {
                            OnlinePushCommand.pushMessage(s_i.getString("did"), "pushFileList", resjl.toString());
                            /////////////////////////////LOG///////////
                            logservice.save(logTypeEnum.INFO.toString(), "推送文件到设备", AuthorityUtil.getRequesterUserName(getRequest()), AuthorityUtil.getRequesterUUID(getRequest()), s_i.getString("did"));
                            ///////////////////////////////////////////
                        }
                    }
                    return ajaxSuccess(Constants.REQUEST_01, Constants.REQUEST_OK);
                } else
                    return ajaxFailure(Constants.REQUEST_03, "发布请求不存在或未审核");


            } else {
                return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
            }

        } catch (Exception ex) {
            logger.info("add schedule error !!!");
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = "/files", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse fileList() {
        try {
            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_PUSH);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            PageData pd = this.getPageData();
            Page page = new Page();
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            pd.put("stype", "file");
            page.setPd(pd);
            page.setOffset(pd.getString("offset") == null ? 0 : Integer.valueOf(pd.getString("offset")));
            List<PageData> result = scheduleService.list(page);
            for (int i = 0; i < result.size(); i++) {
                PageData pd_i = result.get(i);

                PageData extP = new PageData();//获取扩展信息
                extP.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                extP.put("sid", pd_i.getString("sid"));
                List<PageData> p_dev = scheduleService.itemListAll(extP);
                if (null != p_dev) {
                    pd_i.put("devlist", p_dev);
                } else
                    pd_i.put("devlist", new ArrayList());

                result.set(i, pd_i);
            }
            return ajaxSuccessPage("release", result, page, Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * 查询设备下面的播放列表
     */
    @RequestMapping(value = "/file/dev/{id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse fileListByDid(@PathVariable("id") String id) {
        try {

            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Device_READ);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);


            PageData pd = this.getPageData();
            pd.put("did", id);
            pd.put("stype", "file");
            List<PageData> result = scheduleService.listByDid(pd);
            return ajaxSuccess(result, Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * 查询指定设备，节目单的文件列表推送情况
     */
    @RequestMapping(value = "/file/item", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse fileitem() {
        try {

            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Device_READ);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);


            PageData pd = this.getPageData();
            pd.put("did", pd.getString("did"));
            pd.put("sid", pd.getString("sid"));
            List<PageData> result = scheduleService.pushItemList(pd);
            for (int i = 0; i < result.size(); i++) {
                PageData d_i = result.get(i);
                PageData parma = new PageData();
                parma.put("fid", d_i.getString("resid"));
                parma.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                PageData res_i = filesService.findById(parma);
                if (res_i != null) {
                    d_i.put("fname", res_i.getString("fname"));
                }
                result.set(i, d_i);
            }
            return ajaxSuccess(result, Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * 删除产品
     */
    @RequestMapping(value = "/file/{id}", method = RequestMethod.DELETE, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse deleteByFId(@PathVariable("id") String id) {
        try {

            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Program_PUSH);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            if (null == id) {
                return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
            }

            PageData pd = new PageData();
            pd.put("sid", id);
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            scheduleService.deletePushItem(pd);
            scheduleService.deleteItem(pd);
            scheduleService.delete(pd);

            return ajaxSuccess(Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }
}
