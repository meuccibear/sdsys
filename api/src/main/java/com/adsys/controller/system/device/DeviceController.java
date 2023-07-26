package com.adsys.controller.system.device;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.java_websocket.WebSocket;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.adsys.common.enums.logTypeEnum;
import com.adsys.common.enums.permissionEnum;
import com.adsys.controller.base.BaseController;
import com.adsys.entity.Page;
import com.adsys.plugin.websocketOnline.OnlineDeviceServerPool;
import com.adsys.plugin.websocketOnline.OnlinePushCommand;
import com.adsys.service.adEditor.program.impl.AdProgramService;
import com.adsys.service.system.customer.CustomerManager;
import com.adsys.service.system.device.DeviceManager;
import com.adsys.service.system.device.ext.impl.DeviceExtService;
import com.adsys.service.system.device.impl.DeviceService;
import com.adsys.service.system.device.timepref.impl.DeviceTimePrefService;
import com.adsys.service.system.group.GroupManager;
import com.adsys.service.system.group.impl.GroupService;
import com.adsys.service.system.log.impl.LogService;
import com.adsys.service.system.schedule.impl.ScheduleService;
import com.adsys.util.AppUtil;
import com.adsys.util.AuthorityUtil;
import com.adsys.util.Const;
import com.adsys.util.Constants;
import com.adsys.util.DateUtil;
import com.adsys.util.JwtUtil;
import com.adsys.util.PageData;
import com.adsys.util.PlayingMonCache;
import com.adsys.util.TXLiveUrl;
import com.adsys.util.Tools;
import com.adsys.util.json.JsonResponse;

import net.sf.json.JSONObject;


@Controller
@RequestMapping(value = "/devices")
public class DeviceController extends BaseController {

    @Resource(name = "deviceService")
    private DeviceService deviceservice;

    @Resource(name = "deviceExtService")
    private DeviceExtService deviceextservice;

    @Resource(name = "logService")
    private LogService logservice;

    @Resource(name = "groupService")
    private GroupManager groupsservice;

    @Resource(name = "customerService")
    private CustomerManager customerService;

    @Resource(name = "deviceTimePrefService")
    private DeviceTimePrefService devicetimeprefservice;

    @Resource(name = "adProgramService")
    private AdProgramService adProgramservice;

    /**
     * 新增接口
     *
     * @return
     */
    @RequestMapping(value = "/device", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse addDevice() {
        try {
            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Device_EDIT);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            PageData pd = this.getPageData();
            if (null != pd && pd.size() > 0) {
                JsonResponse checkResult = deviceParamsCheck(pd);
                if (checkResult.isSuccess() == false) {
                    return checkResult;
                }
                JwtUtil jwt = new JwtUtil();
                JSONObject obj = new JSONObject();
                obj.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                obj.put("did", pd.getString("did"));
                String token = jwt.createJWT(Const.JWT_ID, obj.toString(), Const.JWT_TTL);

                pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                pd.put("dtoken", token);
                pd.put("deploydate", DateUtil.getTime());
                pd.put("operdate", DateUtil.getTime());
                PageData dev = deviceservice.findById(pd);
                if (dev == null) {
                    PageData cs_p = new PageData();
                    cs_p.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                    PageData cs = customerService.findById(cs_p);
                    if (cs != null) {
                        int installnum = Integer.valueOf(cs.getString("installnum"));
                        int limitnum = Integer.valueOf(cs.getString("limitnum"));

                        if (limitnum != 0) {
                            if (installnum >= limitnum)
                                return ajaxFailure(Constants.REQUEST_03, "设备数已到达授权上限");
                        }
                        if (cs.getString("expriedate") != null && cs.getString("expriedate") != "") {
                            Date exprie = DateUtil.fomatDateTime(cs.getString("expriedate"));
                            if (System.currentTimeMillis() > exprie.getTime())
                                return ajaxFailure(Constants.REQUEST_03, "已到达授权时间，请联系客服");
                        }
                        deviceservice.save(pd);

                        customerService.addInstallNum(cs);//增加安装数记录
                        /////////////////////////////LOG///////////
                        logservice.save(logTypeEnum.INFO.toString(), "添加新设备", AuthorityUtil.getRequesterUserName(getRequest()), AuthorityUtil.getRequesterUUID(getRequest()), pd.getString("did"));
                        ///////////////////////////////////////////
                        PageData rst = new PageData();
                        rst.put("did", pd.getString("did"));
                        rst.put("token", token);
                        return ajaxSuccess(rst, Constants.REQUEST_01, Constants.REQUEST_OK);
                    }
                }
                return ajaxFailure(Constants.REQUEST_03, "你已经添加此设备");
            } else {
                return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
            }

        } catch (Exception ex) {
            logger.info("add device error !!!");
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = "/device", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse deviceList() {
        try {
            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Device_READ);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            PageData pd = this.getPageData();
            Page page = new Page();
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            if (pd.getString("keywords") != null && !"".equals(pd.getString("keywords")))
                pd.put("keywords", pd.getString("keywords"));

            if (pd.getString("gid") != null && !"".equals(pd.getString("gid"))) {
                List<Integer> gids = new ArrayList<Integer>();
                PageData gpd = new PageData();
                gpd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                gpd.put("gpid", pd.getString("gid"));
                List<PageData> gresult = groupsservice.listAll(gpd);
                for (int i = 0; i < gresult.size(); i++) {
                    gids.add((int) gresult.get(i).get("gid"));
                }
                pd.put("giditem", gids);
            }

            if (pd.getString("cid") != null && !"".equals(pd.getString("cid"))) {
                if ("admin".equals(AuthorityUtil.getRequesterUserType(getRequest()))) {
                    pd.put("uuid", pd.getString("cid"));
                }
            }

            page.setPd(pd);
            page.setOffset(pd.getString("offset") == null ? 0 : Integer.valueOf(pd.getString("offset")));
            List<PageData> result = deviceservice.list(page);
            for (int i = 0; i < result.size(); i++) {
                PageData pd_i = result.get(i);

                PageData gpd = new PageData();
                gpd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
                gpd.put("gid", (int) pd_i.get("gid"));
                PageData grp = groupsservice.findById(gpd);
                if (grp != null)
                    pd_i.put("group", grp);

                PageData cs_p = new PageData();
                cs_p.put("uuid", pd_i.getString("uuid"));
                PageData cs = customerService.findById(cs_p);
                pd_i.put("cname", cs.getString("cname"));

                PageData extP = new PageData();//获取扩展信息
                extP.put("did", pd_i.getString("did"));
                //extP.put("uuid", pd_i.getString("uuid"));
                PageData p_ext = deviceextservice.findById(extP);
                if (null != p_ext) {
                    PageData py = PlayingMonCache.getDevPlayMon(pd_i.getString("did"));
                    if (py == null)
                        p_ext.put("playing", "Unknown");
                    else
                        p_ext.put("playing", py.getString("name"));
                    pd_i.put("ext", p_ext);
                } else {
                    PageData defExt = new PageData();
                    defExt.put("ipaddr", "Unknown");
                    defExt.put("nettype", "Unknown");
                    defExt.put("space", "Unknown");
                    defExt.put("mac", "Unknown");
                    defExt.put("volume", "Unknown");
                    defExt.put("sysvision", "Unknown");
                    defExt.put("ipdns", "Unknown");
                    defExt.put("ssid", "Unknown");
                    defExt.put("gateway", "Unknown");
                    defExt.put("apn", "Unknown");
                    defExt.put("disptype", "Unknown");
                    defExt.put("width", "Unknown");
                    defExt.put("height", "Unknown");
                    defExt.put("brightness", "Unknown");
                    defExt.put("playing", "Unknown");
                    pd_i.put("ext", defExt);
                }
//                String beatdate = DateUtil.formatSdfTimes((Timestamp) pd_i.get("beatdate"));
//                //判断心跳是否超时，如果超时将状态设为离线
//                int mins = DateUtil.getDiffMin(beatdate, DateUtil.getTime());
//                logger.debug("mins:" + mins + " beatdate:" + beatdate);
//                if (mins > 1 && "online".equals(pd_i.getString("dstatus"))) {
//                    pd_i.put("dstatus", "offline");
//                    deviceservice.updateState(pd_i);
//                }

                result.set(i, pd_i);
            }
            return ajaxSuccessPage("device", result, page, Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = "/program/couter", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse deviceCouter() {
        try {
            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Device_READ);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            PageData pd = this.getPageData();
            Page page = new Page();
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));

            page.setPd(pd);
            page.setOffset(pd.getString("offset") == null ? 0 : Integer.valueOf(pd.getString("offset")));
            List<PageData> result = adProgramservice.couterPageList(page);

            return ajaxSuccessPage("couter", result, page, Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * 查询产品详情
     */
    @RequestMapping(value = "/device/{id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse findDeviceById(@PathVariable("id") String id) {
        try {
            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Device_READ);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            PageData pd = this.getPageData();

            pd.put("did", id);
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            PageData pageData = deviceservice.findById(pd);
            pageData.put("dtoken", "");//不显示TOKEN

            PageData extP = new PageData();//获取扩展信息
            extP.put("did", pageData.getString("did"));
            //extP.put("uuid", pageData.getString("uuid"));
            PageData p_ext = deviceextservice.findById(extP);
            if (null != p_ext) {
                pageData.put("ext", p_ext);
            } else {
                PageData defExt = new PageData();
                defExt.put("ipaddr", "Unknown");
                defExt.put("nettype", "Unknown");
                defExt.put("space", "Unknown");
                defExt.put("resolution", "Unknown");
                defExt.put("volume", "Unknown");
                defExt.put("sysvision", "Unknown");
                pageData.put("ext", defExt);
            }

            PageData gpd = new PageData();
            gpd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            gpd.put("gid", (int) pageData.get("gid"));
            PageData grp = groupsservice.findById(gpd);
            if (grp != null)
                pageData.put("group", grp);

            //判断心跳是否超时，如果超时将状态设为离线
//            int mins = DateUtil.getDiffMin(DateUtil.formatSdfTimes((Timestamp) pageData.get("beatdate")), DateUtil.getTime());
//            if (mins > 3 && "online".equals(pageData.getString("dstatus"))) {
//                pageData.put("dstatus", "offline");
//                deviceservice.updateState(pageData);
//            }

            return ajaxSuccess(pageData, Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * 修改设备信息
     */
    @RequestMapping(value = "/device/{id}", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse updateDevice(@PathVariable("id") String id) {
        try {

            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Device_EDIT);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            PageData pd = this.getPageData();
            if (null == id) {
                return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
            }
            pd.put("did", id);

            JsonResponse checkResult = deviceParamsCheck(pd);
            if (checkResult.isSuccess() == false) {
                return checkResult;
            }

            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            deviceservice.edit(pd);
            /////////////////////////////LOG///////////
            logservice.save(logTypeEnum.INFO.toString(), "修改设备信息", AuthorityUtil.getRequesterUserName(getRequest()), AuthorityUtil.getRequesterUUID(getRequest()), pd.getString("did"));
            ///////////////////////////////////////////
            return ajaxSuccess(Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * 查询列表:树形
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse toList() {
        try {
            PageData pd = this.getPageData();
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            List<PageData> result = deviceservice.listAll(pd);
            return ajaxSuccess(result, Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * 查询位置列表
     */
    @RequestMapping(value = "/locations", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse devicesLocation() {
        try {
            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Device_READ);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            PageData pd = this.getPageData();
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            List<PageData> result = deviceservice.devicesLocation(pd);

            return ajaxSuccess(result, Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * 删除产品
     */
    @RequestMapping(value = "/device/{id}", method = RequestMethod.DELETE, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse deleteById(@PathVariable("id") String id) {
        try {

            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Device_EDIT);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            if (null == id) {
                return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
            }

            PageData pd = new PageData();
            pd.put("did", id);
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            try {
                deviceextservice.delete(pd);
                PageData cs = customerService.findById(pd);
                customerService.minusInstallNum(cs);
            } catch (Exception ex) {
                logger.error(ex);
            }
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            deviceservice.delete(pd);
            /////////////////////////////LOG///////////
            logservice.save(logTypeEnum.WARNING.toString(), "删除设备", AuthorityUtil.getRequesterUserName(getRequest()), AuthorityUtil.getRequesterUUID(getRequest()), pd.getString("did"));
            ///////////////////////////////////////////
            return ajaxSuccess(Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * 主动通知设备消息
     */
    @RequestMapping(value = "/notify/{id}", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse notifyDevice(@PathVariable("id") String id) {
        try {

            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Device_EDIT);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            PageData pd = this.getPageData();
            if (null == id) {
                return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
            }


            if (null == pd.getString("command") || "".equals(pd.getString("command"))) {
                return ajaxFailure(Constants.REQUEST_03, "请输入命令");
            }

            if (null == pd.getString("data") || "".equals(pd.getString("data"))) {
                return ajaxFailure(Constants.REQUEST_03, "请输入发送内容");
            }

            pd.put("did", id);
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            deviceservice.findById(pd);

            WebSocket dev = OnlineDeviceServerPool.getWebSocketByUser(id);
            if (dev != null) {
                OnlinePushCommand.pushMessage(id, pd.getString("command"), pd.getString("data"));

                return ajaxSuccess(Constants.REQUEST_01, Constants.REQUEST_OK);
            } else
                return ajaxFailure(Constants.REQUEST_05, "设备离线");


        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    @RequestMapping(value = "/live/{id}", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse deviceLivePush(@PathVariable("id") String id) {
        try {

            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Device_EDIT);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            PageData pd = this.getPageData();
            if (null == id) {
                return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
            }


            if (null == pd.getString("src") || "".equals(pd.getString("src"))) {
                return ajaxFailure(Constants.REQUEST_03, "请输入直播源");
            }

            if (null == pd.getString("action") || "".equals(pd.getString("action"))) {
                return ajaxFailure(Constants.REQUEST_03, "请输入控制参数");
            }

            pd.put("did", id);
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            deviceservice.findById(pd);

            WebSocket dev = OnlineDeviceServerPool.getWebSocketByUser(id);
            if (dev != null) {
                JSONObject pdata = new JSONObject();
                if ("screen".equals(pd.getString("src")))
                    pdata.put("pushsrc", 1);
                else
                    pdata.put("pushsrc", 0);
                pdata.put("pushact", pd.getString("action"));
                //pdata.put("pushurl", "rtmp://26168.livepush.myqcloud.com/live/26168_7c74f5b470?bizid=26168&txSecret=358c873fb9b8101051288fb4e44154a2&txTime=5B37A8FF");
                pdata.put("pushurl", TXLiveUrl.getSafeUrl(Const.TXLIVE_URL_KEY, Const.TXLIVE_BIZID + "_" + id, DateUtil.currDateAddDay(5).getTime() / 1000));
                OnlinePushCommand.pushMessage(id, "pushVideo", pdata.toString());


                return ajaxSuccess(Constants.REQUEST_01, Constants.REQUEST_OK);
            } else
                return ajaxFailure(Constants.REQUEST_05, "设备离线");


        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    @RequestMapping(value = "/live/url/{id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse deviceLivePushUrl(@PathVariable("id") String id) {
        try {

            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Device_EDIT);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            PageData pd = this.getPageData();
            if (null == id) {
                return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
            }


            pd.put("did", id);
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            deviceservice.findById(pd);

            //WebSocket dev = OnlineDeviceServerPool.getWebSocketByUser(id);
            //if(dev != null){
            PageData lurl = new PageData();
            lurl.put("url", TXLiveUrl.getPlayUrl(id));

            return ajaxSuccess(lurl, Constants.REQUEST_01, Constants.REQUEST_OK);
            //}
            //else
            //	return ajaxFailure(Constants.REQUEST_05,"设备离线");


        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }


    /**
     * 查询产品详情
     */
    @RequestMapping(value = "/timepref/{id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse devTimePrefById(@PathVariable("id") String id) {
        try {
            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Device_READ);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            PageData pd = this.getPageData();

            pd.put("did", id);
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            List<PageData> tlist = devicetimeprefservice.listAll(pd);
            return ajaxSuccess(tlist, Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }

    /**
     * 删除产品
     */
    @RequestMapping(value = "/timepref/{id}", method = RequestMethod.DELETE, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public JsonResponse deleteTiemPrefById(@PathVariable("id") String id) {
        try {

            boolean auth = AuthorityUtil.checkPermissionByRole(getRequest(), permissionEnum.Device_EDIT);
            if (auth == false)
                return ajaxFailure(Constants.REQUEST_404, Constants.PERMISSION_FAILL);

            if (null == id) {
                return ajaxFailure(Constants.REQUEST_03, Constants.REQUEST_PARAM_ERROR);
            }

            PageData pd = new PageData();
            pd.put("id", id);
            pd.put("uuid", AuthorityUtil.getRequesterUUID(getRequest()));
            try {
                devicetimeprefservice.delete(pd);
            } catch (Exception ex) {
                logger.error(ex);
            }

            return ajaxSuccess(Constants.REQUEST_01, Constants.REQUEST_OK);
        } catch (Exception ex) {
            logger.error(ex);
            return ajaxFailure(Constants.REQUEST_05, Constants.REQUEST_FAILL);
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    private JsonResponse deviceParamsCheck(PageData params) {

        if (null == params.getString("did") || "".equals(params.getString("did"))) {
            return ajaxFailure(Constants.REQUEST_03, "请输入设备ID");
        }

        if (null == params.getString("dname") || "".equals(params.getString("dname"))) {
            return ajaxFailure(Constants.REQUEST_03, "请输入设备名称");
        }

//		if(null == params.getString("daddr") || "".equals(params.getString("daddr"))){
//			return ajaxFailure(Constants.REQUEST_03, "请输入设备部署地址");
//		}


        return ajaxSuccess(Constants.REQUEST_01, Constants.REQUEST_OK);
    }

}
